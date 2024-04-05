package Algorithm;

import Graph.Vertex;
import Graph.VertexLists;
import Graph.Graph;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LabelledSubgraphCountingAlgorithmTest {

    private final Graph TRIANGLE = createKn(3);
    private final Graph SINGLE_EDGE = createKn(2);

    private final Graph SINGLETON = createKn(1);

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

}