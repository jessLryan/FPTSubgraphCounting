import algorithmRunner.AlgorithmRunner;
import algorithmRunner.RunResult;
import graph.Graph;
import graph.GraphFileReader;

public class Main {

    public static void Main(String[] args) {
        String patternGraphFilepath = args[0];
        String hostGraphFilepath = args[1];

        Graph patternGraph = GraphFileReader.readToGraph(patternGraphFilepath);
        Graph hostGraph = GraphFileReader.readToGraph(hostGraphFilepath);
        RunResult result = new RunResult(hostGraph,
                hostGraphFilepath,
                patternGraph,
                patternGraphFilepath);
        AlgorithmRunner.runAlgorithm(result, hostGraph, patternGraph);
    }






}
