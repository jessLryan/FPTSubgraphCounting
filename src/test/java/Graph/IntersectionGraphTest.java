package Graph;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntersectionGraphTest {

    private final List<Vertex> HOST_VERTICES = createVertices(3);
    private final IntersectionGraph SMALLER_GRAPH = createSmallerGraph();
    private final IntersectionGraph LARGER_GRAPH = createLargerGraph();

    @Test
    void testVertexCorrespondence() {
        Vertex patternVertex = SMALLER_GRAPH.vertices.getFirst();
        Vertex hostVertex = HOST_VERTICES.getFirst();
        Set<Vertex> correspondenceSet = Set.of(hostVertex);
        assertEquals(SMALLER_GRAPH.vertexCorrespondence(patternVertex),correspondenceSet);
    }

    @Test
    void testEqualsSameObjectTrue() {
        assertEquals(SMALLER_GRAPH,SMALLER_GRAPH);
    }

    @Test
    void testEqualsDifferentObjectsButIdenticalTrue() {
        IntersectionGraph smallerGraphCopy = createSmallerGraph();
        assertEquals(SMALLER_GRAPH,smallerGraphCopy);
    }


    @Test
    void testEqualsIdenticalButDifferentCorrespondenceFalse() {
        IntersectionGraph smallerGraphCopy = createSmallerGraph();
        Vertex vertexToChange = smallerGraphCopy.vertices.getFirst();
        smallerGraphCopy.vertexCorrespondence(vertexToChange).clear();
        assertNotEquals(SMALLER_GRAPH, smallerGraphCopy);
    }

    @Test
    void testEqualsIdenticalButMissingEdgeFalse() {
        IntersectionGraph smallerGraphCopy = createSmallerGraph();
        Vertex firstPatternVertexCopy = smallerGraphCopy.vertices.getFirst();
        Vertex lastPatternVertexCopy = smallerGraphCopy.vertices.getLast();
        firstPatternVertexCopy.neighbours().clear();
        lastPatternVertexCopy.neighbours().clear();
        assertNotEquals(SMALLER_GRAPH, smallerGraphCopy);
    }

    @Test
    void testEqualsDifferentSizesFalse() {
        assertNotEquals(SMALLER_GRAPH, LARGER_GRAPH);
    }

    @Test
    void testHashCodeSameObject() {
        assert SMALLER_GRAPH.hashCode() == SMALLER_GRAPH.hashCode();
    }

    @Test
    void testHashCodeIdenticalGraph() {
        IntersectionGraph smallerGraphCopy = createSmallerGraph();
        assert SMALLER_GRAPH.hashCode() == smallerGraphCopy.hashCode();
    }

    @Test
    void testHashCodeDifferentSizedGraphs() {
        assert !(LARGER_GRAPH.hashCode() == SMALLER_GRAPH.hashCode());
    }

    private IntersectionGraph createSmallerGraph() {
        ArrayList<Vertex> patternVertices = createVertices(2);
        Vertex firstPatternVertex = patternVertices.getFirst();
        Vertex lastPatternVertex = patternVertices.getLast();
        firstPatternVertex.addNeighbour(lastPatternVertex);
        lastPatternVertex.addNeighbour(firstPatternVertex);

        HashMap<Vertex, HashSet<Vertex>> correspondence = new HashMap<>();
        correspondence.put(firstPatternVertex, new HashSet<>(Set.of(HOST_VERTICES.getFirst())));
        correspondence.put(lastPatternVertex, new HashSet<>(Set.of(HOST_VERTICES.getLast(), HOST_VERTICES.get(1))));

        return new IntersectionGraph(patternVertices, correspondence);
    }

    private IntersectionGraph createLargerGraph() {
        ArrayList<Vertex> patternVertices = createVertices(3);

        HashMap<Vertex, HashSet<Vertex>> correspondence = new HashMap<>();
        correspondence.put(patternVertices.getFirst(), new HashSet<>(Set.of(HOST_VERTICES.getFirst())));
        correspondence.put(patternVertices.get(1), new HashSet<>(Set.of(HOST_VERTICES.get(1))));
        correspondence.put(patternVertices.getLast(), new HashSet<>(Set.of(HOST_VERTICES.getLast())));

        return new IntersectionGraph(patternVertices, correspondence);
    }

    private ArrayList<Vertex> createVertices(int numVertices) {
        return new ArrayList<>(IntStream.range(0, numVertices).mapToObj(i -> new Vertex()).toList());
    }
}