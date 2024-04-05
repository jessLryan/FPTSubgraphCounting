package Algorithm;

import Graph.Graph;

import java.math.BigInteger;

//calculate optimal values of FPT parameters:
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

        //initial function value is the runtime of simple brute-force approach
        //i.e. no vertices are marked as "high degree vertices"
        BigInteger functionValue = BigInteger.valueOf(hostMaxDeg).pow(patternOrder);
        //note that there is no value in marking more than patternOrder
        //vertices as high degree vertices in host graph as this will
        //not change the runtime (all pattern vertices are already to be
        //mapped to the high degree set)
        for (int numVerticesRemoved = 1; numVerticesRemoved <= patternOrder; numVerticesRemoved++) {
            int maxDegRemaining = host.maxDegRemaining(numVerticesRemoved);

            BigInteger term1 = BigInteger.valueOf(numVerticesRemoved * patternOrder).pow(numVerticesRemoved+1);
            BigInteger term2 = BigInteger.valueOf(maxDegRemaining).pow(patternOrder);
            if (term1.compareTo(functionValue)<0&&term2.compareTo(functionValue)<0) {
                BigInteger newFunctionValue = term1.multiply(term2);

                if (newFunctionValue.compareTo(functionValue) < 1) {
                    numHighDegVertices = numVerticesRemoved;
                    maxDegRemainingVertices = maxDegRemaining;
                }
            }
        }
    }

    public int getNumHighDegVertices() {
        return numHighDegVertices;
    }

    public int getMaxDegRemainingVertices() {
        return maxDegRemainingVertices;
    }
}
