package Algorithm;

import Graph.IntersectionGraph;
import Graph.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IntersectionGraphUtils {
    private final IntersectionGraph graph1;
    private final IntersectionGraph graph2;

    public IntersectionGraphUtils(IntersectionGraph graph1, IntersectionGraph graph2) {
        //graph1 should have fewest vertices for efficiency
        if (graph1.order() < graph2.order()) {
            this.graph1 = graph1;
            this.graph2 = graph2;
        } else {
            this.graph1 = graph2;
            this.graph2 = graph1;
        }
    }

    //a merged graph of graph1 and graph2 contains (a copy of)
    //the vertices in both graphs, but at least one pair of vertices
    //one from each graph must correspond to the same vertex in the new graph
    public List<IntersectionGraph> createMergedGraphs() {
        Map<Vertex, Vertex> mergedVertexMap = new HashMap<>();
        return mergeVerticesRecursively(mergedVertexMap, graph1.getVertices(), graph2.getVertices());
    }

    private List<IntersectionGraph> mergeVerticesRecursively(Map<Vertex, Vertex> mergedVertexMap,
                                                             List<Vertex> unmergedVerticesGraph1,
                                                             List<Vertex> unmergedVerticesGraph2) {
        //if unmergedVerticesGraph1 empty, we are done - check at least one pair are merged
        //and construct merged graph from map
        if (unmergedVerticesGraph1.isEmpty() && !mergedVertexMap.values().isEmpty()) {
            return List.of(constructMergedGraphFromMap(mergedVertexMap));
        }

        List<IntersectionGraph> mergedGraphs = new ArrayList<>();
        //option 1: merge next vertex in graph1 to each possible
        // remaining vertex in graph2
        Vertex vertexGraph1 = unmergedVerticesGraph1.getFirst();
        for (Vertex vertexGraph2 : unmergedVerticesGraph2) {
            Map<Vertex, Vertex> mergedVertexMapCopy = new HashMap<>(mergedVertexMap);
            mergedVertexMapCopy.put(vertexGraph1, vertexGraph2);

            List<Vertex> unmergedVerticesGraph2Copy = new ArrayList<>(unmergedVerticesGraph2);
            unmergedVerticesGraph2Copy.remove(vertexGraph2);

            List<Vertex> unmergedVerticesGraph1Copy = unmergedVerticesGraph1.subList(1, unmergedVerticesGraph1.size());

            mergedGraphs.addAll(mergeVerticesRecursively(mergedVertexMapCopy, unmergedVerticesGraph1Copy, unmergedVerticesGraph2Copy));
        }

        //option 2: don't merge next vertex in graph1 to any vertex in graph2
        Map<Vertex, Vertex> mergeMapCopy = new HashMap<>(mergedVertexMap);
        List<Vertex> unmergedVerticesGraph1Copy = unmergedVerticesGraph1.subList(1, unmergedVerticesGraph1.size());
        mergedGraphs.addAll(mergeVerticesRecursively(mergeMapCopy, unmergedVerticesGraph1Copy, unmergedVerticesGraph2));

        return mergedGraphs;
    }


    private IntersectionGraph constructMergedGraphFromMap(Map<Vertex, Vertex> mergeMap) {
        List<Vertex> nonMergedVerticesGraph1 = graph1.getVertices().stream().filter(v -> !mergeMap.containsKey(v)).toList();
        List<Vertex> nonMergedVerticesGraph2 = graph2.getVertices().stream().filter(v -> !mergeMap.containsValue(v)).toList();
        Map<Vertex, HashSet<Vertex>> correspondenceGraph1 = graph1.getCorrespondence();
        Map<Vertex, HashSet<Vertex>> correspondenceGraph2 = graph2.getCorrespondence();
        HashMap<Vertex, HashSet<Vertex>> newCorrespondence = new HashMap<>();

        Map<Vertex, Vertex> newVertexMap = new HashMap<>();
        for (Vertex vertexGraph1 : nonMergedVerticesGraph1) {
            Vertex newVertex = new Vertex();
            newVertexMap.put(vertexGraph1, newVertex);
            newCorrespondence.put(newVertex, correspondenceGraph1.get(vertexGraph1));
        }
        for (Vertex vertexGraph2 : nonMergedVerticesGraph2) {
            Vertex newVertex = new Vertex();
            newVertexMap.put(vertexGraph2, newVertex);
            newCorrespondence.put(newVertex, correspondenceGraph2.get(vertexGraph2));
        }
        for (Map.Entry<Vertex, Vertex> entry : mergeMap.entrySet()) {
            Vertex newVertex = new Vertex();
            Vertex vertexGraph1 = entry.getKey();
            Vertex vertexGraph2 = entry.getValue();

            HashSet<Vertex> newCorrespondenceForVertex = new HashSet<>();
            newCorrespondenceForVertex.addAll(correspondenceGraph1.get(vertexGraph1));
            newCorrespondenceForVertex.addAll(correspondenceGraph2.get(vertexGraph2));
            newCorrespondence.put(newVertex, newCorrespondenceForVertex);

            newVertexMap.put(vertexGraph1, newVertex);
            newVertexMap.put(vertexGraph2, newVertex);
        }


        for (Map.Entry<Vertex, Vertex> entry : newVertexMap.entrySet()) {
            Vertex oldVertex = entry.getKey();
            Vertex newVertex = entry.getValue();
            for (Vertex oldNeighbour : oldVertex.neighbours()) {
                Vertex newNeighbour = newVertexMap.get(oldNeighbour);
                newNeighbour.addNeighbour(newVertex);
                newVertex.addNeighbour(newNeighbour);
            }
        }

        List<Vertex> newVertices = newVertexMap.values().stream().toList();
        return new IntersectionGraph(newVertices, newCorrespondence);
    }

}

