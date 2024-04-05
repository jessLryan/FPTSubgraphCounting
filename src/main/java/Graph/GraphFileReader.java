package Graph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class GraphFileReader {

    //read LAD format graph file
    public static Graph readToGraph(String filepath) {
        BufferedReader reader = createBufferedReader(filepath);
        try {
            String line = reader.readLine();
            int order = Integer.parseInt(line);
            ArrayList<Vertex> vertices = createVertices(order);
            line = reader.readLine();
            for (Vertex vertex : vertices) {
                String[] neighboursAsStrings = line.split(" ");
                for (int i = 1; i < neighboursAsStrings.length; i++) {
                    int neighbourIndex = Integer.parseInt(neighboursAsStrings[i]);
                    Vertex neighbour = vertices.get(neighbourIndex);
                    vertex.addNeighbour(neighbour);
                    neighbour.addNeighbour(vertex);
                }
                line = reader.readLine();
            }
            reader.close();
            return new Graph(vertices);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<Vertex> createVertices(int numVertices) {
        return new ArrayList<>(IntStream.range(0, numVertices).mapToObj(index -> new Vertex()).toList());
    }

    private static BufferedReader createBufferedReader(String filepath) {
        try {
            FileReader fileReader = new FileReader(filepath);
            return new BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
