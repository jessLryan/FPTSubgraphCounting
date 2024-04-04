package Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class Graph {
    //vertices ordered in descending order of degree
    protected final List<Vertex> vertices;

    public Graph(List<Vertex> vertexList) {
        this.vertices = new ArrayList<>(vertexList);
        this.vertices.sort((o1, o2) -> Integer.compare(o2.degree(), o1.degree()));
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public int order() {
        return vertices.size();
    }

    public int edgeCount() {
        return vertices.stream().mapToInt(vertex -> vertex.degree()).sum()/2;
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
        return vertices.get(k).degree();
    }

    public List<IntersectionGraph> getConnectedComponentsWithoutVertices(List<Vertex> verticesToExclude) {
        List<Vertex> remainingVertices = vertices.stream().filter(v -> !verticesToExclude.contains(v)).toList();
        List<Vertex> remainingVerticesCopy = createCopyOfVertices(remainingVertices);
        Map<Vertex, HashSet<Vertex>> correspondence = createCorrespondenceBetween(remainingVertices, remainingVerticesCopy);

        List<IntersectionGraph> components = new ArrayList<>();
        while (!remainingVerticesCopy.isEmpty()) {
            Vertex vertex = remainingVerticesCopy.getFirst();
            Set<Vertex> componentVertices = new HashSet<>();
            getReachableVertices(vertex, componentVertices);
            List<Vertex> componentVerticesList = componentVertices.stream().toList();
            HashMap<Vertex, HashSet<Vertex>> componentCorrespondence = new HashMap<>();
            for (Vertex componentVertex : componentVerticesList) {
                componentCorrespondence.put(componentVertex, correspondence.get(componentVertex));
            }
            components.add(new IntersectionGraph(componentVerticesList, componentCorrespondence));
            remainingVerticesCopy.removeAll(componentVerticesList);
        }

        return components;
    }

    //Todo look into if we need synchronized
    private synchronized void getReachableVertices(Vertex currentVertex, Set<Vertex> seen) {
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

    private List<Vertex> createCopyOfVertices(List<Vertex> remainingVertices) {
        List<Vertex> remainingVerticesCopy = createVertices(remainingVertices.size());
        for (Vertex vertex : remainingVertices) {
            int vertexIndex = remainingVertices.indexOf(vertex);
            Vertex vertexCopy = remainingVerticesCopy.get(vertexIndex);
            for (Vertex neighbour : vertex.neighbours()) {
                int neighbourIndex = remainingVertices.indexOf(neighbour);
                if (neighbourIndex > -1) {
                    Vertex neighbourCopy = remainingVerticesCopy.get(neighbourIndex);
                    vertexCopy.addNeighbour(neighbourCopy);
                    neighbourCopy.addNeighbour(vertexCopy);
                }
            }
        }
        return remainingVerticesCopy;
    }

    private List<Vertex> createVertices(int numVertices) {
        return new ArrayList<>(IntStream.range(0, numVertices).mapToObj(i -> new Vertex()).toList());
    }

}