package AlgorithmRunner;

import Graph.Graph;
import Graph.GraphStatistics;
import Graph.GraphType;

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
                "date=" + date +
                ", patternGraphStatistics=" + patternGraphStatistics +
                ", hostGraphStatistics=" + hostGraphStatistics +
                ", numberOfHighDegreeVertices=" + numberOfHighDegreeVertices +
                ", maximumDegreeOfRemainingVertices=" + maximumDegreeOfRemainingVertices +
                ", status=" + status +
                ", count=" + count +
                ", runtimeInMilliseconds=" + runtimeInMilliseconds +
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
