package algorithm;

import graph.Graph;
import graph.Vertex;
import graph.VertexLists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LabelledSubgraphCountingAlgorithmTest {

    private final Graph TRIANGLE = createKn(3);
    private final Graph SINGLE_EDGE = createKn(2);

    private final Graph SINGLETON = createKn(1);

    private final Graph CUSTOM_HOST = createCustomHost();

    private final Graph CUSTOM_PATTERN = createCustomPattern();

    @Test
    void testSingletonPatternTriangleHost() {
        ParameterValueOptimiser parameterOptimiser = new ParameterValueOptimiser(SINGLETON, TRIANGLE);
        int numHighDegVertices = parameterOptimiser.getNumHighDegVertices();

        List<Vertex> highestDegVertices = TRIANGLE.getHighestDegVertices(numHighDegVertices);
        LabelledSubgraphCountingAlgorithm FPTAlg = new LabelledSubgraphCountingAlgorithm(TRIANGLE, SINGLETON, highestDegVertices);
        int FPTCount = FPTAlg.run();

        VertexLists mapLists = new VertexLists(TRIANGLE, SINGLETON);
        int bruteForceCount = BruteForceLabelledSubgraphCountingAlgorithm.countLabelledCopiesWithLists(SINGLETON, mapLists);

        assertEquals(bruteForceCount, FPTCount);
    }

    @Test
    void testSingleEdgePatternTriangleHost() {
        ParameterValueOptimiser parameterOptimiser = new ParameterValueOptimiser(SINGLE_EDGE, TRIANGLE);
        int numHighDegVertices = parameterOptimiser.getNumHighDegVertices();

        List<Vertex> highestDegVertices = TRIANGLE.getHighestDegVertices(numHighDegVertices);
        LabelledSubgraphCountingAlgorithm FPTAlg = new LabelledSubgraphCountingAlgorithm(TRIANGLE, SINGLE_EDGE, highestDegVertices);
        int FPTCount = FPTAlg.run();

        VertexLists mapLists = new VertexLists(TRIANGLE, SINGLE_EDGE);
        int bruteForceCount = BruteForceLabelledSubgraphCountingAlgorithm.countLabelledCopiesWithLists(SINGLE_EDGE, mapLists);

        assertEquals(bruteForceCount, FPTCount);
    }

    @Test
    void testCustomHost() {
        ParameterValueOptimiser parameterOptimiser = new ParameterValueOptimiser(CUSTOM_PATTERN, CUSTOM_HOST);
        int numHighDegVertices = parameterOptimiser.getNumHighDegVertices();

        List<Vertex> highestDegVertices = CUSTOM_HOST.getHighestDegVertices(numHighDegVertices);
        LabelledSubgraphCountingAlgorithm FPTAlg = new LabelledSubgraphCountingAlgorithm(CUSTOM_HOST, CUSTOM_PATTERN, highestDegVertices);
        int FPTCount = FPTAlg.run();

        VertexLists mapLists = new VertexLists(CUSTOM_HOST, CUSTOM_PATTERN);
        int bruteForceCount = BruteForceLabelledSubgraphCountingAlgorithm.countLabelledCopiesWithLists(CUSTOM_PATTERN, mapLists);
        System.out.println("num high deg " + parameterOptimiser.getNumHighDegVertices());
        System.out.println("FPT " + FPTCount);
        System.out.println("BruteForce " + bruteForceCount);
        assertEquals(bruteForceCount, FPTCount);
    }


    private Graph createKn(int n) {
        ArrayList<Vertex> vertices = createVertices(n);
        for (int i = 0; i < n; i++) {
            Vertex vertex1 = vertices.get(i);
            for (int j = i + 1; j < n; j++) {
                Vertex vertex2 = vertices.get(j);
                vertex1.addNeighbour(vertex2);
                vertex2.addNeighbour(vertex1);
            }
        }
        return new Graph(vertices);
    }

    private ArrayList<Vertex> createVertices(int numVertices) {
        return new ArrayList<>(IntStream.range(0, numVertices).mapToObj(v -> new Vertex()).toList());
    }

    private Graph createCustomHost() {
        ArrayList<Vertex> vertices = new ArrayList<>();
        Vertex v0 = new Vertex();
        vertices.add(v0);
        Vertex v1 = new Vertex();
        vertices.add(v1);
        Vertex v2 = new Vertex();
        vertices.add(v2);
        Vertex v3 = new Vertex();
        vertices.add(v3);
        Vertex v4 = new Vertex();
        vertices.add(v4);
        Vertex v5 = new Vertex();
        vertices.add(v5);

        v0.addNeighbour(v1);
        v1.addNeighbour(v0);

        v0.addNeighbour(v2);
        v2.addNeighbour(v0);

        v0.addNeighbour(v3);
        v3.addNeighbour(v0);

        v0.addNeighbour(v4);
        v4.addNeighbour(v0);

        v1.addNeighbour(v5);
        v5.addNeighbour(v1);

        v2.addNeighbour(v5);
        v5.addNeighbour(v2);

        return new Graph(vertices);
    }

    private Graph createCustomPattern() {
        ArrayList<Vertex> vertices = createVertices(6);
        vertices.getFirst().addNeighbour(vertices.get(1));
        vertices.get(1).addNeighbour(vertices.get(0));

        vertices.get(1).addNeighbour(vertices.get(2));
        vertices.get(2).addNeighbour(vertices.get(1));

        vertices.get(2).addNeighbour(vertices.get(3));
        vertices.get(3).addNeighbour(vertices.get(2));

        vertices.get(3).addNeighbour(vertices.get(4));
        vertices.get(4).addNeighbour(vertices.get(3));

        vertices.get(4).addNeighbour(vertices.get(5));
        vertices.get(5).addNeighbour(vertices.get(4));

        return new Graph(vertices);
    }
}