import java.io.IOException;
import java.net.StandardSocketOptions;
import java.util.*;

/* Suggestions:
Type of collection: probably LinkedList
Nodes - A generic type 'N'
Graph - Interface we need to create?

ToDo:
 */

public class ListGraph<N> {
    LinkedList<N> nodeList = new LinkedList<>();
    private Map<N, List<N>> adjacencyList = new HashMap<>();

    public void add(N node) {
        if(nodeList.contains(node)) {
            System.err.println("Error: Node already exists!");
        }
        nodeList.add(node);
    }

    //ToDo: Need to remove edges
    public void remove(N node) throws NoSuchElementException {
        try {
            if(!nodeList.contains(node)) {
                throw new NoSuchElementException("Not an existing node!");
            }
            nodeList.remove(node);
        } catch (NoSuchElementException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Multiple exceptions need to be thrown
    // 'bindName' - from user input
    public void connect(N currentNode, N newNode, String bindName, int weight) {
        Edge edge = new Edge(bindName, weight, newNode);

        // dont know what im doin here...
        //saves the state of the graph to the selected Node?
        adjacencyList.put(currentNode, nodeList);

    }

    // returns amount of 'nodes'
    public double getNodes() {
        return nodeList.stream().count();
    }


    public String toString() {
        return nodeList.toString();
    }

}
