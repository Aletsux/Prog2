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
    private Map<N, Set<Edge<N>>> adjacentNodes = new HashMap<>();

    public void add(N node) {
        if(adjacentNodes.containsKey(node)) {
            System.err.println("Error: Node already exists!");
        }
        //existingNodes.add(node);
        adjacentNodes.putIfAbsent(node, new HashSet<Edge<N>>());
    }

    //ToDo: Need to remove edges
    public void remove(N node) throws NoSuchElementException {
        try {
            if(!adjacentNodes.containsKey(node)) {
                throw new NoSuchElementException("Not an existing node!");
            }
            adjacentNodes.remove(node);
        } catch (NoSuchElementException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Multiple exceptions need to be thrown
    // 'bindName' - from user input?
    public void connect(N currentNode, N newNode, String bindName, int weight) {

        try {
            System.out.println("Entered connect method");

            //Add nodes if they don't already exist
            add(currentNode);
            add(newNode);

            //Reference the Set<Edge> for the currentNode in the adjacentNodes map
            Set<Edge<N>> edges = adjacentNodes.get(currentNode);

            //create a new edge from currentNode to newNode
            Edge<N> currentToNew = new Edge<N>(bindName, weight, newNode);

            //more optimal version: adjacentNodes.computeIfAbsent(currentNode, k -> new HashSet<Edge<N>>()).add(currentToNew);
            if(edges != null) {
                edges.add(currentToNew);
                adjacentNodes.put(currentNode, edges);
            } else {
                edges = new HashSet<>();
                edges.add(currentToNew);
                adjacentNodes.put(currentNode, edges);
            }

            //Reference the Set<Edge> for the newNode in the adjacentNodes map
            edges = adjacentNodes.get(newNode);

            //Same as above, create edge for newNode to currentNode
            Edge<N> newToCurrent = new Edge<>(bindName, weight, currentNode);
            if(edges != null) {
                edges.add(newToCurrent);
                adjacentNodes.put(newNode, edges);
            } else {
                edges = new HashSet<>();
                edges.add(newToCurrent);
                adjacentNodes.put(newNode, edges);
            }

            //If list is empty create new list of Edges
            if(edges == null) {
                edges = new HashSet<>();
                adjacentNodes.put(currentNode, edges);
            }

            //Throw exceptions
            //checks whether selected nodes exist
            if(!existingNodes.contains(currentNode) || !existingNodes.contains(newNode)) {
                throw new NoSuchElementException("Node doesn't exist");
            }

            if(weight < 0) {
                throw new IllegalArgumentException("Invalid value");
            }

            if(adjacentNodes.containsKey(currentNode)) {
                throw new IllegalStateException("A connection already exists");
            }
        }


        //Catch exceptions
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

    public Set<Edge<N>> getEdges(N key) {
        return adjacentNodes.get(key);
    }


    public String toString() {
        return adjacentNodes.toString();
    }

}
