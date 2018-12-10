public class GraphStreamInput {

    public static void main(String[] args) {
        Graph graph = new SingleGraph("Test Graph");
        FileSource fs = new FileSourceDGS();

        fs.addSink(graph);

        try {
            fs.begin(this.inputFilePath.getAbsolutePath());

            while (fs.nextEvents()) {
                //TODO: make something with the graph
            }
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        } finally {
            fs.end();
            fs.removeSink(graph);
        }

    }

}
