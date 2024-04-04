package Graph;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IntersectionSet implements Comparable<IntersectionSet> {

    private final List<IntersectionGraph> graphs;
    private final int size;
    private final SortedSet<IntersectionSet> containedIntersectionSets;

    private int count;

    public IntersectionSet(List<IntersectionGraph> graphs) {
        this.graphs = graphs;
        this.size = graphs.size();
        this.containedIntersectionSets = new TreeSet<>();
    }

    public SortedSet<IntersectionSet> getContainedIntersectionSets() {
        return containedIntersectionSets;
    }

    public List<IntersectionGraph> getGraphs() {
        return graphs;
    }

    public Stream<IntersectionGraph> graphStream() {
        return graphs.stream();
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void addContainedIntersectionSet(IntersectionSet contained) {
        containedIntersectionSets.add(contained);
    }

    public void addContainedIntersectionSets(Set<IntersectionSet> contained) {
        containedIntersectionSets.addAll(contained);
    }

    public int size() {
        return graphs.size();
    }


    @Override
    public int compareTo(IntersectionSet o) {
        return Integer.compare(size, o.size);
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
        int vertices = 0;
        for (IntersectionGraph graph : graphs) {
            vertices += graph.order();
        }
        return 53 * (size) + 7 * vertices;
    }
}
