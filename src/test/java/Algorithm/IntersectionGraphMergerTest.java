package Algorithm;

import Graph.IntersectionGraph;
import Graph.Vertex;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class IntersectionGraphMergerTest {
    private final List<Vertex> CORRESPONDENCE_VERTICES = createVertices(7);
    private final IntersectionGraph SINGLETON = createSingleton();
    private final IntersectionGraph SINGLE_EDGE = createSingleEdge();
    private final IntersectionGraph DIAMOND_GRAPH = createK4Graph();
    private final IntersectionGraph TRIANGLE_GRAPH = createTriangleGraph();

    @Test
    void testMergeSingletonAndSingleEdge() {
        IntersectionGraphMerger graphMerger = new IntersectionGraphMerger(SINGLE_EDGE, SINGLETON);
        assert graphMerger.getGraph1().order() == 1;
        assert graphMerger.getGraph2().order() == 2;

        List<IntersectionGraph> mergedGraphs = graphMerger.createMergedGraphs();
        assert mergedGraphs.size() == 2;

        for (IntersectionGraph mergedGraph : mergedGraphs) {
            assert mergedGraph.order() == 2;

            Vertex firstVertex = mergedGraph.getVertices().getFirst();
            assert firstVertex.degree() == 1;

            Vertex secondVertex = mergedGraph.getVertices().getLast();
            assert secondVertex.degree() == 1;

            HashSet<Vertex> correspondenceVertices = new HashSet<>();
            correspondenceVertices.addAll(mergedGraph.vertexCorrespondence(firstVertex));
            correspondenceVertices.addAll(mergedGraph.vertexCorrespondence(secondVertex));

            Set<Vertex> allCorrespondencePresent = IntStream.range(0, 5).mapToObj(CORRESPONDENCE_VERTICES::get).collect(Collectors.toSet());
            assert correspondenceVertices.equals(allCorrespondencePresent);
        }
    }

    @Test
    void testMergeTriangleAndK4() {
        IntersectionGraphMerger graphMerger = new IntersectionGraphMerger(DIAMOND_GRAPH, TRIANGLE_GRAPH);
        List<IntersectionGraph> mergedGraphs = graphMerger.createMergedGraphs();
        assert mergedGraphs.size() == 72;

        for (IntersectionGraph mergedGraph : mergedGraphs) {
            HashSet<Vertex> correspondenceVertices = new HashSet<>();
            for (Vertex vertex : mergedGraph.getVertices()) {
                correspondenceVertices.addAll(mergedGraph.vertexCorrespondence(vertex));
            }
            assert correspondenceVertices.equals(new HashSet<>(CORRESPONDENCE_VERTICES));
        }
    }

    private List<Vertex> createVertices(int numVertices) {
        return IntStream.range(0, numVertices).mapToObj(i -> new Vertex()).toList();
    }

    private IntersectionGraph createSingleton() {
        Vertex vertex = new Vertex();
        HashMap<Vertex, HashSet<Vertex>> correspondence = new HashMap<>();
        correspondence.put(vertex, new HashSet<>(Set.of(CORRESPONDENCE_VERTICES.get(4))));
        return new IntersectionGraph(List.of(vertex), correspondence);
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

        return new IntersectionGraph(List.of(vertex1, vertex2), correspondence);
    }

    private IntersectionGraph createTriangleGraph() {
        List<Vertex> vertices = new ArrayList<>();
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
        List<Vertex> vertices = new ArrayList<>();
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