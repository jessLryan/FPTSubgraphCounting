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
        return "filepath: " + filepath + "\n" +
                "graph type: " + graphType + "\n" +
                "order: " + order + "\n" +
                "number of edges: " + numEdges + "\n" +
                "average degree: " + averageDegree + "\n" +
                "maximum degree: " + maxDegree + "\n" ;
    }
}