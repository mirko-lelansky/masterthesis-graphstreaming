/**
 * This is the main command of the application.
 *
 * @author mlelansky
 */
@CommandLine.Command(description = "This program checks the bipartite of a "
+  "graph and writes the result back to the cmd or in a text file.",
version = "Version: 0.1.0")
public class MainCommand implements Callable<Void>, Serializable {

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

    @Override
    public Void call() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        GraphStream<Long, NullValue, NullValue> graph = new SimpleEdgeStream<>(parseGraph(env), env);
        DataStream<Candidates> candidatesStream = graph.aggregate(new BipartitenessCheck<>(this.time));
        if (this.outputFilePath != null) {
           candidatesStream.writeAsText(this.outputFilePath.getCanonicalPath());
        } else {
            candidatesStream.print();
        }
        env.execute("Bipartite Check");
        return null;
    }

}
