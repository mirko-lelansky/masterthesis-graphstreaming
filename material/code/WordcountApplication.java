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

package test.rsq.distributed.flink.wordcount;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.util.Arrays;

/**
 * This is the example wordcount streaming application for Apache Flink.
 *
 * @author mlelansky
 */
public final class WordcountApplication {

    /**
     * This is the default constructor.
     */
    private WordcountApplication() {

    }

    /**
     * This is the main entry point of the application.
     *
     * @param args the command line arguments
     * @throws Exception if an execution error was occurred
     */
    public static void main(final String[] args) throws Exception {
        final ParameterTool cli = ParameterTool.fromArgs(args);
        final String host = cli.get("host", "localhost");
        final int port = cli.getInt("port", 8888);
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        final DataStream<Tuple2<String, Integer>> resultStream =
                env.socketTextStream(host, port, "\n")
                        .flatMap((String value, Collector<Tuple2<String, Integer>> out) ->
                                Arrays.stream(value.split(" ")).forEach(word -> {
                                    out.collect(new Tuple2<>(word, 1));
                                }))
                        .returns(new TypeHint<Tuple2<String, Integer>>() {})
                        .keyBy(0).timeWindow(Time.seconds(5))
                        .sum(1);

        resultStream.print();
        env.execute("Word Count");
    }
}
