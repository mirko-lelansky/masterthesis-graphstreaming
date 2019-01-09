@CommandLine.Command(description = "This program checks the bipartite of"
+ "a graph.", version = "Version: 0.1.0")
public class MainCommand implements Callable<Void> {

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
