package Algorithm;

import Graph.IntersectionSet;
import Graph.IntersectionGraph;
import Graph.Graph;
import Graph.VertexLists;
import Graph.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
        VertexLists mapLists = new VertexLists(host, pattern);
        if (!mapLists.isFeasible()) {
            return 0;
        } else {
            return runForEachSubset(mapLists, new ArrayList<>(), highDegVerticesG);
        }
    }

    private int runForEachSubset(VertexLists mapLists,
                                 List<Vertex> verticesInSubset,
                                 List<Vertex> verticesToChooseFrom) {
        int count = mapSubsetToHighDegreeVertices(verticesInSubset, mapLists, 0);
        int numVerticesToChooseFrom = verticesToChooseFrom.size();
        if (numVerticesToChooseFrom==0||verticesInSubset.size() == numHighDegVerticesG) {
            return count;
        }

        for (int vertexToAddIndex = 0; vertexToAddIndex< numVerticesToChooseFrom; vertexToAddIndex++) {
            Vertex vertexToAdd  = verticesToChooseFrom.get(vertexToAddIndex);
            List<Vertex> verticesInSubsetCopy = new ArrayList<>(verticesInSubset);
            verticesInSubsetCopy.add(vertexToAdd);

            VertexLists mapListsCopy = mapLists.deepCopy();
            mapListsCopy.updateListsToIncludeOnly(List.of(vertexToAdd), highDegVerticesG);
            List<Vertex> verticesNotInSubset = pattern.getVertices().stream().filter(v -> !verticesInSubsetCopy.contains(v)).toList();
            mapListsCopy.updateListsToIncludeOnly(verticesNotInSubset, nonHighDegVerticesG);

            if (mapListsCopy.hasEnoughUniqueValues()) {
                List<Vertex> verticesToChooseFromCopy = verticesToChooseFrom.subList(vertexToAddIndex + 1, numVerticesToChooseFrom);
                count += runForEachSubset(mapListsCopy, verticesInSubsetCopy, verticesToChooseFromCopy);
            }
        }
        return count;
    }


    private int mapSubsetToHighDegreeVertices(List<Vertex> verticesInSubset, VertexLists mapLists, int currentVertexIndex) {
        if (currentVertexIndex == verticesInSubset.size()) {
            return countComponents(verticesInSubset, mapLists);
        }

        int count = 0;
        Vertex currentVertex = verticesInSubset.get(currentVertexIndex);

        for (Vertex mapTo : mapLists.getListOfVertex(currentVertex)) {
            VertexLists mapListCopy = mapLists.deepCopy();
            mapListCopy.assignVertex(currentVertex, mapTo);
            if (mapListCopy.hasNoVertexWithEmptyList() &&mapListCopy.hasEnoughUniqueValues()) {
                count += mapSubsetToHighDegreeVertices(verticesInSubset, mapListCopy, currentVertexIndex++);
            }
        }

        return count;
    }

    private int countComponents(List<Vertex> verticesInSubset, VertexLists mapLists) {
        int count = 0;

        List<IntersectionGraph> connectedComponents = pattern.getConnectedComponentsWithoutVertices(verticesInSubset);
        //intersection sets ordered by size so that when we want to count non-overlapping copies
        //of an intersection set S in host, we know the number of (non-overlapping) copies of every
        //intersection set contained in S
        SortedSet<IntersectionSet> orderedIntersectionSets = IntersectionSetFactory.createIntersectionSetsOfGraphs(connectedComponents);

        for (IntersectionSet intersectionSet : orderedIntersectionSets) {
            count-=IntersectionSetCounter.countNonOverlappingCopiesOfIntersectionSet(intersectionSet, mapLists);
        }
        return count;
    }

}
