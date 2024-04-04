package Algorithm;

import Graph.VertexLists;
import Graph.IntersectionSet;
import Graph.IntersectionGraph;
import Graph.Vertex;
import Algorithm.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class IntersectionSetCounter {

    public static int countNonOverlappingCopiesOfIntersectionSet(IntersectionSet intersectionSet, VertexLists mapLists) {
        int totalCount = countAllCopiesOfIntersectionSet(intersectionSet, mapLists);
        for (IntersectionSet containedSet : intersectionSet.getContainedIntersectionSets()) {
            totalCount -= containedSet.getCount();
        }
        intersectionSet.setCount(totalCount);
        return totalCount;
    }

    private static int countAllCopiesOfIntersectionSet(IntersectionSet intersectionSet, VertexLists mapLists) {
        int count = 1;
        for (IntersectionGraph graph : intersectionSet.getGraphs()) {
            VertexLists graphMapList = createMapListsForGraph(graph, mapLists);
            int graphCount = BruteForceLabelledSubgraphCountingAlgorithm.countLabelledCopiesWithLists(graph, graphMapList);
            if (graphCount == 0) {
                return 0;
            }
            count *= graphCount;
        }
        return count;
    }

    private static VertexLists createMapListsForGraph(IntersectionGraph graph, VertexLists map) {
        Map<Vertex, HashSet<Vertex>> correspondence = graph.getCorrespondence();
        HashMap<Vertex, ArrayList<Vertex>> newmap = new HashMap<>();
        for (Vertex vertex : graph.getVertices()) {
            ArrayList<Vertex> listForVertex = new ArrayList<>();
            for (Vertex mapVertex : correspondence.get(vertex)) {
                if (listForVertex.isEmpty()) {
                    listForVertex.addAll(map.getListOfVertex(mapVertex));
                }
                else {
                    listForVertex.retainAll(map.getListOfVertex(mapVertex));
                }
            }
            newmap.put(vertex, listForVertex);
        }
        return new VertexLists(newmap);
    }
}
