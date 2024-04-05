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
        int count =0;
        //first get number of embeddings with vertices in verticesInSubset mapped
        //to high degree vertices in host
        VertexLists mapListsforcount = mapLists.deepCopy();
        mapListsforcount.updateListsToIncludeOnly(verticesToChooseFrom, nonHighDegVerticesG);
        if (mapListsforcount.isFeasible()) {
            count = mapSubsetToHighDegreeVertices(verticesInSubset, mapLists, verticesToChooseFrom, 0);
        }

        if (verticesInSubset.size() == numHighDegVerticesG) {
            return count;
        }

        for (Vertex vertexToAdd: verticesToChooseFrom) {
            List<Vertex> verticesInSubsetCopy = new ArrayList<>(verticesInSubset);
            verticesInSubsetCopy.add(vertexToAdd);

            List<Vertex> verticesToChooseFromCopy = new ArrayList<>(verticesToChooseFrom);
            verticesToChooseFromCopy.remove(vertexToAdd);

            VertexLists mapListsCopy = mapLists.deepCopy();
            mapListsCopy.updateListsToIncludeOnly(List.of(vertexToAdd), highDegVerticesG);

            if (mapListsCopy.isFeasible()) {
                count += runForEachSubset(mapListsCopy, verticesInSubsetCopy, verticesToChooseFromCopy);
            }
        }
        return count;
    }


    private int mapSubsetToHighDegreeVertices(List<Vertex> verticesInSubset, VertexLists mapLists, List<Vertex> verticesToChooseFrom, int currentVertexIndex) {
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
        int count = 0;

        List<IntersectionGraph> connectedComponents = pattern.getConnectedComponentsWithoutVertices(verticesInSubset);
        //runtime efficiency depends upon the vertices of the components
        //being ordered so that each vertex is preceded in the ordering
        //by at least one of its neighbours
        for (IntersectionGraph component : connectedComponents) {
            component.orderVerticesEachPrecededByNeighbour();
        }

        //intersection sets are ordered by size so that when we want to count non-overlapping copies
        //of an intersection set S in host, we know the number of (non-overlapping) copies of every
        //intersection set contained in S
        SortedSet<IntersectionSet> orderedIntersectionSets = IntersectionSetFactory.createIntersectionSetsOfGraphs(connectedComponents);

        for (IntersectionSet intersectionSet : orderedIntersectionSets) {
            count-=IntersectionSetCounter.countNonOverlappingCopiesOfIntersectionSet(intersectionSet, mapLists);
        }
        return count;
    }

}
