package Graph;

public class GraphStatistics {

    private final int order;
    private final int numEdges;
    private final int averageDegree;
    private final int maxDegree;
    private final String filepath;
    private final GraphType graphType;

    public GraphStatistics(Graph graph, String filepath, GraphType graphType) {
        order = graph.order();
        numEdges = graph.edgeCount();
        averageDegree = 2 * numEdges / order;
        maxDegree = graph.maxDeg();
        this.filepath = filepath;
        this.graphType = graphType;
    }

    @Override
    public String toString() {
        return "Graph.GraphStatistics{" +
                "order=" + order +
                ", numEdges=" + numEdges +
                ", averageDegree=" + averageDegree +
                ", maxDegree=" + maxDegree +
                ", filepath='" + filepath +
                ", graphType='" + graphType +
                '}';
    }
}