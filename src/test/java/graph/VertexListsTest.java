package graph;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class VertexListsTest {

    private final VertexLists FEASIBLE_LIST = createFeasibleLists();

    private final VertexLists INFEASIBLE_LIST = createInfeasibleLists();

    @Test
    void testAtLeastOneListIsNonEmptyTrue() {
        assert (FEASIBLE_LIST.atLeastOneListIsNonEmpty());
    }

    @Test
    void testAtLeastOneListIsNonEmptyFalse() {
        HashMap<Vertex, ArrayList<Vertex>> map = new HashMap<>();
        Vertex vertex = new Vertex();
        map.put(vertex, new ArrayList<>());
        VertexLists lists = new VertexLists(map);
        assertFalse(lists.atLeastOneListIsNonEmpty());
    }

    @Test
    void testHasNoVertexWithEmptyListTrue() {
        assert (FEASIBLE_LIST.hasNoVertexWithEmptyList());
    }

    @Test
    void testHasNoVertexWithEmptyListFalse() {
        assertFalse(INFEASIBLE_LIST.hasNoVertexWithEmptyList());
    }

    @Test
    void testHasEnoughUniqueValuesTrue() {
        assert (FEASIBLE_LIST.hasEnoughUniqueValues());
    }

    @Test
    void testHasEnoughUniqueValuesFalse() {
        assertFalse(INFEASIBLE_LIST.hasEnoughUniqueValues());
    }

    @Test
    void testDeepCopy() {
        VertexLists listsCopy = FEASIBLE_LIST.deepCopy();
        Vertex vertex = FEASIBLE_LIST.getMap().keySet().stream().toList().getFirst();
        assert FEASIBLE_LIST.getListOfVertex(vertex) != listsCopy.getListOfVertex(vertex);
        assertEquals(FEASIBLE_LIST.getListOfVertex(vertex), listsCopy.getListOfVertex(vertex));
    }

    @Test
    void testAssignVertex() {
        Vertex key1 = new Vertex();
        Vertex key2 = new Vertex();
        Vertex key3 = new Vertex();
        key1.addNeighbour(key2);
        key2.addNeighbour(key1);

        Vertex value1 = new Vertex();
        Vertex value2 = new Vertex();
        Vertex value3 = new Vertex();
        value1.addNeighbour(value2);
        value2.addNeighbour(value1);

        HashMap<Vertex, ArrayList<Vertex>> map = new HashMap<>();
        map.put(key1, new ArrayList<>(List.of(value1, value2, value3)));
        map.put(key2, new ArrayList<>(List.of(value1, value2, value3)));
        map.put(key3, new ArrayList<>(List.of(value1, value2, value3)));
        VertexLists vertexLists = new VertexLists(map);

        vertexLists.assignVertex(key1, value1);
        assertEquals(vertexLists.getListOfVertex(key2), List.of(value2));
        assertEquals(vertexLists.getListOfVertex(key3), List.of(value2, value3));
    }

    @Test
    void testUpdateListsToIncludeOnly() {
        Vertex key1 = new Vertex();
        Vertex key2 = new Vertex();
        Vertex key3 = new Vertex();

        Vertex value1 = new Vertex();
        Vertex value2 = new Vertex();
        Vertex value3 = new Vertex();

        HashMap<Vertex, ArrayList<Vertex>> map = new HashMap<>();
        map.put(key1, new ArrayList<>(List.of(value1, value2, value3)));
        map.put(key2, new ArrayList<>(List.of(value1, value2, value3)));
        map.put(key3, new ArrayList<>(List.of(value1, value2, value3)));
        VertexLists vertexLists = new VertexLists(map);

        vertexLists.updateListsToIncludeOnly(List.of(key1, key2), List.of(value1));

        assertEquals(vertexLists.getListOfVertex(key1), List.of(value1));
        assertEquals(vertexLists.getListOfVertex(key2), List.of(value1));
        assertEquals(vertexLists.getListOfVertex(key3), List.of(value1, value2, value3));
    }

    @Test
    void testIsFeasibleTrue() {
        assert FEASIBLE_LIST.isFeasible();
    }

    @Test
    void testIsFeasibleFalse() {
        assertFalse(INFEASIBLE_LIST.isFeasible());
    }

    private VertexLists createFeasibleLists() {
        HashMap<Vertex, ArrayList<Vertex>> map = new HashMap<>();
        Vertex vertex1 = new Vertex();
        Vertex vertex2 = new Vertex();
        Vertex vertex3 = new Vertex();
        map.put(vertex1, new ArrayList<>(List.of(vertex2, vertex3)));
        map.put(vertex2, new ArrayList<>(List.of(vertex1, vertex3)));
        return new VertexLists(map);
    }

    private VertexLists createInfeasibleLists() {
        HashMap<Vertex, ArrayList<Vertex>> map = new HashMap<>();
        Vertex vertex1 = new Vertex();
        Vertex vertex2 = new Vertex();
        Vertex vertex3 = new Vertex();
        map.put(vertex1, new ArrayList<>(List.of(vertex1, vertex3)));
        map.put(vertex2, new ArrayList<>(List.of(vertex1, vertex3)));
        map.put(vertex3, new ArrayList<>(List.of()));
        return new VertexLists(map);
    }
}