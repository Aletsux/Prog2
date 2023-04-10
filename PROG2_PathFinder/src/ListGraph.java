import java.util.*;

/* Suggestions:
Type of collection: probably LinkedList
Nodes - A generic type 'N'
Graph - Interface we need to create?

ToDo:
 */

public class ListGraph<N> {
    LinkedList<N> existingNodes = new LinkedList<>();

    //Tracks nodes and connected edges
    private Map<N, List<Edge<N>>> adjacentNodes = new HashMap<>();

    public void add(N node) {
        if(existingNodes.contains(node)) {
            System.err.println("Error: Node already exists!");
        }
        existingNodes.add(node);
    }

    //ToDo: Need to remove edges
    public void remove(N node) throws NoSuchElementException {
        try {
            if(!existingNodes.contains(node)) {
                throw new NoSuchElementException("Not an existing node!");
            }
            existingNodes.remove(node);
        } catch (NoSuchElementException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Multiple exceptions need to be thrown
    // 'bindName' - from user input?
    public void connect(N currentNode, N newNode, String bindName, int weight) {
        try {
                //checks whether selected nodes exist
                if(!existingNodes.contains(currentNode) || !existingNodes.contains(newNode)) {
                    throw new NoSuchElementException("Node doesn't exist");
                }

                if(weight < 0) {
                    throw new IllegalArgumentException("Invalid value");
                }

                if(adjacentNodes.containsKey(currentNode)) {
                    throw new IllegalStateException("A conenction already exists");
                }

                //create instance of edge to connect nodes
                Edge<N> edge = new Edge<>(bindName, weight, newNode);

                //set list 'edges' to the key 'currentNode' from the map
                List<Edge<N>> edges = adjacentNodes.get(currentNode);

                //If list is empty create new list of Edges
                if(edges == null) {
                    edges = new ArrayList<>();
                    adjacentNodes.put(currentNode, edges);
                }
                edges.add(edge);
        }
        catch (NoSuchElementException e) {
            System.err.println("Error: " + e.getMessage());
        }

        catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }

        catch (IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // returns amount of 'nodes'
    public double getNodes() {
        return existingNodes.stream().count();
    }


    public String toString() {
        return existingNodes.toString();
    }

}
