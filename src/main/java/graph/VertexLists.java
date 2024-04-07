package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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

    //we only add vertices to a list if vertex degree is high enough
    private VertexLists(ArrayList<Vertex> keys, ArrayList<Vertex> values) {
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

    //returns true if there exists at least one mapping of the vertices
    //in keys to the vertices in their lists
    public boolean isFeasible() {
        return (atLeastOneListIsNonEmpty() && hasNoVertexWithEmptyList() && hasEnoughUniqueValues());
    }

    public boolean atLeastOneListIsNonEmpty() {
        return map.values().stream().anyMatch(list -> !list.isEmpty());
    }

    public boolean hasNoVertexWithEmptyList() {
        return map.entrySet().stream().noneMatch(entry -> entry.getValue().isEmpty());
    }

    public boolean hasEnoughUniqueValues() {
        Set<Vertex> uniqueVertices = map.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
        return uniqueVertices.size() >= map.keySet().size();
    }

    public VertexLists deepCopy() {
        HashMap<Vertex, ArrayList<Vertex>> mapCopy = new HashMap<>(map.size());
        for (Vertex key : map.keySet()) {
            mapCopy.put(key, new ArrayList<>(map.get(key)));
        }
        return new VertexLists(mapCopy);
    }

    public void assignVertex(Vertex key, Vertex value) {
        List<Vertex> valueNeighbours = value.neighbours();
        map.remove(key);
        //update neighbours of key so that their lists contain
        //only neighbours of value
        for (Map.Entry<Vertex, ArrayList<Vertex>> entry : map.entrySet()) {
            if (entry.getKey().isAdjacentTo(key)) {
                entry.getValue().retainAll(valueNeighbours);
            } else {
                //remove value from every list
                entry.getValue().remove(value);
            }
        }
    }

    public void updateListsToIncludeOnly(List<Vertex> keys, List<Vertex> valuesToInclude) {
        for (Vertex key : keys) {
            map.get(key).retainAll(valuesToInclude);
        }
    }

}
