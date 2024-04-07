package Graph;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

class GraphTest {

    private final Graph graph = createGraph();

    @Test
    void testOrder() {
        assert graph.order() == 6;
    }

    @Test
    void testEdgeCount() {
        assert graph.edgeCount() == 5;
    }

    @Test
    void testMaxDeg() {
        assert graph.maxDeg() == 3;
    }

    @Test
    void testGetHighestDegVertex() {
        List<Vertex> highestDegVertices = graph.getHighestDegVertices(1);
        assert highestDegVertices.size() == 1;
        assert highestDegVertices.getFirst().degree() == 3;
    }

    @Test
    void testGetTwoHighestDegVertices() {
        List<Vertex> highestDegVertices = graph.getHighestDegVertices(2);
        assert highestDegVertices.size() == 2;
        assert highestDegVertices.getFirst().degree() == 3;
        assert highestDegVertices.getLast().degree() == 2;
    }

    @Test
    void testMaxDegRemainingRemoveNoVertices() {
        assert graph.maxDegRemaining(0) == 3;
    }

    @Test
    void testMaxDegRemainingRemoveOneVertex() {
        assert graph.maxDegRemaining(1) == 1;
    }

    @Test
    void testVertexOrdering() {
        List<Vertex> vertices = graph.vertices;
        assert vertices.getFirst().degree() == 3;
        assert vertices.get(1).degree() == 2;
        assert vertices.get(2).degree() == 2;
        assert vertices.get(3).degree() == 1;
        assert vertices.get(4).degree() == 1;
        assert vertices.getLast().degree() == 1;
    }

    @Test
    void testVertexOrderingPrecededByNeighbour() {
        Graph connectedGraph = createConnectedGraph();
        connectedGraph.orderVerticesSoEachIsPrecededByNeighbour();
        List<Vertex> vertices = connectedGraph.vertices;
        for (int i=1;i<vertices.size();i++) {
            Vertex vertex = vertices.get(i);
            Vertex neighbourPrecedingInList = null;
            for (int j=0;j<i;j++) {
                Vertex possibleNeighbour = vertices.get(j);
                if (vertex.isAdjacentTo(possibleNeighbour)) {
                    neighbourPrecedingInList = possibleNeighbour;
                }
            }
            assert neighbourPrecedingInList != null;
        }
    }

    @Test
    void testGetConnectedComponentsAllVerticesPresent() {
        List<IntersectionGraph> components = graph.getConnectedComponentsWithoutVertices(List.of());
        assert components.size() == 2;

        int totalVertices = components.getFirst().order() + components.getLast().order();
        assert totalVertices == 6;

        int maxDeg = Math.max(components.getFirst().maxDeg(), components.getLast().maxDeg());
        assert maxDeg == 3;
    }

    @Test
    void testGetConnectedComponentsWithoutHighestDegreeVertex() {
        List<Vertex> verticesToRemove = List.of(graph.vertices.getFirst());
        List<IntersectionGraph> components = graph.getConnectedComponentsWithoutVertices(verticesToRemove);
        assert components.size() == 3;

        int totalVertices = components.getFirst().order() + components.get(1).order() + components.getLast().order();
        assert totalVertices == 5;

        int maxDeg = Math.max(components.getFirst().maxDeg(), components.getLast().maxDeg());
        maxDeg = Math.max(maxDeg, components.get(1).maxDeg());
        assert maxDeg == 1;
    }


    private Graph createGraph() {
        ArrayList<Vertex> vertices = createVertices();
        vertices.get(0).addNeighbour(vertices.get(4));
        vertices.get(4).addNeighbour(vertices.get(0));

        vertices.get(0).addNeighbour(vertices.get(2));
        vertices.get(2).addNeighbour(vertices.get(0));

        vertices.get(3).addNeighbour(vertices.get(1));
        vertices.get(1).addNeighbour(vertices.get(3));

        vertices.get(0).addNeighbour(vertices.get(5));
        vertices.get(5).addNeighbour(vertices.get(0));

        vertices.get(4).addNeighbour(vertices.get(5));
        vertices.get(5).addNeighbour(vertices.get(4));
        return new Graph(vertices);
    }

    private Graph createConnectedGraph() {
        ArrayList<Vertex> vertices = createVertices();
        vertices.get(0).addNeighbour(vertices.get(4));
        vertices.get(4).addNeighbour(vertices.get(0));

        vertices.get(0).addNeighbour(vertices.get(2));
        vertices.get(2).addNeighbour(vertices.get(0));

        vertices.get(3).addNeighbour(vertices.get(1));
        vertices.get(1).addNeighbour(vertices.get(3));

        vertices.get(0).addNeighbour(vertices.get(5));
        vertices.get(5).addNeighbour(vertices.get(0));

        vertices.get(4).addNeighbour(vertices.get(5));
        vertices.get(5).addNeighbour(vertices.get(4));

        vertices.get(4).addNeighbour(vertices.get(3));
        vertices.get(3).addNeighbour(vertices.get(4));

        return new Graph(vertices);
    }

    private ArrayList<Vertex> createVertices() {
        return new ArrayList<>(IntStream.range(0, 6).mapToObj(i -> new Vertex()).toList());
    }
}