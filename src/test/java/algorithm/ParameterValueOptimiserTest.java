package algorithm;

import graph.Graph;
import graph.Vertex;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

class ParameterValueOptimiserTest {

    private final Graph HOST_GRAPH_1 = createFirstHostGraph();
    private final Graph HOST_GRAPH_2 = createKn(4);
    private final Graph PATTERN_SINGLETON = createSingleton();
    private final Graph PATTERN_TRIANGLE = createKn(3);

    @Test
    void testHostGraph1PatternSingleton() {
        ParameterValueOptimiser parameterValueOptimiser = new ParameterValueOptimiser(PATTERN_SINGLETON, HOST_GRAPH_1);
        int numHighDeg = parameterValueOptimiser.getNumHighDegVertices();
        int maxDegRemaining = parameterValueOptimiser.getMaxDegRemainingVertices();
        assert numHighDeg == 1;
        assert maxDegRemaining == 3;
    }

    @Test
    void testHostGraph1PatternTriangle() {
        ParameterValueOptimiser parameterValueOptimiser = new ParameterValueOptimiser(PATTERN_TRIANGLE, HOST_GRAPH_1);
        int numHighDeg = parameterValueOptimiser.getNumHighDegVertices();
        int maxDegRemaining = parameterValueOptimiser.getMaxDegRemainingVertices();
        assert numHighDeg == 0;
        assert maxDegRemaining == 4;
    }

    @Test
    void testHostGraph2PatternSingleton() {
        ParameterValueOptimiser parameterValueOptimiser = new ParameterValueOptimiser(PATTERN_SINGLETON, HOST_GRAPH_2);
        int numHighDeg = parameterValueOptimiser.getNumHighDegVertices();
        int maxDegRemaining = parameterValueOptimiser.getMaxDegRemainingVertices();
        assert numHighDeg == 1;
        assert maxDegRemaining == 2;
    }

    @Test
    void testHostGraph2PatternTriangle() {
        ParameterValueOptimiser parameterValueOptimiser = new ParameterValueOptimiser(PATTERN_TRIANGLE, HOST_GRAPH_2);
        int numHighDeg = parameterValueOptimiser.getNumHighDegVertices();
        int maxDegRemaining = parameterValueOptimiser.getMaxDegRemainingVertices();

        assert numHighDeg == 0;
        assert maxDegRemaining == 3;
    }

    private Graph createSingleton() {
        Vertex vertex = new Vertex();
        ArrayList<Vertex> vertices = new ArrayList<>(List.of(vertex));
        return new Graph(vertices);
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

    private Graph createFirstHostGraph() {
        ArrayList<Vertex> vertices = createVertices(6);
        vertices.getFirst().addNeighbour(vertices.get(1));

        vertices.get(1).addNeighbour(vertices.getFirst());
        vertices.get(1).addNeighbour(vertices.get(2));
        vertices.get(1).addNeighbour(vertices.get(3));
        vertices.get(1).addNeighbour(vertices.get(5));

        vertices.get(2).addNeighbour(vertices.get(4));

        vertices.get(3).addNeighbour(vertices.get(1));
        vertices.get(3).addNeighbour(vertices.get(4));
        vertices.get(3).addNeighbour(vertices.get(5));

        vertices.get(4).addNeighbour(vertices.get(2));
        vertices.get(4).addNeighbour(vertices.get(3));
        vertices.get(4).addNeighbour(vertices.get(5));

        vertices.get(5).addNeighbour(vertices.get(1));
        vertices.get(5).addNeighbour(vertices.get(4));
        vertices.get(5).addNeighbour(vertices.get(3));

        return new Graph(vertices);
    }

    private ArrayList<Vertex> createVertices(int numVertices) {
        return new ArrayList<>(IntStream.range(0, numVertices).mapToObj(v -> new Vertex()).toList());
    }

}