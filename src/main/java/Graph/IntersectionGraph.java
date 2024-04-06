package Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class IntersectionGraph extends Graph {
    private final HashMap<Vertex, HashSet<Vertex>> correspondence;

    public IntersectionGraph(ArrayList<Vertex> vertices, HashMap<Vertex, HashSet<Vertex>> correspondence) {
        super(vertices);
        this.correspondence = correspondence;
    }

    public HashMap<Vertex, HashSet<Vertex>> getCorrespondence() {
        return correspondence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntersectionGraph otherGraph = (IntersectionGraph) o;
        if (order() != otherGraph.order()) {
            return false;
        }

        VertexLists vertexLists = new VertexLists(otherGraph, this);
        if (!vertexLists.isFeasible()) {
            return false;
        }
        boolean isIsomorphic = isIsomorphic(0, vertexLists, otherGraph);
        return isIsomorphic;
    }

    private boolean isIsomorphic(int currentVertexIndex, VertexLists vertexLists, IntersectionGraph otherGraph) {
        //all vertices have been mapped
        if (currentVertexIndex == vertices.size()) {
            return true;
        }
        boolean isIsomorphic = false;
        if (vertexLists.isFeasible()) {
            Vertex key = vertices.get(currentVertexIndex);
            for (Vertex value : vertexLists.getListOfVertex(key)) {
                if (vertexCorrespondence(key).equals(otherGraph.vertexCorrespondence(value))) {
                    VertexLists vertexListsCopy = vertexLists.deepCopy();
                    vertexListsCopy.assignVertex(key, value);
                    isIsomorphic = isIsomorphic || isIsomorphic(currentVertexIndex + 1, vertexListsCopy, otherGraph);
                }
            }
        }
        return isIsomorphic;
    }

    public Set<Vertex> vertexCorrespondence(Vertex key) {
        return correspondence.get(key);
    }

    @Override
    public int hashCode() {
        return 7 * maxDeg() + 29 * order();
    }
}
