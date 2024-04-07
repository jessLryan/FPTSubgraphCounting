package Algorithm;

import Graph.IntersectionSet;
import Graph.IntersectionGraph;
import Graph.Graph;
import Graph.VertexLists;
import Graph.Vertex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class LabelledSubgraphCountingAlgorithm {

    private final Graph host;
    private final Graph pattern;
    private final List<Vertex> highDegVerticesG;
    private final int numHighDegVerticesG;
    private final List<Vertex> nonHighDegVerticesG;

    public LabelledSubgraphCountingAlgorithm(Graph host, Graph pattern, List<Vertex> highDegVerticesG) {
        this.host = host;
        this.pattern = pattern;
        this.highDegVerticesG = highDegVerticesG;
        this.numHighDegVerticesG = highDegVerticesG.size();
        this.nonHighDegVerticesG = host.getVertices().stream().filter(v -> !highDegVerticesG.contains(v)).toList();
    }

    public int run() {
        if (pattern.order()==1) {
            return host.order();
        }
        VertexLists vertexLists = new VertexLists(host, pattern);
        if (!vertexLists.isFeasible()) {
            return 0;
        } else {
            return runForEachSubset(vertexLists, new ArrayList<>(), pattern.getVertices());
        }
    }

    private int runForEachSubset(VertexLists vertexLists,
                                 List<Vertex> verticesInSubset,
                                 List<Vertex> remainingPatternVertices) {
        int count =0;
        if (!vertexLists.isFeasible()) {
            return count;
        }
        //first get number of embeddings with pattern vertices in current
        //subset are mapped to high degree vertices in host
        VertexLists vertexListsforCount = vertexLists.deepCopy();
        List<Vertex> remainingVertices = pattern.getVertices().stream().filter(v -> !verticesInSubset.contains(v)).toList();
        vertexListsforCount.updateListsToIncludeOnly(remainingVertices, nonHighDegVerticesG);


        if (vertexListsforCount.isFeasible()) {
            int m = mapSubsetToHighDegreeVertices(verticesInSubset, vertexListsforCount, remainingPatternVertices, 0);
            count =m;
        }

        if (verticesInSubset.size() == numHighDegVerticesG) {
            return count;
        }

        for (Vertex vertexToAdd: remainingPatternVertices) {
            List<Vertex> verticesInSubsetCopy = new ArrayList<>(verticesInSubset);
            verticesInSubsetCopy.add(vertexToAdd);

            int index = remainingPatternVertices.indexOf(vertexToAdd);

            List<Vertex> verticesToChooseFromCopy = new ArrayList<>();
            if (remainingPatternVertices.size()>1) {
                verticesToChooseFromCopy = remainingPatternVertices.subList(index+1, remainingPatternVertices.size());
            }


            VertexLists mapListsCopy = vertexLists.deepCopy();
            mapListsCopy.updateListsToIncludeOnly(List.of(vertexToAdd), highDegVerticesG);

            if (mapListsCopy.isFeasible()) {
                count += runForEachSubset(mapListsCopy, verticesInSubsetCopy, verticesToChooseFromCopy);
            }
        }
        return count;
    }


    private int mapSubsetToHighDegreeVertices(List<Vertex> verticesInSubset, VertexLists mapLists, List<Vertex> verticesToChooseFrom, int currentVertexIndex) {
        //need to have updated maplists of all vertices not in subset
        //not just ones at end of list

        if (currentVertexIndex == verticesInSubset.size()) {
            return countComponents(verticesInSubset, mapLists);
        }

        int count = 0;
        Vertex currentVertex = verticesInSubset.get(currentVertexIndex);

        for (Vertex mapTo : mapLists.getListOfVertex(currentVertex)) {
            VertexLists mapListCopyCopy = mapLists.deepCopy();
            mapListCopyCopy.assignVertex(currentVertex, mapTo);
            if (mapListCopyCopy.isFeasible()) {
                count += mapSubsetToHighDegreeVertices(verticesInSubset, mapListCopyCopy,verticesToChooseFrom, currentVertexIndex+1);
            }
        }

        return count;
    }

    private int countComponents(List<Vertex> verticesInSubset, VertexLists mapLists) {
        List<Vertex> remainingVertices = pattern.getVertices().stream().filter(v -> !verticesInSubset.contains(v)).toList();
        if (remainingVertices.isEmpty()) {
            return 1;
        }
        if (remainingVertices.size()==1) {
            return mapLists.getListOfVertex(remainingVertices.getFirst()).size();
        }

        int count = 1;

        ArrayList<IntersectionGraph> connectedComponents = pattern.getConnectedComponentsWithoutVertices(verticesInSubset);
        //runtime efficiency depends upon the vertices of the components
        //being ordered so that each vertex is preceded in the ordering
        //by at least one of its neighbours
        for (IntersectionGraph component : connectedComponents) {
            component.orderVerticesSoEachIsPrecededByNeighbour();
        }

        //intersection sets are ordered by size so that when we want to count non-overlapping copies
        //of an intersection set S in host, we know the number of (non-overlapping) copies of every
        //intersection set contained in S
        IntersectionSet intersectionSetOfGraphs = IntersectionSetFactory.createIntersectionSetOfGraphs(connectedComponents);
        HashSet<IntersectionSet> containedIntersectionSets = intersectionSetOfGraphs.getContainedIntersectionSets();

        ArrayList<IntersectionSet> containedSetsOrdered = new ArrayList<>(containedIntersectionSets.stream().toList());
        containedSetsOrdered.add(intersectionSetOfGraphs);
        containedSetsOrdered.sort(Comparator.comparingInt((IntersectionSet o) -> o.size()));
        IntersectionSetCounter intersectionSetCounter = new IntersectionSetCounter(containedSetsOrdered, mapLists);
        return intersectionSetCounter.getCountOfSet(intersectionSetOfGraphs);
    }

}
