/**
 * This is the main command.
 *
 * @author mlelansky
 */
@CommandLine.Command(description = "This program checks the bipartite of"
+ "a graph.", version = "Version: 0.1.0")
public class MainCommand implements Callable<Void> {

    /**
     * The serialisation id.
     */
    private static final long serialVersionUID = -7588372038062730425L;

    /**
     * This is the input graph parameter.
     */
    @CommandLine.Parameters(arity = "1", paramLabel = "INPUT",
            description = "The input graph file.")
    private File inputFilePath;

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
            fs.removeSink(graph);
        }

        return null;
    }

}
