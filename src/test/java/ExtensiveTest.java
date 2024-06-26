import algorithm.BruteForceLabelledSubgraphCountingAlgorithm;
import algorithm.LabelledSubgraphCountingAlgorithm;
import algorithm.ParameterValueOptimiser;
import graph.Graph;
import graph.GraphFileReader;
import graph.Vertex;
import graph.VertexLists;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

//runs FPT algorithm and compares result to output of
//Brute Force algorithm with same inputs
//large number of graph instances included
public class ExtensiveTest {

    private final String resourcesDirectoryPath = "src/test/resources";

    @Test
    public void TestRandomHostGraphsPatternGraphsOrder1() {
        int unsuccessful = 0;

        for (int i = 1; i < 8; i++) {
            List<Graph> hostGraphs = createRandomHostGraphs(i, 20, 1000);
            ArrayList<Graph> patterns = createAllPatternGraphsWithOrder(i);
            for (Graph host : hostGraphs) {
                for (Graph pattern : patterns) {
                    ParameterValueOptimiser parameterOptimiser = new ParameterValueOptimiser(pattern, host);
                    int numHighDegVertices = parameterOptimiser.getNumHighDegVertices();
                    List<Vertex> highestDegVertices = host.getHighestDegVertices(numHighDegVertices);
                    LabelledSubgraphCountingAlgorithm FPTAlg = new LabelledSubgraphCountingAlgorithm(host, pattern, highestDegVertices);
                    int FPTCount = FPTAlg.run();

                    VertexLists mapLists = new VertexLists(host, pattern);
                    int bruteForceCount = BruteForceLabelledSubgraphCountingAlgorithm.countLabelledCopiesWithLists(pattern, mapLists);
                    if (bruteForceCount != FPTCount) {
                        unsuccessful++;
                    }
                }
            }

        }
        assertEquals(0, unsuccessful);
    }

    public ArrayList<Graph> createAllPatternGraphsWithOrder(int order) {
        ArrayList<Graph> graphs = new ArrayList<>();
        File folder = new File(resourcesDirectoryPath + "/pattern_graphs/" + order);
        for (File patternFile : Objects.requireNonNull(folder.listFiles())) {
            Graph pattern = GraphFileReader.readToGraph(patternFile.getPath());
            graphs.add(pattern);
        }
        return graphs;
    }

    private List<Graph> createRandomHostGraphs(int minOrder, int maxOrder, int amountWanted) {
        List<Graph> hostGraphList = new ArrayList<>();
        for (int i = 0; i < amountWanted; i++) {
            int randomOrder = (int) (Math.random() * maxOrder);
            hostGraphList.add(createRandomHostGraphWithOrder(Math.max(minOrder, randomOrder)));
        }
        return hostGraphList;
    }


    private Graph createRandomHostGraphWithOrder(int order) {
        Random random = new Random();
        ArrayList<Vertex> vertices = createVertices(order);
        for (int vertex1Index = 0; vertex1Index < order; vertex1Index++) {
            Vertex vertex1 = vertices.get(vertex1Index);
            for (int vertexIndex2 = vertex1Index + 1; vertexIndex2 < order; vertexIndex2++) {
                boolean isAdjacent = random.nextBoolean();
                if (isAdjacent) {
                    Vertex vertex2 = vertices.get(vertexIndex2);
                    vertex1.addNeighbour(vertex2);
                    vertex2.addNeighbour(vertex1);
                }
            }
        }
        return new Graph(vertices);
    }

    private ArrayList<Vertex> createVertices(int numVertices) {
        return new ArrayList<>(IntStream.range(0, numVertices).mapToObj(i -> new Vertex()).toList());
    }


}
