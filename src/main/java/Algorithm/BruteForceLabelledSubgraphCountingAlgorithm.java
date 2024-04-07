package Algorithm;

import Graph.Graph;
import Graph.Vertex;
import Graph.VertexLists;

import java.util.ArrayList;
import java.util.List;

public class BruteForceLabelledSubgraphCountingAlgorithm {

    public static int countLabelledCopiesWithLists(Graph pattern, VertexLists vertexLists) {
        ArrayList<Vertex> patternVertices = pattern.getVertices();
        //case: singleton
        if (patternVertices.size() == 1) {
            Vertex vertex = patternVertices.getFirst();
            return vertexLists.getListOfVertex(vertex).size();
        }

        return countEmbeddingsRecursive(patternVertices, vertexLists, 0);
    }

    private static int countEmbeddingsRecursive(List<Vertex> patternVertices, VertexLists vertexLists, int nextVertexIndex) {
        if (nextVertexIndex == patternVertices.size() - 1) {
            return vertexLists.getListOfVertex(patternVertices.get(nextVertexIndex)).size();
        }
        if (!vertexLists.isFeasible()) {
            return 0;
        }
        int count = 0;
        Vertex patternVertex = patternVertices.get(nextVertexIndex);
        for (Vertex hostVertex : vertexLists.getListOfVertex(patternVertex)) {
            VertexLists mapListCopy = vertexLists.deepCopy();
            mapListCopy.assignVertex(patternVertex, hostVertex);
            count += countEmbeddingsRecursive(patternVertices, mapListCopy, nextVertexIndex + 1);
        }
        return count;
    }

}
