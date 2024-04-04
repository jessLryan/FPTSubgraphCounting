package Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class VertexLists {

    private final HashMap<Vertex, ArrayList<Vertex>> map;

    public VertexLists(HashMap<Vertex, ArrayList<Vertex>> map) {
        this.map = map;
    }

    public VertexLists(Graph host, Graph pattern) {
        this(pattern.getVertices(), host.getVertices());
    }

    private VertexLists(List<Vertex> keys, List<Vertex> values) {
        map = new HashMap<>(keys.size());
        int numValues = values.size();
        for (Vertex key : keys) {
            ArrayList<Vertex> valuesList = new ArrayList<>(numValues);
            for (Vertex value : values) {
                if (value.degree() >= key.degree()) {
                    valuesList.add(value);
                }
            }
            map.put(key, valuesList);
        }
    }

    public Map<Vertex, ArrayList<Vertex>> getMap() {
        return map;
    }

    public ArrayList<Vertex> getListOfVertex(Vertex key) {
        return map.get(key);
    }

    public boolean atLeastOneListIsNonEmpty() {
        for (List<Vertex> list : map.values()) {
            if (!list.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasNoVertexWithEmptyList() {
        for (Map.Entry<Vertex, ArrayList<Vertex>> entry : map.entrySet()) {
            if (entry.getValue().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasEnoughUniqueValues() {
        Set<Vertex> uniqueVertices = map.keySet().stream().flatMap(vertex -> map.get(vertex).stream()).collect(Collectors.toCollection(HashSet::new));
        return uniqueVertices.size() >= map.keySet().size();
    }

    public VertexLists deepCopy() {
        HashMap<Vertex, ArrayList<Vertex>> mapCopy = new HashMap<>();
        for (Vertex key : map.keySet()) {
            mapCopy.put(key, new ArrayList<>(map.get(key)));
        }
        return new VertexLists(mapCopy);
    }

    public void assignVertex(Vertex key, Vertex value) {
        List<Vertex> valueNeighbours = value.neighbours();
        map.remove(key);
        for (Map.Entry<Vertex, ArrayList<Vertex>> entry : map.entrySet()) {
            if (entry.getKey().isAdjacentTo(key)) {
                entry.getValue().retainAll(valueNeighbours);
            }
            else {
                entry.getValue().remove(value);
            }
        }
    }

    public void updateListsToIncludeOnly(List<Vertex> keys, List<Vertex> values) {
        for (Vertex key : keys) {
            map.get(key).retainAll(values);
        }
    }

    public boolean isFeasible() {
        return (atLeastOneListIsNonEmpty() && hasNoVertexWithEmptyList() && hasEnoughUniqueValues());
    }
}
