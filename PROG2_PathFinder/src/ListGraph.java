import java.util.*;

/* Suggestions:
Type of collection: probably LinkedList
Nodes - A generic type 'N'
Graph - Interface we need to create?

ToDo:
 */

public class ListGraph<N> implements Graph<N> {
    //LinkedList<N> existingNodes = new LinkedList<>();

    //Tracks nodes and connected edges
    private Map<N, Set<Edge<N>>> adjacentNodes = new HashMap<>();
    public Set<N> visited = new HashSet<N>();

    public void add(N node) {
        if (adjacentNodes.containsKey(node)) {
            System.err.println("Error: Node already exists!");
        }
        //existingNodes.add(node);
        adjacentNodes.putIfAbsent(node, new HashSet<Edge<N>>());
    }

    // Multiple exceptions need to be thrown
    // 'bindName' - from user input?
    public void connect(N currentNode, N newNode, String bindName, int weight) {

        try {
            //Throw exceptions
            //checks whether selected nodes exist
            if (!adjacentNodes.containsKey(currentNode) || !adjacentNodes.containsKey(newNode)) {
                throw new NoSuchElementException("Node doesn't exist");
            }

            if (weight < 0) {
                throw new IllegalArgumentException("Invalid value");
            }

            if (adjacentNodes.get(currentNode) == null) {
                throw new IllegalStateException("A connection already exists");
            }

            System.out.println("Entered connect method");

            //Reference the Set<Edge> for the currentNode in the adjacentNodes map
            Set<Edge<N>> edges = adjacentNodes.get(currentNode);

            //create a new edge from currentNode to newNode
            Edge<N> currentToNew = new Edge<N>(bindName, weight, newNode);

            //more optimal version: adjacentNodes.computeIfAbsent(currentNode, k -> new HashSet<Edge<N>>()).add(currentToNew);
            if (edges != null) {
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
            if (edges != null) {
                edges.add(newToCurrent);
                adjacentNodes.put(newNode, edges);
            } else {
                edges = new HashSet<>();
                edges.add(newToCurrent);
                adjacentNodes.put(newNode, edges);
            }

            //If list is empty create new list of Edges
            if (edges == null) {
                edges = new HashSet<>();
                adjacentNodes.put(currentNode, edges);
            }

        }

        //Catch exceptions
        catch (NoSuchElementException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    //ToDo: Need to remove edges
    public void remove(N nodeToRemove) throws NoSuchElementException {
        try {
            System.out.println("Entered remove method!");
            if (!adjacentNodes.containsKey(nodeToRemove)) {
                throw new NoSuchElementException("Not an existing node!");
            }

            //save all connections to nodeToRemove
            Set<Edge<N>> connections = adjacentNodes.get(nodeToRemove);

            //iterate over connections, remove edges from other nodes
            for (Edge<N> edge : connections) {
                if (edge.getDestination() != nodeToRemove) {
                    Set<Edge<N>> adjacentEdges = adjacentNodes.get(edge.getDestination());
                    adjacentEdges.removeIf(e -> e.getDestination() == nodeToRemove);
                }
            }
            //removes the node and all edges from the node
            adjacentNodes.remove(nodeToRemove);

            System.out.println("All connections should be removed!");

        } catch (NoSuchElementException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // returns amount of 'nodes'
    public Set<N> getNodes() {
        return adjacentNodes.keySet();
    }

    public Set<Edge<N>> getEdges(N key) {
        return adjacentNodes.get(key);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
            for (City city : nodes.keySet()) {
                sb.append(city).append("\n");
            }

            return sb.toString();

        }


    @Override
    public void setConnectionWeight(N node1, N node2, int weight) {

    }

    @Override
    public Collection<Edge<N>> getEdgesFrom(N node) {
        return null;
    }

    @Override
    public Edge<N> getEdgeBetween(N node1, N node2) {
        for (Edge edge : adjacentNodes.get(node2)) {
            if (edge.getDestination().equals(node1)) {
                return edge;
            }
        }
        return null;
    }


    @Override
    public void disconnect(N node1, N node2) throws NoSuchElementException {
        try {
            if (getEdgeBetween(node1, node2) == null) {
                throw new NoSuchElementException("No connection between the nodes exists");
            }
        } catch (NoSuchElementException e) {
            System.err.println("Error: " + e.getMessage());
        }

        Set<Edge<N>> connections = adjacentNodes.get(node1);




    }

    @Override
    public boolean pathExists(N node1, N node2) { // byt namn på noderna så de följer konventioner
        getPath(node1, node2);
        return visited.contains(node2);
    }

    @Override
    public List<Edge<N>> getPath(N from, N to) { //borde nog vara private
        visited.add(from);
        if (from.)

        return null;
    }


}
