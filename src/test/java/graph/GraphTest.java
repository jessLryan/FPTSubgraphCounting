package graph;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

class GraphTest {

    private final Graph graph = createGraph();

    @Test
    void testOrder() {
        assertEquals(6, graph.order());
    }

    @Test
    void testEdgeCount() {
        assertEquals(5, graph.edgeCount());
    }

    @Test
    void testMaxDeg() {
        assertEquals(3, graph.maxDeg());
    }

    @Test
    void testGetHighestDegVertex() {
        List<Vertex> highestDegVertices = graph.getHighestDegVertices(1);
        assertEquals(1, highestDegVertices.size());
        assertEquals(3, highestDegVertices.getFirst().degree());
    }

    @Test
    void testGetTwoHighestDegVertices() {
        List<Vertex> highestDegVertices = graph.getHighestDegVertices(2);
        assertEquals(2, highestDegVertices.size());
        assertEquals(3, highestDegVertices.getFirst().degree());
        assertEquals(2, highestDegVertices.getLast().degree());
    }

    @Test
    void testMaxDegRemainingRemoveNoVertices() {
        assertEquals(3, graph.maxDegRemaining(0));
    }

    @Test
    void testMaxDegRemainingRemoveOneVertex() {
        assertEquals(1, graph.maxDegRemaining(1));
    }

    @Test
    void testVertexOrdering() {
        List<Vertex> vertices = graph.vertices;
        assertEquals(3, vertices.getFirst().degree());
        assertEquals(2, vertices.get(1).degree());
        assertEquals(2, vertices.get(2).degree());
        assertEquals(1, vertices.get(3).degree());
        assertEquals(1, vertices.get(4).degree());
        assertEquals(1, vertices.getLast().degree());
    }

    @Test
    void testVertexOrderingPrecededByNeighbour() {
        Graph connectedGraph = createConnectedGraph();
        connectedGraph.orderVerticesSoEachIsPrecededByNeighbour();
        List<Vertex> vertices = connectedGraph.vertices;
        for (int i = 1; i < vertices.size(); i++) {
            Vertex vertex = vertices.get(i);
            Vertex neighbourPrecedingInList = null;
            for (int j = 0; j < i; j++) {
                Vertex possibleNeighbour = vertices.get(j);
                if (vertex.isAdjacentTo(possibleNeighbour)) {
                    neighbourPrecedingInList = possibleNeighbour;
                }
            }
            assertNotNull(neighbourPrecedingInList);
        }
    }

    @Test
    void testGetConnectedComponentsAllVerticesPresent() {
        List<IntersectionGraph> components = graph.getConnectedComponentsWithoutVertices(List.of());
        assertEquals(2, components.size());

        int totalVertices = components.getFirst().order() + components.getLast().order();
        assertEquals(6, totalVertices);

        int maxDeg = Math.max(components.getFirst().maxDeg(), components.getLast().maxDeg());
        assertEquals(3, maxDeg);
    }

    @Test
    void testGetConnectedComponentsWithoutHighestDegreeVertex() {
        List<Vertex> verticesToRemove = List.of(graph.vertices.getFirst());
        List<IntersectionGraph> components = graph.getConnectedComponentsWithoutVertices(verticesToRemove);
        assertEquals(3, components.size());

        int totalVertices = components.getFirst().order() + components.get(1).order() + components.getLast().order();
        assertEquals(5, totalVertices);

        int maxDeg = Math.max(components.getFirst().maxDeg(), components.getLast().maxDeg());
        maxDeg = Math.max(maxDeg, components.get(1).maxDeg());
        assertEquals(1, maxDeg);
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