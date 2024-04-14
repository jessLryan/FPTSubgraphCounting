package algorithm;

import graph.IntersectionGraph;
import graph.Vertex;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

class IntersectionGraphMergerTest {
    private final List<Vertex> CORRESPONDENCE_VERTICES = createVertices(7);
    private final IntersectionGraph SINGLETON = createSingleton();
    private final IntersectionGraph SINGLE_EDGE = createSingleEdge();
    private final IntersectionGraph DIAMOND_GRAPH = createK4Graph();
    private final IntersectionGraph TRIANGLE_GRAPH = createTriangleGraph();

    @Test
    void testMergeSingletonAndSingleEdge() {
        IntersectionGraphMerger graphMerger = new IntersectionGraphMerger(SINGLE_EDGE, SINGLETON);
        assertEquals(1, graphMerger.graph1().order());
        assertEquals(2, graphMerger.graph2().order());

        List<IntersectionGraph> mergedGraphs = graphMerger.createMergedGraphs();
        assertEquals(2, mergedGraphs.size());

        for (IntersectionGraph mergedGraph : mergedGraphs) {
            assertEquals(2, mergedGraph.order());

            Vertex firstVertex = mergedGraph.getVertices().getFirst();
            assertEquals(1, firstVertex.degree());

            Vertex secondVertex = mergedGraph.getVertices().getLast();
            assertEquals(1, secondVertex.degree());

            HashSet<Vertex> correspondenceVertices = new HashSet<>();
            correspondenceVertices.addAll(mergedGraph.vertexCorrespondence(firstVertex));
            correspondenceVertices.addAll(mergedGraph.vertexCorrespondence(secondVertex));

            Set<Vertex> allCorrespondencePresent = IntStream.range(0, 5).mapToObj(CORRESPONDENCE_VERTICES::get).collect(Collectors.toSet());
            assertEquals(allCorrespondencePresent, correspondenceVertices);
        }
    }

    @Test
    void testMergeTriangleAndK4() {
        IntersectionGraphMerger graphMerger = new IntersectionGraphMerger(DIAMOND_GRAPH, TRIANGLE_GRAPH);
        List<IntersectionGraph> mergedGraphs = graphMerger.createMergedGraphs();
        assertEquals(72, mergedGraphs.size());

        for (IntersectionGraph mergedGraph : mergedGraphs) {
            HashSet<Vertex> correspondenceVertices = new HashSet<>();
            for (Vertex vertex : mergedGraph.getVertices()) {
                correspondenceVertices.addAll(mergedGraph.vertexCorrespondence(vertex));
            }
            assertEquals(new HashSet<>(CORRESPONDENCE_VERTICES), correspondenceVertices);
        }
    }

    private List<Vertex> createVertices(int numVertices) {
        return IntStream.range(0, numVertices).mapToObj(i -> new Vertex()).toList();
    }

    private IntersectionGraph createSingleton() {
        Vertex vertex = new Vertex();
        HashMap<Vertex, HashSet<Vertex>> correspondence = new HashMap<>();
        correspondence.put(vertex, new HashSet<>(Set.of(CORRESPONDENCE_VERTICES.get(4))));
        return new IntersectionGraph(new ArrayList<>(List.of(vertex)), correspondence);
    }

    private IntersectionGraph createSingleEdge() {
        Vertex vertex1 = new Vertex();
        HashSet<Vertex> vertex1Correspondence = new HashSet<>();
        vertex1Correspondence.add(CORRESPONDENCE_VERTICES.getFirst());
        vertex1Correspondence.add(CORRESPONDENCE_VERTICES.get(1));
        vertex1Correspondence.add(CORRESPONDENCE_VERTICES.get(2));

        Vertex vertex2 = new Vertex();
        HashSet<Vertex> vertex2Correspondence = new HashSet<>();
        vertex1Correspondence.add(CORRESPONDENCE_VERTICES.get(3));

        HashMap<Vertex, HashSet<Vertex>> correspondence = new HashMap<>();
        correspondence.put(vertex1, vertex1Correspondence);
        correspondence.put(vertex2, vertex2Correspondence);

        vertex1.addNeighbour(vertex2);
        vertex2.addNeighbour(vertex1);

        return new IntersectionGraph(new ArrayList<>(List.of(vertex1, vertex2)), correspondence);
    }

    private IntersectionGraph createTriangleGraph() {
        ArrayList<Vertex> vertices = new ArrayList<>();
        HashMap<Vertex, HashSet<Vertex>> correspondence = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            Vertex vertex = new Vertex();
            HashSet<Vertex> vertexCorrespondence = new HashSet<>();

            vertexCorrespondence.add(CORRESPONDENCE_VERTICES.get(6 - i));
            correspondence.put(vertex, vertexCorrespondence);

            for (Vertex neighbour : vertices) {
                neighbour.addNeighbour(vertex);
                vertex.addNeighbour(neighbour);
            }
            vertices.add(vertex);
        }
        return new IntersectionGraph(vertices, correspondence);
    }

    private IntersectionGraph createK4Graph() {
        ArrayList<Vertex> vertices = new ArrayList<>();
        HashMap<Vertex, HashSet<Vertex>> correspondence = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            Vertex vertex = new Vertex();
            HashSet<Vertex> vertexCorrespondence = new HashSet<>();

            vertexCorrespondence.add(CORRESPONDENCE_VERTICES.get(i));
            correspondence.put(vertex, vertexCorrespondence);

            for (Vertex neighbour : vertices) {
                neighbour.addNeighbour(vertex);
                vertex.addNeighbour(neighbour);
            }
            vertices.add(vertex);
        }
        return new IntersectionGraph(vertices, correspondence);
    }

}