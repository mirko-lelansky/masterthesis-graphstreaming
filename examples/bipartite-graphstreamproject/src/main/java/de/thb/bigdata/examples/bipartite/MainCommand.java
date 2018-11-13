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

package de.thb.bigdata.examples.bipartite;

import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDGS;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;



/**
 * This is the main command.
 *
 * @author mlelansky
 */
@CommandLine.Command(description = "This program checks the bipartite of"
+ "a graph.", version = "Version: 0.1.0")
public class MainCommand implements Callable<Void> {

    /**
     * This is the default window time.
     */
    private static final long MAX_WINDOW_TIME = 500L;

    /**
     * The serialisation id.
     */
    private static final long serialVersionUID = 7150263381930270834L;

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
     * This is the delimiter string.
     */
    @CommandLine.Option(names = {"-d", "--delimiter"}, description = "The nodes delimiter. Default is TAB.")
    private String delimiter = "\\u0009";

    /**
     * This is the window time frame.
     */
    @CommandLine.Option(names = {"-t", "--time"}, description = "The window time.")
    private long time = MAX_WINDOW_TIME;

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
        Graph graph = new SingleGraph("Bipartite Graph");
        FileSource fs = new FileSourceDGS();

        fs.addSink(graph);

        try {
            fs.begin(this.inputFilePath.getAbsolutePath());

            while (fs.nextEvents()) {
                BipartiteAlgorithm bipartite = new BipartiteAlgorithm();
                bipartite.init(graph);
                bipartite.compute();
                System.out.println("The graph is bipartite: " + bipartite.isBipartite());
            }
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        } finally {
            fs.end();
        }

        return null;
    }

}
