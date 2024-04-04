package Algorithm;

import Graph.Graph;

//calculate optimal values of parameters:
//- number of vertices considered the "high degree vertices" in the host graph
//- maximum degree of the remaining vertices in the host
//optimal values calculated using the theoretical runtime function of the algorithm
public class ParameterValueOptimiser {

    private int numHighDegVertices = 0;
    private int maxDegRemainingVertices;

    public ParameterValueOptimiser(Graph pattern, Graph host) {
        int patternOrder = pattern.order();
        int hostMaxDeg = host.maxDeg();
        maxDegRemainingVertices = hostMaxDeg;

        long functionValue = longPower(hostMaxDeg, patternOrder);
        for (int numVerticesRemoved = 1; numVerticesRemoved <= patternOrder; numVerticesRemoved++) {
            int maxDegRemaining = host.maxDegRemaining(numVerticesRemoved);

            long term1 = longPower(numVerticesRemoved * patternOrder, numVerticesRemoved);
            long term2 = longPower(maxDegRemaining, patternOrder);
            long newFunctionValue = term1 * term2;

            if (newFunctionValue < functionValue) {
                numHighDegVertices = numVerticesRemoved;
                maxDegRemainingVertices = maxDegRemaining;
            }
        }
    }

    private static long longPower(int base, int exp) {
        long result = base;
        for (int power = 1; power < exp; power++) {
            result = result * base;
        }
        return result;
    }

    public int getNumHighDegVertices() {
        return numHighDegVertices;
    }

    public int getMaxDegRemainingVertices() {
        return maxDegRemainingVertices;
    }
}
