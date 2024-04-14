package algorithmRunner;

import graph.Graph;
import graph.GraphStatistics;
import graph.GraphType;

import java.time.LocalDateTime;

public class RunResult {
    private final LocalDateTime datetime;
    private final GraphStatistics patternGraphStatistics;
    private final GraphStatistics hostGraphStatistics;
    private Integer numberOfHighDegreeVertices = null;
    private Integer maximumDegreeOfRemainingVertices;
    private RunStatus status;
    private Integer count;
    private Long parameterOptimisationRuntime;
    private Long algorithmRuntime;
    private Long totalRuntime;

    public RunResult(Graph hostGraph,
                     String hostGraphFilepath,
                     Graph patternGraph,
                     String patternGraphFilepath) {
        patternGraphStatistics = new GraphStatistics(patternGraph, patternGraphFilepath, GraphType.PATTERN_GRAPH);
        hostGraphStatistics = new GraphStatistics(hostGraph, hostGraphFilepath, GraphType.HOST_GRAPH);
        datetime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "RunResult\n" +
                "datetime: " + datetime + "\n" +
                "pattern graph statistics: " + patternGraphStatistics + "\n" +
                "host graph statistics: " + hostGraphStatistics + "\n" +
                "number of high degree vertices parameter: " + numberOfHighDegreeVertices + "\n" +
                "maximum degree of remaining host graph: " + maximumDegreeOfRemainingVertices + "\n" +
                "run status: " + status + "\n" +
                "count: " + count + "\n" +
                "parameter optimisation runtime in milliseconds: " + parameterOptimisationRuntime + "\n" +
                "algorithm runtime in milliseconds: : " + algorithmRuntime + "\n" +
                "total runtime in milliseconds: " + totalRuntime;
    }

    public void setMaximumDegreeOfRemainingVertices(int maximumDegreeOfRemainingVertices) {
        this.maximumDegreeOfRemainingVertices = maximumDegreeOfRemainingVertices;
    }

    public void setNumberOfHighDegreeVertices(int numberOfHighDegreeVertices) {
        this.numberOfHighDegreeVertices = numberOfHighDegreeVertices;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setTotalRuntimeInMilliseconds(long runtimeInMilliseconds) {
        this.totalRuntime = runtimeInMilliseconds;
    }

    public void setAlgorithmRuntimeInMilliseconds(long runtimeInMilliseconds) {
        this.algorithmRuntime = runtimeInMilliseconds;
    }

    public void setParameterOptimisationRuntimeInMilliseconds(long runtimeInMilliseconds) {
        this.parameterOptimisationRuntime = runtimeInMilliseconds;
    }

    public void setStatus(RunStatus runStatus) {
        this.status = runStatus;
    }
}
