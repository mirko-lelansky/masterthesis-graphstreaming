/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package de.fhb.bigdata.examples.bipartite;

import java.io.File;
import java.util.concurrent.Callable;

import org.apache.flink.graph.Edge;
import org.apache.flink.graph.streaming.GraphStream;
import org.apache.flink.graph.streaming.SimpleEdgeStream;
import org.apache.flink.graph.streaming.library.BipartitenessCheck;
import org.apache.flink.graph.streaming.summaries.Candidates;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.types.NullValue;
import picocli.CommandLine;

/**
 * This is the main command of the application.
 *
 * @author mlelansky
 */
@CommandLine.Command(description = "This program checks the bipartite of a "
+  "graph and writes the result back to the cmd or in a csv file.",
version = "Version: 0.1.0")
public class MainCommand implements Callable<Void> {

    /**
     * This is the input graph parameter.
     */
    @CommandLine.Parameters(arity = "1", paramLabel = "INPUT",
    description = "The input graph file.")
    private File inputFilePath;

    /**
     * This is the output csv path.
     */
    @CommandLine.Option(names = {"-o", "--output"},
    description = "The output path.")
    private File outputFilePath;

    /**
     * This is the option for the version info.
     */
    @SuppressWarnings("PMD.UnusedPrivateField")
    @CommandLine.Option(names = {"-v", "--version"}, versionHelp = true,
            description = "display version info")
    private boolean versionInfoRequested;

    /**
     * This is the option for the help info.
     */
    @SuppressWarnings("PMD.UnusedPrivateField")
    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true,
            description = "display this help message")
    private boolean usageHelpRequested;

    @Override
    public Void call() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        GraphStream<Long, NullValue, NullValue> graph = new SimpleEdgeStream<>(parseGraph(env), env);
        DataStream<Candidates> candidatesStream = graph.aggregate(new BipartitenessCheck<>(500L));
        if (this.outputFilePath != null) {
           candidatesStream.writeAsCsv(this.outputFilePath.getCanonicalPath());
        } else {
            candidatesStream.print();
        }
        env.execute("Bipartite Check");
        return null;
    }

    /**
     * This method pares the graph events.
     *
     * @param env the streaming environment
     * @return the result stream
     */
    private DataStream<Edge<Long, NullValue>> parseGraph(final StreamExecutionEnvironment env) {
        return env.readTextFile(this.inputFilePath.getAbsolutePath()).map(value -> {
            String[] fields = value.split("\\u0009");
            long src = Integer.parseInt(fields[0]);
            long target = Integer.parseInt(fields[1]);
            return new Edge<>(src, target, new NullValue());
        });
    }
}
