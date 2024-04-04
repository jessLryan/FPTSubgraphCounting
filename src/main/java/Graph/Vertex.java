package Graph;

import java.util.ArrayList;
import java.util.List;

public class Vertex {

    private final List<Vertex> neighbours;

    public Vertex() {
        this.neighbours = new ArrayList<>();
    }

    public Vertex(List<Vertex> neighbours) {
        this.neighbours = neighbours;
    }

    public void addNeighbour(Vertex neighbour) {
        if (!neighbours.contains(neighbour)) {
            neighbours.add(neighbour);
        }
    }

    public boolean isAdjacentTo(Vertex vertex) {
        return neighbours.contains(vertex);
    }

    public int degree() {
        return neighbours.size();
    }

    public List<Vertex> neighbours() {
        return neighbours;
    }

}
