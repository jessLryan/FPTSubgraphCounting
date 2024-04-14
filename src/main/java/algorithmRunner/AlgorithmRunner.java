package algorithmRunner;

import algorithm.LabelledSubgraphCountingAlgorithm;
import algorithm.ParameterValueOptimiser;
import graph.Graph;
import graph.Vertex;

import java.util.List;

public class AlgorithmRunner {

    public static void runAlgorithm(RunResult result, Graph hostGraph, Graph patternGraph) {

        long startTime = System.currentTimeMillis();
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    long endTime = System.currentTimeMillis();
                    result.setStatus(RunStatus.INTERRUPTED);
                    result.setTotalRuntimeInMilliseconds(endTime - startTime);
                    System.out.println(result);
                })
        );

        ParameterValueOptimiser parameterValueOptimiser = new ParameterValueOptimiser(patternGraph, hostGraph);
        int numHighDegVertices = parameterValueOptimiser.getNumHighDegVertices();
        long timeAfterParameterOptimisation = System.currentTimeMillis();
        result.setParameterOptimisationRuntimeInMilliseconds(timeAfterParameterOptimisation - startTime);
        result.setNumberOfHighDegreeVertices(numHighDegVertices);
        result.setMaximumDegreeOfRemainingVertices(parameterValueOptimiser.getMaxDegRemainingVertices());

        List<Vertex> highDegVerticesHost = hostGraph.getHighestDegVertices(numHighDegVertices);
        LabelledSubgraphCountingAlgorithm labelledSubgraphCountingAlgorithm = new LabelledSubgraphCountingAlgorithm(hostGraph, patternGraph, highDegVerticesHost);
        int count = labelledSubgraphCountingAlgorithm.run();
        result.setCount(count);

        long endTime = System.currentTimeMillis();
        result.setAlgorithmRuntimeInMilliseconds(endTime - timeAfterParameterOptimisation);
        result.setTotalRuntimeInMilliseconds(endTime - startTime);

        if (count > 0) {
            result.setStatus(RunStatus.PASS);
        } else {
            result.setStatus(RunStatus.FAIL);
        }

        System.out.println(result);
    }

}
