package Algorithm;

import Graph.IntersectionGraph;
import Graph.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class IntersectionGraphMerger {

    private final IntersectionGraph graph1;
    private final IntersectionGraph graph2;

    public IntersectionGraphMerger(IntersectionGraph graph1, IntersectionGraph graph2) {
        //graph1 should have fewest vertices for efficiency
        if (graph1.order() < graph2.order()) {
            this.graph1 = graph1;
            this.graph2 = graph2;
        } else {
            this.graph1 = graph2;
            this.graph2 = graph1;
        }
    }

    public IntersectionGraph getGraph1() {
        return graph1;
    }

    public IntersectionGraph getGraph2() {
        return graph2;
    }

    //a merged graph of graph1 and graph2 contains (a copy of)
    //the vertices in both graphs, but at least one pair of vertices
    //one from each graph must correspond to the same vertex in the new graph
    public List<IntersectionGraph> createMergedGraphs() {
        Map<Vertex, Vertex> mergedVertexMap = new HashMap<>();
        return mergeVerticesRecursively(mergedVertexMap, new ArrayList<>(), graph2.getVertices(), 0);
    }

    private List<IntersectionGraph> mergeVerticesRecursively(Map<Vertex, Vertex> mergedVertexMap, List<Vertex> unmergedVerticesGraph1, List<Vertex> unmergedVerticesGraph2, int nextVertexIndex) {
        //if unmergedVerticesGraph1 empty, we are done - check at least one pair are merged
        //and construct merged graph from map
        if (nextVertexIndex == graph1.order()) {
            //if no pairs of vertices are merged, don't create any graphs
            //from map
            if (mergedVertexMap.isEmpty()) {
                return List.of();
            } else {
                return List.of(constructMergedGraphFromMap(mergedVertexMap, unmergedVerticesGraph1, unmergedVerticesGraph2));
            }
        }

        List<IntersectionGraph> mergedGraphs = new ArrayList<>();
        //option 1: merge next vertex in graph1 to each possible
        // remaining vertex in graph2
        Vertex vertexGraph1 = graph1.getVertexAtIndex(nextVertexIndex);
        for (Vertex vertexGraph2 : unmergedVerticesGraph2) {
            Map<Vertex, Vertex> mergedVertexMapCopy = new HashMap<>(mergedVertexMap);
            mergedVertexMapCopy.put(vertexGraph1, vertexGraph2);

            List<Vertex> unmergedVerticesGraph2Copy = new ArrayList<>(unmergedVerticesGraph2);
            unmergedVerticesGraph2Copy.remove(vertexGraph2);

            mergedGraphs.addAll(mergeVerticesRecursively(mergedVertexMapCopy, unmergedVerticesGraph1, unmergedVerticesGraph2Copy, nextVertexIndex + 1));
        }

        //option 2: don't merge next vertex in graph1 to any vertex in graph2
        List<Vertex> unmergedVerticesGraph1Copy = new ArrayList<>(unmergedVerticesGraph1);
        unmergedVerticesGraph1Copy.add(vertexGraph1);
        mergedGraphs.addAll(mergeVerticesRecursively(mergedVertexMap, unmergedVerticesGraph1Copy, unmergedVerticesGraph2, nextVertexIndex + 1));

        return mergedGraphs;
    }


    private IntersectionGraph constructMergedGraphFromMap(Map<Vertex, Vertex> mergedVerticesMap, List<Vertex> unmergedVerticesGraph1, List<Vertex> unmergedVerticesGraph2) {

        Map<Vertex, HashSet<Vertex>> correspondenceGraph1 = graph1.getCorrespondence();
        Map<Vertex, HashSet<Vertex>> correspondenceGraph2 = graph2.getCorrespondence();
        HashMap<Vertex, HashSet<Vertex>> newCorrespondence = new HashMap<>();

        //map every old vertex (from graph 1 or graph 2) to a new
        //vertex for the new merged graph
        //also update the correpondence for each new vertex
        Map<Vertex, Vertex> oldToNewVertexMap = new HashMap<>();

        for (Vertex vertexGraph1 : unmergedVerticesGraph1) {
            Vertex newVertex = new Vertex();
            oldToNewVertexMap.put(vertexGraph1, newVertex);
            newCorrespondence.put(newVertex, correspondenceGraph1.get(vertexGraph1));
        }
        for (Vertex vertexGraph2 : unmergedVerticesGraph2) {
            Vertex newVertex = new Vertex();
            oldToNewVertexMap.put(vertexGraph2, newVertex);
            newCorrespondence.put(newVertex, correspondenceGraph2.get(vertexGraph2));
        }
        for (Map.Entry<Vertex, Vertex> entry : mergedVerticesMap.entrySet()) {
            Vertex newVertex = new Vertex();
            Vertex vertexGraph1 = entry.getKey();
            Vertex vertexGraph2 = entry.getValue();

            HashSet<Vertex> newCorrespondenceForVertex = new HashSet<>();
            newCorrespondenceForVertex.addAll(correspondenceGraph1.get(vertexGraph1));
            newCorrespondenceForVertex.addAll(correspondenceGraph2.get(vertexGraph2));
            newCorrespondence.put(newVertex, newCorrespondenceForVertex);

            oldToNewVertexMap.put(vertexGraph1, newVertex);
            oldToNewVertexMap.put(vertexGraph2, newVertex);
        }

        updateNewGraphNeighbours(oldToNewVertexMap);

        ArrayList<Vertex> newVertices = new ArrayList<>(oldToNewVertexMap.values().stream().distinct().toList());
        IntersectionGraph mergedGraph = new IntersectionGraph(newVertices, newCorrespondence);
        mergedGraph.orderVerticesEachPrecededByNeighbour();;
        return mergedGraph;
    }

    private void updateNewGraphNeighbours(Map<Vertex, Vertex> oldToNewVertexMap) {
        for (Map.Entry<Vertex, Vertex> entry : oldToNewVertexMap.entrySet()) {
            Vertex oldVertex = entry.getKey();
            Vertex newVertex = entry.getValue();
            for (Vertex oldNeighbour : oldVertex.neighbours()) {
                Vertex newNeighbour = oldToNewVertexMap.get(oldNeighbour);
                newNeighbour.addNeighbour(newVertex);
                newVertex.addNeighbour(newNeighbour);
            }
        }
    }

}

