import Algorithm.BruteForceLabelledSubgraphCountingAlgorithm;
import Algorithm.LabelledSubgraphCountingAlgorithm;
import Algorithm.ParameterValueOptimiser;
import org.junit.Test;

import Graph.Graph;
import Graph.GraphFileReader;
import Graph.Vertex;
import Graph.VertexLists;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;


public class MainTest {

    List<Graph> hostGraphs = createRandomHostGraphs(15, 5);
    String resourcesDirectoryPath = "src/test/resources";

    @Test
    public void TestRandomHostGraphsPatternGraphsOrder1() {
        ArrayList<Graph> patterns = createAllPatternGraphsWithOrder(1);
        for (Graph host : hostGraphs) {
            for (Graph pattern : patterns) {
                ParameterValueOptimiser parameterOptimiser = new ParameterValueOptimiser(pattern, host);
                int numHighDegVertices = parameterOptimiser.getNumHighDegVertices();
                List<Vertex> highestDegVertices = host.getHighestDegVertices(numHighDegVertices);
                LabelledSubgraphCountingAlgorithm FPTAlg = new LabelledSubgraphCountingAlgorithm(host, pattern, highestDegVertices);
                int FPTCount = FPTAlg.run();

                VertexLists mapLists = new VertexLists(host, pattern);
                int bruteForceCount = BruteForceLabelledSubgraphCountingAlgorithm.countLabelledCopiesWithLists(pattern, mapLists);

                assertEquals(FPTCount, bruteForceCount);
            }
        }
    }


    public ArrayList<Graph> createAllPatternGraphsWithOrder(int order) {
        ArrayList<Graph> graphs = new ArrayList<>();
        File folder = new File(resourcesDirectoryPath+"/pattern_graphs/"+order);
            for (File patternFile: Objects.requireNonNull(folder.listFiles())) {
                Graph pattern = GraphFileReader.readToGraph(patternFile.getPath());
                graphs.add(pattern);
            }
        return graphs;
    }

    private List<Graph> createRandomHostGraphs(int maxOrder, int amountWanted) {
        List<Graph> hostGraphList = new ArrayList<>();
        for (int i=0;i<amountWanted;i++) {
            int randomOrder = (int) (Math.random()*maxOrder);
            hostGraphList.add(createRandomHostGraphWithOrder(randomOrder));
        }
        return hostGraphList;
    }


    private Graph createRandomHostGraphWithOrder(int order) {
        Random random = new Random();
        List<Vertex> vertices = createVertices(order);
        for (int vertex1Index =0;vertex1Index<order;vertex1Index++) {
            Vertex vertex1 = vertices.get(vertex1Index);
            for (int vertexIndex2=vertex1Index+1;vertexIndex2<order;vertexIndex2++) {
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

    private List<Vertex> createVertices(int numVertices) {
        return IntStream.range(0, numVertices).mapToObj(i -> new Vertex()).toList();
    }


}
