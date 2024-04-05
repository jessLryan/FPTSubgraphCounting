package Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class Graph {
    //vertices ordered in descending order of degree
    protected List<Vertex> vertices;

    public Graph(ArrayList<Vertex> vertexList) {
        vertexList.sort((o1, o2) -> Integer.compare(o2.degree(), o1.degree()));
        this.vertices = vertexList;
    }

    //note that the following method will fail if the graph
    //is not connected
    public void orderVerticesEachPrecededByNeighbour() {
        List<Vertex> verticesCopy = new ArrayList<>();
        Vertex firstVertex = vertices.removeFirst();
        verticesCopy.add(firstVertex);
        int numVerticesLeftToAdd = vertices.size();
        for (int i=0; i<numVerticesLeftToAdd;i++) {
            Vertex vertex = getNextVertex(verticesCopy);
            verticesCopy.add(vertex);
            vertices.remove(vertex);
        }
        vertices = verticesCopy;
    }

    private Vertex getNextVertex(List<Vertex> verticesCopy) {
        for (Vertex nextVertex: vertices) {
            for (Vertex possibleNeighbour : verticesCopy) {
                if (nextVertex.isAdjacentTo(possibleNeighbour)) {
                    return nextVertex;
                }
            }
        }
        throw new IllegalArgumentException("vertex ordering not possible");
    }

    public Vertex getVertexAtIndex(int index) {
        return vertices.get(index);
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
        if (k==0) {
            return maxDeg();
        }
        List<Vertex> remainingVertices = vertices.subList(k,order());
        int remainingDeg = 0;
        for (Vertex remainingVertex: remainingVertices) {
            List<Vertex> remainingNeighbours = remainingVertex.neighbours().stream().filter(remainingVertices::contains).toList();
            remainingDeg = Math.max(remainingDeg, remainingNeighbours.size());
        }
        return remainingDeg;
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
            ArrayList<Vertex> componentVerticesList = new ArrayList<>(componentVertices.stream().toList());
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

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int i=0;i<order();i++) {
            Vertex vertex = vertices.get(i);
                string.append("vertex v_").append(i).append("\n");
            for (int j=i+1;j<order();j++) {
                Vertex neighbour = vertices.get(j);
                if (vertex.isAdjacentTo(neighbour)) {
                    string.append("v_").append(i).append(" adjacent to v_").append(j).append("\n");
                }
            }
        }
        return string.toString();
    }
}