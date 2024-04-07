package AlgorithmRunner;

import Algorithm.LabelledSubgraphCountingAlgorithm;
import Algorithm.ParameterValueOptimiser;
import Graph.Graph;
import Graph.Vertex;

import java.util.List;

public class AlgorithmRunner {

    public static void runAlgorithm(RunResult result, Graph hostGraph, Graph patternGraph) {

        long startTime = System.currentTimeMillis();
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    long endTime = System.currentTimeMillis();
                    result.setStatus(RunStatus.INTERRUPTED);
                    result.setRuntimeInMilliseconds(endTime - startTime);
                })
        );

        ParameterValueOptimiser parameterValueOptimiser = new ParameterValueOptimiser(hostGraph, patternGraph);
        int numHighDegVertices = parameterValueOptimiser.getNumHighDegVertices();
        result.setNumberOfHighDegreeVertices(numHighDegVertices);
        result.setMaximumDegreeOfRemainingVertices(parameterValueOptimiser.getMaxDegRemainingVertices());

        List<Vertex> highDegVerticesHost = hostGraph.getHighestDegVertices(numHighDegVertices);
        LabelledSubgraphCountingAlgorithm labelledSubgraphCountingAlgorithm = new LabelledSubgraphCountingAlgorithm(hostGraph, patternGraph, highDegVerticesHost);
        int count = labelledSubgraphCountingAlgorithm.run();
        result.setCount(count);

        long endTime = System.currentTimeMillis();
        result.setRuntimeInMilliseconds(endTime - startTime);

        if (count > 0) {
            result.setStatus(RunStatus.PASS);
        } else {
            result.setStatus(RunStatus.FAIL);
        }

        System.out.println(result);
    }

}
