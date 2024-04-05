package Algorithm;

import Graph.IntersectionGraph;
import Graph.IntersectionSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class IntersectionSetFactory {

    public static IntersectionSet createIntersectionSetOfGraphs(List<IntersectionGraph> graphs) {
        IntersectionSet initialIntersectionSet = new IntersectionSet(graphs);
        createContainedIntersectionSetsOf(initialIntersectionSet);
        return initialIntersectionSet;
    }

    private static void createContainedIntersectionSetsOf(IntersectionSet intersectionSet) {
        int size = intersectionSet.size();
        List<IntersectionGraph> graphs = intersectionSet.getGraphs();
        for (int graph1Index = 0; graph1Index < size - 1; graph1Index++) {
            for (int graph2Index = graph1Index + 1; graph2Index < size; graph2Index++) {
                //get all contained intersection sets in which this
                // pair of graphs are merged together
                intersectionSet.addContainedIntersectionSets(createContainedIntersectionSetsWithGraphPairMerged(graphs, graph1Index, graph2Index));
            }

        }
    }

    private static Set<IntersectionSet> createContainedIntersectionSetsWithGraphPairMerged(List<IntersectionGraph> graphs, int graph1Index, int graph2Index) {
        Set<IntersectionSet> containedSets = new TreeSet<>();

        IntersectionGraph graph1 = graphs.get(graph1Index);
        IntersectionGraph graph2 = graphs.get(graph2Index);
        IntersectionGraphMerger graphMerger = new IntersectionGraphMerger(graph1, graph2);
        List<IntersectionGraph> mergedGraphs = graphMerger.createMergedGraphs();

        for (IntersectionGraph mergedGraph : mergedGraphs) {
            List<IntersectionGraph> graphsCopy = new ArrayList<>(graphs);
            graphsCopy.remove(graph1);
            graphsCopy.remove(graph2);
            graphsCopy.add(mergedGraph);
            IntersectionSet containedIntersectionSet = new IntersectionSet(graphsCopy);
            createContainedIntersectionSetsOf(containedIntersectionSet);

            containedSets.add(containedIntersectionSet);
            containedSets.addAll(containedIntersectionSet.getContainedIntersectionSets());
        }
        return containedSets;
    }

}
