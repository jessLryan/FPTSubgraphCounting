package Algorithm;

import Graph.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public class IntersectionSetFactory {

    public static SortedSet<IntersectionSet> createIntersectionSetsOfGraphs(List<IntersectionGraph> graphs) {
        IntersectionSet initialIntersectionSet = new IntersectionSet(graphs);
        getLargestContainedIntersectionSets(initialIntersectionSet);
        return initialIntersectionSet.getContainedIntersectionSets();
    }

    //the largest contained intersection sets are the intersection sets which are
    //contained in this intersection set and which have size 1 less than the given intersection set
    private static void getLargestContainedIntersectionSets(IntersectionSet intersectionSet) {
        int size = intersectionSet.size();
        List<IntersectionGraph> graphs = intersectionSet.getGraphs();

        for (int graph1Index = 0; graph1Index < size - 1; graph1Index++) {
            for (int graph2Index = graph1Index + 1; graph2Index < size; graph2Index++) {
                IntersectionGraph graph1 = graphs.get(graph1Index);
                IntersectionGraph graph2 = graphs.get(graph2Index);

                IntersectionGraphUtils utils = new IntersectionGraphUtils(graph1, graph2);
                List<IntersectionGraph> mergedGraphs = utils.createMergedGraphs();
                for (IntersectionGraph mergedGraph : mergedGraphs) {
                    List<IntersectionGraph> graphsCopy = new ArrayList<>(graphs);
                    graphsCopy.remove(graph1);
                    graphsCopy.remove(graph2);
                    graphsCopy.add(mergedGraph);
                    IntersectionSet containedIntersectionSet = new IntersectionSet(graphsCopy);
                    getLargestContainedIntersectionSets(containedIntersectionSet);

                    intersectionSet.addContainedIntersectionSet(containedIntersectionSet);
                    intersectionSet.addContainedIntersectionSets(containedIntersectionSet.getContainedIntersectionSets());
                }
            }
        }
    }

}
