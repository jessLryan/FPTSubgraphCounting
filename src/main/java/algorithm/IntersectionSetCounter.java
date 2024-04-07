package algorithm;

import graph.IntersectionGraph;
import graph.IntersectionSet;
import graph.Vertex;
import graph.VertexLists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class IntersectionSetCounter {
    private final HashMap<IntersectionSet, Integer> countMap;
    private final VertexLists mapLists;

    public IntersectionSetCounter(ArrayList<IntersectionSet> intersectionSets, VertexLists patternGraphVertexLists) {
        countMap = new HashMap<>();
        mapLists = patternGraphVertexLists;
        for (IntersectionSet intersectionSet : intersectionSets) {
            countMap.put(intersectionSet, countNonOverlappingCopiesOfIntersectionSet(intersectionSet));
        }
    }

    public int getNonOverlappingCopiesOfSet(IntersectionSet intersectionSet) {
        return countMap.get(intersectionSet);
    }

    public int countNonOverlappingCopiesOfIntersectionSet(IntersectionSet intersectionSet) {
        int totalCount = countAllCopiesOfIntersectionSet(intersectionSet);
        for (IntersectionSet containedSet : intersectionSet.getContainedIntersectionSets()) {
            totalCount -= countMap.get(containedSet);
        }
        return totalCount;
    }

    private int countAllCopiesOfIntersectionSet(IntersectionSet intersectionSet) {
        int count = 1;
        for (IntersectionGraph graph : intersectionSet.getGraphs()) {
            VertexLists graphMapList = createVertexListsForGraph(graph);
            int graphCount = BruteForceLabelledSubgraphCountingAlgorithm.countLabelledCopiesWithLists(graph, graphMapList);
            if (graphCount == 0) {
                return 0;
            }
            count *= graphCount;
        }
        return count;
    }

    private VertexLists createVertexListsForGraph(IntersectionGraph graph) {
        Map<Vertex, HashSet<Vertex>> correspondence = graph.getCorrespondence();
        HashMap<Vertex, ArrayList<Vertex>> newVertexLists = new HashMap<>();

        //list for new vertex is intersection of lists for its corresponding vertices
        for (Vertex vertex : graph.getVertices()) {
            List<Vertex> correspondenceOfVertex = correspondence.get(vertex).stream().toList();
            ArrayList<Vertex> listForVertex = new ArrayList<>(mapLists.getListOfVertex(correspondenceOfVertex.getFirst()));
            for (int i = 1; i < correspondenceOfVertex.size(); i++) {
                listForVertex.retainAll(mapLists.getListOfVertex(correspondenceOfVertex.get(i)));
            }

            newVertexLists.put(vertex, listForVertex);
        }
        return new VertexLists(newVertexLists);
    }
}
