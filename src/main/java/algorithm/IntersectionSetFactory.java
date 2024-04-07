package algorithm;

import graph.IntersectionGraph;
import graph.IntersectionSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class IntersectionSetFactory {

    public static IntersectionSet createIntersectionSetOfGraphs(ArrayList<IntersectionGraph> graphs) {
        IntersectionSet initialIntersectionSet = new IntersectionSet(graphs);
        createContainedIntersectionSetsOf(initialIntersectionSet);
        return initialIntersectionSet;
    }

    //the following method recursively determines the set of contained intersection sets for this set
    //and of all of its contained intersection sets
    private static void createContainedIntersectionSetsOf(IntersectionSet intersectionSet) {
        int size = intersectionSet.size();
        ArrayList<IntersectionGraph> graphs = intersectionSet.getGraphs();
        //for each pair of graphs, get all contained intersection sets in which this
        // pair of graphs are merged together
        for (int graph1Index = 0; graph1Index < size - 1; graph1Index++) {
            for (int graph2Index = graph1Index + 1; graph2Index < size; graph2Index++) {
                intersectionSet.addContainedIntersectionSets(createContainedIntersectionSetsWithGraphPairMerged(graphs, graph1Index, graph2Index));
            }

        }
    }

    private static HashSet<IntersectionSet> createContainedIntersectionSetsWithGraphPairMerged(List<IntersectionGraph> graphs, int graph1Index, int graph2Index) {
        //note that we use a set because we may encounter the same contained
        //intersection set more than once
        HashSet<IntersectionSet> containedSets = new HashSet<>();

        IntersectionGraph graph1 = graphs.get(graph1Index);
        IntersectionGraph graph2 = graphs.get(graph2Index);
        IntersectionGraphMerger graphMerger = new IntersectionGraphMerger(graph1, graph2);
        List<IntersectionGraph> mergedGraphs = graphMerger.createMergedGraphs();

        for (IntersectionGraph mergedGraph : mergedGraphs) {
            ArrayList<IntersectionGraph> containedSetGraphs = new ArrayList<>(graphs);
            containedSetGraphs.remove(graph1);
            containedSetGraphs.remove(graph2);
            containedSetGraphs.add(mergedGraph);

            IntersectionSet containedIntersectionSet = new IntersectionSet(containedSetGraphs);
            //recurse to get all contained intersection sets of this set
            createContainedIntersectionSetsOf(containedIntersectionSet);
            containedSets.add(containedIntersectionSet);
            containedSets.addAll(containedIntersectionSet.getContainedIntersectionSets());
        }
        return containedSets;
    }

}
