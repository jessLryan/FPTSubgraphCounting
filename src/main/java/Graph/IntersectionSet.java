package Graph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntersectionSet {

    private final List<IntersectionGraph> graphs;
    private final int size;
    private final HashSet<IntersectionSet> containedIntersectionSets;

    public IntersectionSet(List<IntersectionGraph> graphs) {
        this.graphs = graphs;
        this.size = graphs.size();
        this.containedIntersectionSets = new HashSet<>();
    }

    public HashSet<IntersectionSet> getContainedIntersectionSets() {
        return containedIntersectionSets;
    }

    public List<IntersectionGraph> getGraphs() {
        return graphs;
    }

    public Stream<IntersectionGraph> graphStream() {
        return graphs.stream();
    }

    public void addContainedIntersectionSets(Set<IntersectionSet> contained) {
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
        boolean result = thisGraphs.equals(otherGraphs);
        return result;
    }

    @Override
    public int hashCode() {
        int vertices = 0;
        for (IntersectionGraph graph : graphs) {
            vertices += graph.order();
        }
        return 53 * (size) + 7 * vertices;
    }
}
