package Graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntersectionSet {

    private final ArrayList<IntersectionGraph> graphs;
    private final int size;
    private final HashSet<IntersectionSet> containedIntersectionSets;

    public IntersectionSet(ArrayList<IntersectionGraph> graphs) {
        this.graphs = graphs;
        this.size = graphs.size();
        this.containedIntersectionSets = new HashSet<>();
    }

    public HashSet<IntersectionSet> getContainedIntersectionSets() {
        return containedIntersectionSets;
    }

    public ArrayList<IntersectionGraph> getGraphs() {
        return graphs;
    }

    public Stream<IntersectionGraph> graphStream() {
        return graphs.stream();
    }

    public void addContainedIntersectionSets(HashSet<IntersectionSet> contained) {
        containedIntersectionSets.addAll(contained);
    }

    public int size() {
        return graphs.size();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        IntersectionSet otherIntersection = (IntersectionSet) other;
        if (otherIntersection.size != this.size) {
            return false;
        }
        Set<IntersectionGraph> thisGraphs = graphStream().collect(Collectors.toSet());
        Set<IntersectionGraph> otherGraphs = otherIntersection.graphStream().collect(Collectors.toSet());
        return thisGraphs.equals(otherGraphs);
    }

    @Override
    public int hashCode() {
        int totalVertices = 0;
        for (IntersectionGraph graph : graphs) {
            totalVertices += graph.order();
        }
        return 7 * size + 53 * totalVertices;
    }
}
