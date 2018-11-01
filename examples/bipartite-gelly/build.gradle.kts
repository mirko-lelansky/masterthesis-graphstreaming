plugins {
    java
    application
    checkstyle
    id("com.github.spotbugs") version "1.6.5"
    id("org.asciidoctor.convert") version "1.5.9"
    pmd
}

val junitVersion = "5.3.+"
val logbackVersion = "1.2.+"
val powerMockVersion = "1.7.+"
val slf4jVersion = "1.7.+"

val intTestImplementation by configurations.creating {
    extendsFrom(configurations["implementation"])
}

val funcTestImplementation by configurations.creating {
    extendsFrom(configurations["implementation"])
}

val intTest by sourceSets.creating {
    compileClasspath += sourceSets["main"].output + configurations["testRuntime"]
    runtimeClasspath += output + compileClasspath
}

val functTest by sourceSets.creating {
    compileClasspath += sourceSets["main"].output + configurations["testRuntime"]
    runtimeClasspath += output + compileClasspath
}

dependencies {
    implementation("org.apache.flink:flink-gelly_2.10:1.2.0")
    implementation(project(":flink-gelly-streaming"))
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    runtimeOnly("ch.qos.logback:logback-core:$logbackVersion")
    runtimeOnly("ch.qos.logback:logback-classic:$logbackVersion")
    runtimeOnly("ch.qos.logback:logback-access:$logbackVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.powermock:powermock-module-junit4:$powerMockVersion")
    testImplementation("org.powermock:powermock-api-mockito2:$powerMockVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:$junitVersion")
    checkstyle("com.puppycrawl.tools:checkstyle:8.14")
    pmd("net.sourceforge.pmd:pmd-java8:6.8.+")
    spotbugs("com.github.spotbugs:spotbugs:3.1.8")
}

application {
    mainClassName = ""
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks.named<JavaCompile>("compileJava") {
    val compilerArgs = options.compilerArgs
    compilerArgs.add("-Xlint:all")
}

tasks.named<Javadoc>("javadoc") {
    val opt = options as StandardJavadocDocletOptions
    opt.addStringOption("link", "http://docs.oracle.com/javase/${java.sourceCompatibility.majorVersion}/docs/api/")
}

tasks.named<ProcessResources>("processResources") {
    expand(mapOf("artifact" to project.name, "group" to group, "version" to version))
}

tasks.named<Test>("test") {
    useJUnitPlatform {
        includeEngines = mutableSetOf("junit-jupiter", "junit-vintage")
    }
}

tasks.register<Test>("integrationTest") {
    description = "Runs the integration tests"
    group = "verification"

    testClassesDirs = sourceSets["intTest"].output.classesDirs
    classpath = sourceSets["intTest"].runtimeClasspath

    mustRunAfter("test")
}.also { tasks["check"].dependsOn(it) }

tasks.register<Test>("functionalTest") {
    description = "Runs the functional tests"
    group = "verification"

    testClassesDirs = sourceSets["functTest"].output.classesDirs
    classpath = sourceSets["functTest"].runtimeClasspath

    mustRunAfter("integrationTest")
}.also { tasks["check"].dependsOn(it) }
