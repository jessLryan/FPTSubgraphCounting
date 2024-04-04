package Graph;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class IntersectionSetTest {

    private final List<Vertex> HOST_VERTICES = createVertices(3);
    private final IntersectionGraph SMALLER_GRAPH = createSmallerGraph();
    private final IntersectionGraph LARGER_GRAPH = createLargerGraph();

    @Test
    void testCompareToSameSize() {
        List<IntersectionGraph> graphs1 = new ArrayList<>();
        graphs1.add(SMALLER_GRAPH);
        graphs1.add(LARGER_GRAPH);
        IntersectionSet intersectionSet1 = new IntersectionSet(graphs1);

        List<IntersectionGraph> graphs2 = new ArrayList<>();
        graphs2.add(LARGER_GRAPH);
        graphs2.add(SMALLER_GRAPH);
        IntersectionSet intersectionSet2 = new IntersectionSet(graphs2);

        assert intersectionSet1.compareTo(intersectionSet2)==0;
    }

    @Test
    void testCompareToDifferentSizes() {
        List<IntersectionGraph> graphs1 = new ArrayList<>();
        graphs1.add(SMALLER_GRAPH);
        graphs1.add(LARGER_GRAPH);
        graphs1.add(LARGER_GRAPH);
        IntersectionSet intersectionSet1 = new IntersectionSet(graphs1);

        List<IntersectionGraph> graphs2 = new ArrayList<>();
        graphs2.add(LARGER_GRAPH);
        graphs2.add(SMALLER_GRAPH);
        IntersectionSet intersectionSet2 = new IntersectionSet(graphs2);

        assert intersectionSet1.compareTo(intersectionSet2)==1;
        assert intersectionSet2.compareTo(intersectionSet1)==-1;
    }

    @Test
    void testEqualsSameGraphsSameOrderTrue() {
        List<IntersectionGraph> graphs1 = new ArrayList<>();
        graphs1.add(SMALLER_GRAPH);
        graphs1.add(LARGER_GRAPH);
        IntersectionSet intersectionSet1 = new IntersectionSet(graphs1);

        List<IntersectionGraph> graphs2 = new ArrayList<>();
        graphs2.add(SMALLER_GRAPH);
        graphs2.add(LARGER_GRAPH);
        IntersectionSet intersectionSet2 = new IntersectionSet(graphs2);

        assertEquals(intersectionSet1, intersectionSet2);
    }

    @Test
    void testEqualsSameGraphsDifferentOrderTrue() {
        List<IntersectionGraph> graphs1 = new ArrayList<>();
        graphs1.add(SMALLER_GRAPH);
        graphs1.add(LARGER_GRAPH);
        IntersectionSet intersectionSet1 = new IntersectionSet(graphs1);

        List<IntersectionGraph> graphs2 = new ArrayList<>();
        graphs2.add(LARGER_GRAPH);
        graphs2.add(SMALLER_GRAPH);
        IntersectionSet intersectionSet2 = new IntersectionSet(graphs2);

        assertEquals(intersectionSet1, intersectionSet2);
    }

    @Test
    void testEqualsSameGraphsDifferentObjectsTrue() {
        List<IntersectionGraph> graphs1 = new ArrayList<>();
        graphs1.add(SMALLER_GRAPH);
        graphs1.add(LARGER_GRAPH);
        IntersectionSet intersectionSet1 = new IntersectionSet(graphs1);

        List<IntersectionGraph> graphs2 = new ArrayList<>();
        graphs2.add(createLargerGraph());
        graphs2.add(createSmallerGraph());
        IntersectionSet intersectionSet2 = new IntersectionSet(graphs2);

        assertEquals(intersectionSet1, intersectionSet2);
    }

    @Test
    void testEqualsSameSizeDifferentGraphsFalse() {
        List<IntersectionGraph> graphs1 = new ArrayList<>();
        graphs1.add(SMALLER_GRAPH);
        graphs1.add(SMALLER_GRAPH);
        IntersectionSet intersectionSet1 = new IntersectionSet(graphs1);

        List<IntersectionGraph> graphs2 = new ArrayList<>();
        graphs2.add(LARGER_GRAPH);
        graphs2.add(SMALLER_GRAPH);
        IntersectionSet intersectionSet2 = new IntersectionSet(graphs2);

        assertNotEquals(intersectionSet1,intersectionSet2);
    }

    @Test
    void testEqualsDifferentSizeFalse() {
        List<IntersectionGraph> graphs1 = new ArrayList<>();
        graphs1.add(SMALLER_GRAPH);
        graphs1.add(SMALLER_GRAPH);
        IntersectionSet intersectionSet1 = new IntersectionSet(graphs1);

        List<IntersectionGraph> graphs2 = new ArrayList<>();
        graphs2.add(LARGER_GRAPH);
        graphs2.add(SMALLER_GRAPH);
        IntersectionSet intersectionSet2 = new IntersectionSet(graphs2);

        assertNotEquals(intersectionSet1,intersectionSet2);
    }

    @Test
    void testHashCodesSameObjectTrue() {
        List<IntersectionGraph> graphs = new ArrayList<>();
        graphs.add(SMALLER_GRAPH);
        graphs.add(LARGER_GRAPH);
        IntersectionSet intersectionSet = new IntersectionSet(graphs);

        assert intersectionSet.hashCode() == intersectionSet.hashCode();
    }

    @Test
    void testHashCodesSameDifferentObjectTrue() {
        List<IntersectionGraph> graphs1 = new ArrayList<>();
        graphs1.add(SMALLER_GRAPH);
        graphs1.add(LARGER_GRAPH);
        IntersectionSet intersectionSet1 = new IntersectionSet(graphs1);

        List<IntersectionGraph> graphs2 = new ArrayList<>();
        graphs2.add(SMALLER_GRAPH);
        graphs2.add(LARGER_GRAPH);
        IntersectionSet intersectionSet2 = new IntersectionSet(graphs2);

        assert intersectionSet1.hashCode() == intersectionSet2.hashCode();
    }

    @Test
    void testHashCodesDifferent() {
        List<IntersectionGraph> graphs1 = new ArrayList<>();
        graphs1.add(LARGER_GRAPH);
        graphs1.add(LARGER_GRAPH);
        IntersectionSet intersectionSet1 = new IntersectionSet(graphs1);

        List<IntersectionGraph> graphs2 = new ArrayList<>();
        graphs2.add(SMALLER_GRAPH);
        graphs2.add(SMALLER_GRAPH);
        IntersectionSet intersectionSet2 = new IntersectionSet(graphs2);

        assert intersectionSet1.hashCode() != intersectionSet2.hashCode();
    }

    private IntersectionGraph createSmallerGraph() {
        List<Vertex> patternVertices = createVertices(2);

        HashMap<Vertex, HashSet<Vertex>> correspondence = new HashMap<>();
        correspondence.put(patternVertices.getFirst(), new HashSet<>(Set.of(HOST_VERTICES.getFirst())));
        correspondence.put(patternVertices.getLast(), new HashSet<>(Set.of(HOST_VERTICES.getLast(), HOST_VERTICES.get(1))));

        return new IntersectionGraph(patternVertices, correspondence);
    }

    private IntersectionGraph createLargerGraph() {
        List<Vertex> patternVertices = createVertices(3);
        Vertex firstPatternVertex = patternVertices.getFirst();
        Vertex lastPatternVertex = patternVertices.getLast();
        firstPatternVertex.addNeighbour(lastPatternVertex);
        lastPatternVertex.addNeighbour(firstPatternVertex);

        HashMap<Vertex, HashSet<Vertex>> correspondence = new HashMap<>();
        correspondence.put(patternVertices.getFirst(), new HashSet<>(Set.of(HOST_VERTICES.getFirst())));
        correspondence.put(patternVertices.get(1), new HashSet<>(Set.of(HOST_VERTICES.get(1))));
        correspondence.put(patternVertices.getLast(), new HashSet<>(Set.of(HOST_VERTICES.getLast())));

        return new IntersectionGraph(patternVertices, correspondence);
    }

    private List<Vertex> createVertices(int numVertices) {
        return IntStream.range(0, numVertices).mapToObj(i -> new Vertex()).toList();
    }
}