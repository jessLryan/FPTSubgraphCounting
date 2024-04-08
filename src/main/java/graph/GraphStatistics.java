package graph;

public class GraphStatistics {

    private final String filepath;
    private final GraphType graphType;
    private final int order;
    private final int numEdges;
    private final int averageDegree;
    private final int maxDegree;

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
        return "GraphStatistics{" +
                "filepath: " + filepath + '\'' +
                ", graph type: " + graphType +
                ", order: " + order +
                ", number of edges: " + numEdges +
                ", average degree: " + averageDegree +
                ", maximum degree: " + maxDegree +
                '}';
    }
}