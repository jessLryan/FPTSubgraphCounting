package Algorithm;

import Graph.VertexLists;
import Graph.IntersectionSet;
import Graph.IntersectionGraph;
import Graph.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class IntersectionSetCounter {
    HashMap<IntersectionSet, Integer> countMap;
    VertexLists mapLists;

    public IntersectionSetCounter(ArrayList<IntersectionSet> intersectionSets, VertexLists vmapLists) {
        countMap = new HashMap<>();
        mapLists = vmapLists;
        for (IntersectionSet intersectionSet : intersectionSets) {
            countMap.put(intersectionSet, countNonOverlappingCopiesOfIntersectionSet(intersectionSet));
        }
    }

    public int getCountOfSet(IntersectionSet intersectionSet) {
        return countMap.get(intersectionSet);
    }

    public int countNonOverlappingCopiesOfIntersectionSet(IntersectionSet intersectionSet) {
        int totalCount = countAllCopiesOfIntersectionSet(intersectionSet, mapLists);
        for (IntersectionSet containedSet : intersectionSet.getContainedIntersectionSets()) {
            if (!countMap.containsKey(containedSet)) {
                System.out.println("doesn't contain key with size "+containedSet.size());
            }
            totalCount -= countMap.get(containedSet);
        }
        return totalCount;
    }

    private int countAllCopiesOfIntersectionSet(IntersectionSet intersectionSet, VertexLists mapLists) {
        int count = 1;
        for (IntersectionGraph graph : intersectionSet.getGraphs()) {
            VertexLists graphMapList = createVertexListsForGraph(graph, mapLists);
            int graphCount = BruteForceLabelledSubgraphCountingAlgorithm.countLabelledCopiesWithLists(graph, graphMapList);
            if (graphCount == 0) {
                return 0;
            }
            count *= graphCount;
        }
        return count;
    }

    private static VertexLists createVertexListsForGraph(IntersectionGraph graph, VertexLists map) {
        Map<Vertex, HashSet<Vertex>> correspondence = graph.getCorrespondence();
        HashMap<Vertex, ArrayList<Vertex>> newmap = new HashMap<>();
        for (Vertex vertex : graph.getVertices()) {
            ArrayList<Vertex> listForVertex = new ArrayList<>();
            if (correspondence.get(vertex).isEmpty()) {
                newmap.put(vertex, listForVertex);
            }
            else {
                List<Vertex> corr = correspondence.get(vertex).stream().toList();
                listForVertex.addAll(map.getListOfVertex(corr.getFirst()));
                for (int i=1;i< corr.size();i++) {
                    listForVertex.retainAll(map.getListOfVertex(corr.get(i)));
                }
            }
            newmap.put(vertex, listForVertex);
        }
        return new VertexLists(newmap);
    }
}
