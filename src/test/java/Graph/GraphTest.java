package Graph;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

class GraphTest {

    private final Graph graph = createGraph();

    @Test
    void testOrder() {
        assert graph.order() == 5;
    }

    @Test
    void testEdgeCount() {
        assert graph.edgeCount() == 3;
    }

    @Test
    void testMaxDeg() {
        assert graph.maxDeg() == 2;
    }

    @Test
    void testGetHighestDegVertex() {
        List<Vertex> highestDegVertices = graph.getHighestDegVertices(1);
        assert highestDegVertices.size() == 1;
        assert highestDegVertices.getFirst().degree() == 2;
    }

    @Test
    void testGetTwoHighestDegVertices() {
        List<Vertex> highestDegVertices = graph.getHighestDegVertices(2);
        assert highestDegVertices.size() == 2;
        assert highestDegVertices.getFirst().degree() == 2;
        assert highestDegVertices.getLast().degree() == 1;
    }

    @Test
    void testMaxDegRemainingRemoveNoVertices() {
        assert graph.maxDegRemaining(0) == 2;
    }

    @Test
    void testMaxDegRemainingRemoveOneVertex() {
        assert graph.maxDegRemaining(1) == 1;
    }

    @Test
    void testGetConnectedComponentsAllVerticesPresent() {
        List<IntersectionGraph> components = graph.getConnectedComponentsWithoutVertices(List.of());
        assert components.size() == 2;

        int totalVertices = components.getFirst().order() + components.getLast().order();
        assert totalVertices == 5;

        int maxDeg = Math.max(components.getFirst().maxDeg(), components.getLast().maxDeg());
        assert maxDeg == 2;
    }

    @Test
    void testGetConnectedComponentsWithoutHighestDegreeVertex() {
        List<Vertex> verticesToRemove = List.of(graph.vertices.getFirst());
        List<IntersectionGraph> components = graph.getConnectedComponentsWithoutVertices(verticesToRemove);
        assert components.size() == 3;

        int totalVertices = components.getFirst().order() + components.get(1).order() + components.getLast().order();
        assert totalVertices == 4;

        int maxDeg = Math.max(components.getFirst().maxDeg(), components.getLast().maxDeg());
        maxDeg = Math.max(maxDeg, components.get(1).maxDeg());
        assert maxDeg == 1;
    }


    private Graph createGraph() {
        List<Vertex> vertices = createVertices();
        vertices.get(0).addNeighbour(vertices.get(4));
        vertices.get(4).addNeighbour(vertices.get(0));

        vertices.get(0).addNeighbour(vertices.get(2));
        vertices.get(2).addNeighbour(vertices.get(0));

        vertices.get(3).addNeighbour(vertices.get(1));
        vertices.get(1).addNeighbour(vertices.get(3));
        return new Graph(vertices);
    }

    private List<Vertex> createVertices() {
        return IntStream.range(0, 5).mapToObj(i -> new Vertex()).toList();
    }
}