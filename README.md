# FPT Subgraph Counting Algorithm
An implementation of an algorithm described in [[1]](#1) for counting the number of times that one graph appears as a subgraph of the other.

The algorithm is expected to be efficient when the first graph is small, and the second graph has only a small proportion of high-degree vertices. 

More formally, the algorithm is an FPT algorithm for labelled subgraph counting in classes of host graphs from the class of graphs with almost-bounded degree parameterised by the order
of the pattern graph.


### Input
The algorithm takes two inputs: a file containing the pattern graph data, and a file containing the host graph data.

The file for each graph must be a .txt file written in LAD format where the first line states the number of vertices in the graph, and the next $n$ lines state, for each vertex, its number of successor nodes, followed by the list of its successor nodes. 

For example, the complete graph $K_4$ would be written as

4  
3 1 2 3  
3 0 2 3  
3 0 1 3  
3 0 1 2

### Output
The result is printed to the screen and contains the following information:

- **Datetime:** the date/time on which the code was run
- **Graph Statistics**: includes the following properties of each of the host and the pattern graph:
    - **filepath**: the absolute filepath of the input file
    - **graph type**: either HOST or PATTERN
    - **order**: the number of vertices in the graph
    - **number of edges**
    - **average degree**

- **Number of high degree vertices**: the value of this parameter is optimised within the code
- **Maximum degree of remaining vertices**: the maximum degree of the host graph after the 'removal' of the high degree vertices
- **Run Status**: PASS if the number of copies of the graph is $>0$, FAIL if there are no copies, and INTERRUPTED if the program fails to complete for whatever reason
- **count** the number of labelled copies of the first graph in the second (null if interrupted)
- **runtime in milliseconds**

## References
<a id="1">[1]</a>
Ryan, Jessica Laurette,
_Parameterised Algorithms for Counting Subgraphs, Matchings, and Monochromatic Partitions_,
PhD Thesis,
2023
