package Algorithm;

import Graph.IntersectionGraph;
import Graph.IntersectionSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class IntersectionSetFactory {

    public static IntersectionSet createIntersectionSetOfGraphs(ArrayList<IntersectionGraph> graphs) {
        IntersectionSet initialIntersectionSet = new IntersectionSet(graphs);
        createContainedIntersectionSetsOf(initialIntersectionSet);
        return initialIntersectionSet;
    }

    private static void createContainedIntersectionSetsOf(IntersectionSet intersectionSet) {
        int size = intersectionSet.size();
        ArrayList<IntersectionGraph> graphs = intersectionSet.getGraphs();
        for (int graph1Index = 0; graph1Index < size - 1; graph1Index++) {
            for (int graph2Index = graph1Index + 1; graph2Index < size; graph2Index++) {
                //get all contained intersection sets in which this
                // pair of graphs are merged together
                intersectionSet.addContainedIntersectionSets(createContainedIntersectionSetsWithGraphPairMerged(graphs, graph1Index, graph2Index));
            }

        }
    }

    private static HashSet<IntersectionSet> createContainedIntersectionSetsWithGraphPairMerged(List<IntersectionGraph> graphs, int graph1Index, int graph2Index) {
        HashSet<IntersectionSet> containedSets = new HashSet<>();

        IntersectionGraph graph1 = graphs.get(graph1Index);
        IntersectionGraph graph2 = graphs.get(graph2Index);
        IntersectionGraphMerger graphMerger = new IntersectionGraphMerger(graph1, graph2);
        List<IntersectionGraph> mergedGraphs = graphMerger.createMergedGraphs();

        for (IntersectionGraph mergedGraph : mergedGraphs) {
            ArrayList<IntersectionGraph> graphsCopy = new ArrayList<>(graphs);
            graphsCopy.remove(graph1);
            graphsCopy.remove(graph2);
            graphsCopy.add(mergedGraph);
            IntersectionSet containedIntersectionSet = new IntersectionSet(graphsCopy);
            createContainedIntersectionSetsOf(containedIntersectionSet);
            if (containedSets.contains(containedIntersectionSet)) {
                System.out.println("was already there");
            }
            else {
                containedSets.add(containedIntersectionSet);
            }
            containedSets.addAll(containedIntersectionSet.getContainedIntersectionSets());
        }
        return containedSets;
    }

}
