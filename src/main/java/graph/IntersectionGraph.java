package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class IntersectionGraph extends Graph {
    private final HashMap<Vertex, HashSet<Vertex>> correspondence;

    public IntersectionGraph(ArrayList<Vertex> vertices, HashMap<Vertex, HashSet<Vertex>> correspondence) {
        super(vertices);
        this.correspondence = correspondence;
    }

    public HashMap<Vertex, HashSet<Vertex>> getCorrespondence() {
        return correspondence;
    }

    //two intersection graphs are equal if they are isomorphic
    //and the correspondence of each pair of 'equivalent' vertices
    //is identical
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntersectionGraph otherGraph = (IntersectionGraph) o;
        if (order() != otherGraph.order()) {
            return false;
        }
        //vertex lists contains possible pairings of vertices
        VertexLists vertexLists = new VertexLists(otherGraph, this);
        if (!vertexLists.isFeasible()) {
            return false;
        }

        return isIsomorphic(0, vertexLists, otherGraph);
    }

    //it is assumed that we have already checked the graphs have the same
    //order
    private boolean isIsomorphic(int currentVertexIndex, VertexLists vertexLists, IntersectionGraph otherGraph) {
        //all vertices have been mapped
        if (currentVertexIndex == vertices.size()) {
            return true;
        }
        boolean isIsomorphic = false;

        if (vertexLists.isFeasible()) {
            Vertex vertexGraph1 = vertices.get(currentVertexIndex);
            for (Vertex vertexGraph2 : vertexLists.getListOfVertex(vertexGraph1)) {
                if (vertexCorrespondence(vertexGraph1).equals(otherGraph.vertexCorrespondence(vertexGraph2))) {
                    VertexLists vertexListsCopy = vertexLists.deepCopy();
                    vertexListsCopy.assignVertex(vertexGraph1, vertexGraph2);
                    isIsomorphic = isIsomorphic || isIsomorphic(currentVertexIndex + 1, vertexListsCopy, otherGraph);
                }
            }
        }
        return isIsomorphic;
    }

    public HashSet<Vertex> vertexCorrespondence(Vertex key) {
        return correspondence.get(key);
    }

    @Override
    public int hashCode() {
        return 7 * maxDeg() + 29 * order();
    }
}
