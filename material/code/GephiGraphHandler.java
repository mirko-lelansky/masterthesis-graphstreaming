public class GephiGraphHandler {

    public static void main(String[] args) {

            AtomicInteger counter = new AtomicInteger();
            
            GraphEventHandler eventHandler = new GraphEventHandler() {
                public void handleGraphEvent(GraphEvent event) {
                    counter.incrementAndGet();
                }
            };
            
            URL url = new URL("http://streamingserver/streamingcontext");
            url.openConnection();
            InputStream inputStream = url.openStream();
 
            GraphEventBuilder eventBuilder = new GraphEventBuilder(endpoint.getUrl());

            StreamReaderFactory readerFactory = Lookup.getDefault().lookup(StreamReaderFactory.class);
            StreamReader reader = readerFactory.createStreamReader("JSON", eventHandler, eventBuilder);
            streamReader.processStream(inputStream);
    }
}
