package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class Graph {
    //vertices ordered in descending order of degree
    protected ArrayList<Vertex> vertices;

    public Graph(ArrayList<Vertex> vertexList) {
        vertexList.sort((o1, o2) -> Integer.compare(o2.degree(), o1.degree()));
        this.vertices = vertexList;
    }

    //note that the following method will throw exception if the graph
    //is not connected
    public void orderVerticesSoEachIsPrecededByNeighbour() {
        ArrayList<Vertex> verticesCopy = new ArrayList<>(vertices.size());
        Vertex firstVertex = vertices.removeFirst();
        verticesCopy.add(firstVertex);
        int numVerticesLeftToAdd = vertices.size();
        for (int i = 0; i < numVerticesLeftToAdd; i++) {
            Vertex vertex = getNextVertexInSequence(verticesCopy);
            verticesCopy.add(vertex);
            vertices.remove(vertex);
        }
        vertices = verticesCopy;
    }

    private Vertex getNextVertexInSequence(List<Vertex> verticesCopy) {
        for (Vertex nextVertex : vertices) {
            for (Vertex possibleNeighbour : verticesCopy) {
                if (nextVertex.isAdjacentTo(possibleNeighbour)) {
                    return nextVertex;
                }
            }
        }
        throw new IllegalArgumentException("vertex ordering not possible");
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public int order() {
        return vertices.size();
    }

    public int edgeCount() {
        return vertices.stream().mapToInt(vertex -> vertex.degree()).sum() / 2;
    }

    public int maxDeg() {
        int maxDeg = 0;
        for (Vertex vertex : vertices) {
            maxDeg = Math.max(maxDeg, vertex.degree());
        }
        return maxDeg;
    }

    //getListOfVertex k highest degree vertices in graph
    public List<Vertex> getHighestDegVertices(int k) {
        return vertices.subList(0, k);
    }

    //return max degree of graph after k highest vertices are removed
    public int maxDegRemaining(int k) {
        if (k == 0) {
            return maxDeg();
        }
        //get vertices after k highest degree vertices removed
        List<Vertex> remainingVertices = vertices.subList(k, order());
        int remainingDeg = 0;
        //work out new degree of remaining vertices
        for (Vertex remainingVertex : remainingVertices) {
            List<Vertex> remainingNeighbours = remainingVertex.neighbours().stream().filter(remainingVertices::contains).toList();
            remainingDeg = Math.max(remainingDeg, remainingNeighbours.size());
        }
        return remainingDeg;
    }

    public ArrayList<IntersectionGraph> getConnectedComponentsWithoutVertices(List<Vertex> verticesToExclude) {
        List<Vertex> remainingVertices = vertices.stream().filter(v -> !verticesToExclude.contains(v)).toList();
        //vertices in returned graphs must be new objects
        List<Vertex> remainingVerticesCopy = createCopyOfVertices(remainingVertices);
        Map<Vertex, HashSet<Vertex>> correspondence = createCorrespondenceBetween(remainingVertices, remainingVerticesCopy);

        ArrayList<IntersectionGraph> components = new ArrayList<>();
        while (!remainingVerticesCopy.isEmpty()) {
            Vertex vertex = remainingVerticesCopy.getFirst();
            HashSet<Vertex> componentVertices = new HashSet<>();
            getReachableVertices(vertex, componentVertices);

            ArrayList<Vertex> componentVertexList = new ArrayList<>(componentVertices.stream().toList());
            HashMap<Vertex, HashSet<Vertex>> componentCorrespondence = new HashMap<>(componentVertexList.size());
            for (Vertex componentVertex : componentVertexList) {
                componentCorrespondence.put(componentVertex, correspondence.get(componentVertex));
            }
            components.add(new IntersectionGraph(componentVertexList, componentCorrespondence));
            remainingVerticesCopy.removeAll(componentVertexList);
        }

        return components;
    }

    private void getReachableVertices(Vertex currentVertex, HashSet<Vertex> seen) {
        seen.add(currentVertex);
        for (Vertex neighbour : currentVertex.neighbours()) {
            if (!seen.contains(neighbour)) {
                getReachableVertices(neighbour, seen);
            }
        }
    }

    private HashMap<Vertex, HashSet<Vertex>> createCorrespondenceBetween(List<Vertex> oldList, List<Vertex> newList) {
        HashMap<Vertex, HashSet<Vertex>> correspondence = new HashMap<>();
        for (int vertexIndex = 0; vertexIndex < oldList.size(); vertexIndex++) {
            Vertex originalVertex = oldList.get(vertexIndex);
            Vertex copyVertex = newList.get(vertexIndex);
            correspondence.put(copyVertex, new HashSet<>(Set.of(originalVertex)));
        }
        return correspondence;
    }

    private List<Vertex> createCopyOfVertices(List<Vertex> verticesToCopy) {
        List<Vertex> copy = createVertices(verticesToCopy.size());
        for (Vertex vertexToCopy : verticesToCopy) {
            int vertexToCopyIndex = verticesToCopy.indexOf(vertexToCopy);
            Vertex vertexCopy = copy.get(vertexToCopyIndex);

            for (Vertex neighbour : vertexToCopy.neighbours()) {
                int neighbourIndex = verticesToCopy.indexOf(neighbour);
                if (neighbourIndex > -1) {
                    Vertex neighbourCopy = copy.get(neighbourIndex);
                    vertexCopy.addNeighbour(neighbourCopy);
                    neighbourCopy.addNeighbour(vertexCopy);
                }
            }
        }
        return copy;
    }

    private List<Vertex> createVertices(int numVertices) {
        return new ArrayList<>(IntStream.range(0, numVertices).mapToObj(i -> new Vertex()).toList());
    }

}