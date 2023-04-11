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

            add(currentNode);
            add(newNode);

            Set<Edge<N>> edges = adjacentNodes.get(currentNode);

            Edge<N> currentToNew = new Edge<>(bindName, weight, newNode);
            if(edges != null) {
                edges.add(currentToNew);
                adjacentNodes.put(currentNode, edges);
            } else {
                edges = new HashSet<>();
                edges.add(currentToNew);
                adjacentNodes.put(currentNode, edges);
            }

            edges = adjacentNodes.get(newNode);
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
