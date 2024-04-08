package algorithmRunner;

import graph.Graph;
import graph.GraphStatistics;
import graph.GraphType;

import java.time.LocalDate;

public class RunResult {
    private final LocalDate date;
    private final GraphStatistics patternGraphStatistics;
    private final GraphStatistics hostGraphStatistics;
    private Integer numberOfHighDegreeVertices = null;
    private Integer maximumDegreeOfRemainingVertices = null;
    private RunStatus status;
    private Integer count = null;
    private long runtimeInMilliseconds;

    public RunResult(Graph hostGraph,
                     String hostGraphFilepath,
                     Graph patternGraph,
                     String patternGraphFilepath) {
        patternGraphStatistics = new GraphStatistics(patternGraph, patternGraphFilepath, GraphType.PATTERN_GRAPH);
        hostGraphStatistics = new GraphStatistics(hostGraph, hostGraphFilepath, GraphType.HOST_GRAPH);
        date = LocalDate.now();
    }

    @Override
    public String toString() {
        return "RunResult{" +
                "date: " + date +
                ", pattern graph statistics: " + patternGraphStatistics +
                ", host graph statistics: " + hostGraphStatistics +
                ", number of high degree vertices parameter: " + numberOfHighDegreeVertices +
                ", maximum degree of remaining host graph: " + maximumDegreeOfRemainingVertices +
                ", run status: " + status +
                ", count: " + count +
                ", runtime in milliseconds: " + runtimeInMilliseconds +
                '}';
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

    public void setRuntimeInMilliseconds(long runtimeInMilliseconds) {
        this.runtimeInMilliseconds = runtimeInMilliseconds;
    }

    public void setStatus(RunStatus runStatus) {
        this.status = runStatus;
    }
}
