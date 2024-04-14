package graph;

import java.util.ArrayList;

public class Vertex {

    private final ArrayList<Vertex> neighbours;

    public Vertex() {
        this.neighbours = new ArrayList<>();
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

    public ArrayList<Vertex> neighbours() {
        return neighbours;
    }

}
