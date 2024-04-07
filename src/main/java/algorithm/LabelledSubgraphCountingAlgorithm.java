package algorithm;

import graph.Graph;
import graph.IntersectionGraph;
import graph.IntersectionSet;
import graph.Vertex;
import graph.VertexLists;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class LabelledSubgraphCountingAlgorithm {

    private final Graph host;
    private final Graph pattern;
    private final List<Vertex> highDegVertices;
    private final int numHighDegVertices;
    private final List<Vertex> nonHighDegVertices;

    public LabelledSubgraphCountingAlgorithm(Graph host, Graph pattern, List<Vertex> highDegVertices) {
        this.host = host;
        this.pattern = pattern;
        this.highDegVertices = highDegVertices;
        this.numHighDegVertices = highDegVertices.size();
        this.nonHighDegVertices = host.getVertices().stream().filter(v -> !highDegVertices.contains(v)).toList();
    }

    public int run() {
        if (pattern.order() == 1) {
            return host.order();
        }

        VertexLists vertexLists = new VertexLists(host, pattern);
        if (!vertexLists.isFeasible()) {
            return 0;
        } else {
            return runForEachSubset(vertexLists, new ArrayList<>(), pattern.getVertices());
        }
    }

    private int runForEachSubset(VertexLists vertexLists, List<Vertex> verticesInSubset, List<Vertex> patternVerticesToChooseFrom) {
        int count = 0;
        if (!vertexLists.isFeasible()) {
            return count;
        }
        //first get number of embeddings with pattern vertices in current
        //subset are mapped to high degree vertices in host
        VertexLists vertexListsCopy = vertexLists.deepCopy();
        List<Vertex> remainingVertices = pattern.getVertices().stream().filter(v -> !verticesInSubset.contains(v)).toList();
        vertexListsCopy.updateListsToIncludeOnly(remainingVertices, nonHighDegVertices);
        if (vertexListsCopy.isFeasible()) {
            count = mapSubsetToHighDegreeVertices(verticesInSubset, vertexListsCopy, 0);
        }

        if (verticesInSubset.size() == numHighDegVertices) {
            return count;
        }

        //extend subset in each possible way (avoiding visiting same
        //subset twice)
        for (Vertex vertexToAdd : patternVerticesToChooseFrom) {
            List<Vertex> verticesInSubsetCopy = new ArrayList<>(verticesInSubset);
            verticesInSubsetCopy.add(vertexToAdd);

            int vertexToAddIndex = patternVerticesToChooseFrom.indexOf(vertexToAdd);

            List<Vertex> patternVerticesToChooseFromCopy = new ArrayList<>();
            if (patternVerticesToChooseFrom.size() > 1) {
                patternVerticesToChooseFromCopy = patternVerticesToChooseFrom.subList(vertexToAddIndex + 1, patternVerticesToChooseFrom.size());
            }

            VertexLists vertexListsNow = vertexLists.deepCopy();
            vertexListsNow.updateListsToIncludeOnly(List.of(vertexToAdd), highDegVertices);

            if (vertexListsNow.isFeasible()) {
                count += runForEachSubset(vertexListsNow, verticesInSubsetCopy, patternVerticesToChooseFromCopy);
            }
        }
        return count;
    }


    private int mapSubsetToHighDegreeVertices(List<Vertex> verticesInSubset, VertexLists mapLists, int nextVertexIndex) {
        if (nextVertexIndex == verticesInSubset.size()) {
            return countComponents(verticesInSubset, mapLists);
        }

        int count = 0;
        Vertex nextPatternVertex = verticesInSubset.get(nextVertexIndex);

        for (Vertex hostVertex : mapLists.getListOfVertex(nextPatternVertex)) {
            VertexLists mapListCopyCopy = mapLists.deepCopy();
            mapListCopyCopy.assignVertex(nextPatternVertex, hostVertex);
            if (mapListCopyCopy.isFeasible()) {
                count += mapSubsetToHighDegreeVertices(verticesInSubset, mapListCopyCopy, nextVertexIndex + 1);
            }
        }
        return count;
    }

    private int countComponents(List<Vertex> verticesInSubset, VertexLists mapLists) {
        List<Vertex> remainingVertices = pattern.getVertices().stream().filter(v -> !verticesInSubset.contains(v)).toList();
        if (remainingVertices.isEmpty()) {
            return 1;
        }
        //easy case: remaining vertices form a singleton
        if (remainingVertices.size() == 1) {
            return mapLists.getListOfVertex(remainingVertices.getFirst()).size();
        }

        ArrayList<IntersectionGraph> connectedComponents = pattern.getConnectedComponentsWithoutVertices(verticesInSubset);
        //runtime efficiency depends upon the vertices of the components
        //being ordered so that each vertex is preceded in the ordering
        //by at least one of its neighbours
        for (IntersectionGraph component : connectedComponents) {
            component.orderVerticesSoEachIsPrecededByNeighbour();
        }

        IntersectionSet intersectionSetOfGraphs = IntersectionSetFactory.createIntersectionSetOfGraphs(connectedComponents);

        HashSet<IntersectionSet> containedIntersectionSets = intersectionSetOfGraphs.getContainedIntersectionSets();

        //intersection sets are ordered by size so that when we want to count non-overlapping copies
        //of an intersection set S in host, we know the number of (non-overlapping) copies of every
        //intersection set contained in S
        ArrayList<IntersectionSet> containedSetsOrdered = new ArrayList<>(containedIntersectionSets.stream().toList());
        containedSetsOrdered.add(intersectionSetOfGraphs);
        containedSetsOrdered.sort(Comparator.comparingInt((IntersectionSet o) -> o.size()));

        IntersectionSetCounter intersectionSetCounter = new IntersectionSetCounter(containedSetsOrdered, mapLists);
        return intersectionSetCounter.getNonOverlappingCopiesOfSet(intersectionSetOfGraphs);
    }

}
