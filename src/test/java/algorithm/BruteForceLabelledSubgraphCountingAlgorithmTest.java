package algorithm;

import graph.Graph;
import graph.Vertex;
import graph.VertexLists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


class BruteForceLabelledSubgraphCountingAlgorithmTest {

    private final Graph PATTERN_TRIANGLE = createTriangleGraph();
    private final Graph HOST_GRAPH_1 = createFirstHostGraph();
    private final Graph HOST_GRAPH_2 = createSecondHostGraph();

    @Test
    void testPatternTriangleHostGraph1() {
        VertexLists vertexLists = new VertexLists(HOST_GRAPH_1, PATTERN_TRIANGLE);
        int count = BruteForceLabelledSubgraphCountingAlgorithm.countLabelledCopiesWithLists(PATTERN_TRIANGLE, vertexLists);
        assert count == 6;
    }

    @Test
    void testPatternTriangleHostGraph2() {
        VertexLists vertexLists = new VertexLists(HOST_GRAPH_2, PATTERN_TRIANGLE);
        int count = BruteForceLabelledSubgraphCountingAlgorithm.countLabelledCopiesWithLists(PATTERN_TRIANGLE, vertexLists);
        assert count == 12;
    }

    private Graph createTriangleGraph() {
        Vertex vertex1 = new Vertex();
        Vertex vertex2 = new Vertex();
        Vertex vertex3 = new Vertex();

        vertex1.addNeighbour(vertex2);
        vertex1.addNeighbour(vertex3);
        vertex2.addNeighbour(vertex1);
        vertex2.addNeighbour(vertex3);
        vertex3.addNeighbour(vertex1);
        vertex3.addNeighbour(vertex2);

        ArrayList<Vertex> vertices = new ArrayList<>(List.of(vertex1, vertex2, vertex3));
        return new Graph(vertices);
    }

    private Graph createFirstHostGraph() {
        Vertex vertex1 = new Vertex();
        Vertex vertex2 = new Vertex();
        Vertex vertex3 = new Vertex();
        Vertex vertex4 = new Vertex();
        Vertex vertex5 = new Vertex();

        vertex1.addNeighbour(vertex2);
        vertex1.addNeighbour(vertex5);
        vertex2.addNeighbour(vertex1);
        vertex2.addNeighbour(vertex3);
        vertex3.addNeighbour(vertex2);
        vertex3.addNeighbour(vertex4);
        vertex3.addNeighbour(vertex5);
        vertex4.addNeighbour(vertex3);
        vertex4.addNeighbour(vertex5);
        vertex5.addNeighbour(vertex1);
        vertex5.addNeighbour(vertex3);
        vertex5.addNeighbour(vertex4);

        ArrayList<Vertex> vertices = new ArrayList<>(List.of(vertex1, vertex2, vertex3, vertex4, vertex5));
        return new Graph(vertices);
    }

    private Graph createSecondHostGraph() {
        Vertex vertex1 = new Vertex();
        Vertex vertex2 = new Vertex();
        Vertex vertex3 = new Vertex();
        Vertex vertex4 = new Vertex();

        vertex1.addNeighbour(vertex2);
        vertex1.addNeighbour(vertex4);
        vertex2.addNeighbour(vertex1);
        vertex2.addNeighbour(vertex3);
        vertex2.addNeighbour(vertex4);
        vertex3.addNeighbour(vertex2);
        vertex3.addNeighbour(vertex4);
        vertex4.addNeighbour(vertex1);
        vertex4.addNeighbour(vertex3);
        vertex4.addNeighbour(vertex2);

        ArrayList<Vertex> vertices = new ArrayList<>(List.of(vertex1, vertex2, vertex3, vertex4));
        return new Graph(vertices);
    }
}