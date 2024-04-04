package Algorithm;

import Graph.Graph;
import Graph.Vertex;
import Graph.VertexLists;

import java.util.List;

public class BruteForceLabelledSubgraphCountingAlgorithm {

    public static int countLabelledCopiesWithLists(Graph pattern, VertexLists mapLists) {
        List<Vertex> patternVertices = pattern.getVertices();
        return countEmbeddingsRecursive(patternVertices, mapLists, 0);
    }

    private static int countEmbeddingsRecursive(List<Vertex> patternVertices, VertexLists mapLists, int nextVertexIndex) {
        if (nextVertexIndex == patternVertices.size() - 1) {
            return mapLists.getListOfVertex(patternVertices.get(nextVertexIndex)).size();
        }
        if (!mapLists.isFeasible()) {
            return 0;
        }
        int count = 0;
        Vertex nextVertex = patternVertices.get(nextVertexIndex);
        for (Vertex v_G : mapLists.getListOfVertex(nextVertex)) {
            VertexLists mapListCopy = mapLists.deepCopy();
            mapListCopy.assignVertex(nextVertex, v_G);
            count += countEmbeddingsRecursive(patternVertices, mapListCopy, nextVertexIndex + 1);
        }
        return count;
    }

}
