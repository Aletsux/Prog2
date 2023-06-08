import java.util.*;

// Grupp 365
// Cheng che Huang chhu9071
// Adin Farid adfa8505
// Sara Afshar saaf0625
public class ListGraph<N> implements Graph<N> {

    private Map<N, Set<Edge<N>>> adjacentNodes = new HashMap<>();
    private Set<N> visited = new HashSet<N>();

    public Set<N> getNodes() {
        return adjacentNodes.keySet();
    }

    public Set<Edge<N>> getEdges(N key) {
        return adjacentNodes.get(key);
    }

    public void add(N node) {
        if (adjacentNodes.containsKey(node)) {
            return;
        }
        adjacentNodes.putIfAbsent(node, new HashSet<>());
    }

    public void connect(N currentNode, N newNode, String bindName, int weight) {
        //checks whether selected nodes exist
        if (!adjacentNodes.containsKey(currentNode) || !adjacentNodes.containsKey(newNode)) {
            throw new NoSuchElementException("Node doesn't exist");
        }
        //Checks validity of weight value
        if (weight < 0) {
            throw new IllegalArgumentException("Invalid value");
        }

        boolean edgeExists = false;

        for (Set<Edge<N>> setOfEdges : adjacentNodes.values()) {
            for (Edge<N> edge : setOfEdges) {
                if (edge.equals(getEdgeBetween(currentNode, newNode))) {
                    edgeExists = true;
                }
            }
        }
        if (edgeExists) {
            throw new IllegalStateException("A connection already exists:");
        }


        //Reference the Set<Edge> for the currentNode in the adjacentNodes map
        Set<Edge<N>> edges = adjacentNodes.get(currentNode);

        //create a new edge from currentNode to newNode
        Edge<N> currentToNew = new Edge<N>(bindName, weight, newNode);


        if (edges != null) {
            edges.add(currentToNew);
            adjacentNodes.put(currentNode, edges);
        } else {
            edges = new HashSet<>();
            edges.add(currentToNew);
            adjacentNodes.put(currentNode, edges);
        }

        edges = adjacentNodes.get(newNode);

        Edge<N> newToCurrent = new Edge<>(bindName, weight, currentNode);
        if (edges != null) {
            edges.add(newToCurrent);
            adjacentNodes.put(newNode, edges);
        } else {
            edges = new HashSet<>();
            edges.add(newToCurrent);
            adjacentNodes.put(newNode, edges);
        }

        if (edges == null) {
            edges = new HashSet<>();
            adjacentNodes.put(currentNode, edges);
        }
    }


    public void remove(N nodeToRemove) {
        if (!adjacentNodes.containsKey(nodeToRemove)) {
            throw new NoSuchElementException("Not an existing node!");
        }

        Set<Edge<N>> connections = adjacentNodes.get(nodeToRemove);

        if (connections != null) {
            for (Edge<N> edge : connections) {
                if (edge.getDestination() != nodeToRemove) {
                    Set<Edge<N>> adjacentEdges = adjacentNodes.get(edge.getDestination());
                    adjacentEdges.removeIf(e -> e.getDestination() == nodeToRemove);
                }
            }
        }

        if (adjacentNodes.get(nodeToRemove) != null) {
            adjacentNodes.remove(nodeToRemove);
        } else {
            throw new NoSuchElementException("Element was null");
        }
    }

    @Override
    public void setConnectionWeight(N node1, N node2, int newWeight) {

        if (!adjacentNodes.containsKey(node1) || !adjacentNodes.containsKey(node2)) {
            throw new NoSuchElementException();
        }

        if (newWeight < 0) {
            throw new IllegalArgumentException();
        }

        Set<Edge<N>> edges = adjacentNodes.get(node1);
        if (edges == null || adjacentNodes.get(node2) == null) {
            throw new NoSuchElementException();
        }

        if (newWeight < 0) {
            throw new IllegalArgumentException("Invalid weight value");
        }

        Edge edgeAtoB = getEdgeBetween(node1, node2);
        edgeAtoB.setWeight(newWeight);

        Edge edgeBtoA = getEdgeBetween(node2, node1);
        edgeBtoA.setWeight(newWeight);
    }

    @Override
    public Collection<Edge<N>> getEdgesFrom(N node) {

        if (!adjacentNodes.containsKey(node)) {
            throw new NoSuchElementException();
        }

        if (!adjacentNodes.containsValue(getEdges(node))) {
            throw new NoSuchElementException();
        }
        return adjacentNodes.get(node);
    }

    @Override
    public Edge<N> getEdgeBetween(N node1, N node2) {
        Edge<N> edgeToReturn = null;
        if (adjacentNodes.get(node1) == null || adjacentNodes.get(node2) == null) {
            throw new NoSuchElementException("Error: node not registered");
        }
        Collection<Edge<N>> edgesTo = adjacentNodes.get(node1); //Checks one way
        Collection<Edge<N>> edgesFrom = adjacentNodes.get(node2);
        if (edgesTo == null || edgesFrom == null) {
            throw new NoSuchElementException("Error: Node has no edges");
        }
        for (Edge edgeTo : adjacentNodes.get(node1)) {
            if (edgeTo.getDestination().equals(node2)) {
                edgeToReturn = edgeTo;
            } else {
                for (Edge edgeFrom : adjacentNodes.get(node1)) {
                    if (edgeFrom.getDestination().equals(node2)) {
                        edgeToReturn = edgeFrom;
                    }
                }
            }
        }
        
        return edgeToReturn;
    }


    @Override
    public void disconnect(N node1, N node2) {

        if (adjacentNodes.get(node1) == null || adjacentNodes.get(node2) == null) {
            throw new NoSuchElementException("Nodes not registered in the system");
        }
        if (getEdgeBetween(node1, node2) == null || getEdgeBetween(node2, node1) == null) {
            throw new IllegalStateException("No connection between the nodes exists");
        }

        if (node1 instanceof String) {
            Edge edgeFrom = getEdgeBetween(node1, node2);
            //iterate over connectionsFrom, remove edges from other nodes
            Set<Edge<N>> connectionsFrom = adjacentNodes.get(edgeFrom.getDestination());
            for (Edge<N> edge : connectionsFrom) {
                if (edge.getDestination() != node1) {
                    Set<Edge<N>> adjacentEdges = adjacentNodes.get(node2);
                    adjacentEdges.removeIf(e -> e.getDestination().equals(node1));
                }
            }

            //Iterate over connectionsTo, remove edges to other nodes
            Set<Edge<N>> connectionsTo = adjacentNodes.get(node2);
            for (Edge<N> edge : connectionsTo) {
                if (edge.getDestination() != node2) {
                    Set<Edge<N>> adjacentEdges = adjacentNodes.get(node1);
                    adjacentEdges.removeIf(e -> e.getDestination().equals(node2));
                }
            }
        }

        //iterate over connectionsFrom, remove edges from other nodes
        Set<Edge<N>> adjacentEdgesFrom = adjacentNodes.get(node2);
        Set<Edge<N>> adjacentEdgesTo = adjacentNodes.get(node1);

        adjacentEdgesFrom.removeIf(e -> e.getDestination().equals(node1));
        if (adjacentEdgesFrom.isEmpty()) {
            adjacentEdgesTo.removeIf(e -> e.getDestination().equals(node2));
        }
    }


    @Override
    public boolean pathExists(N node1, N node2) { // byt namn på noderna så de följer konventioner
        if (node1 == null || node2 == null) {
            return false;
        }

        if (!adjacentNodes.containsKey(node1) || !adjacentNodes.containsKey(node2)) {
            return false;
        }

        depthFirstSearch(node1, node2, visited, new Stack<>());
        return visited.contains(node2);
    }

    @Override
    public List<Edge<N>> getPath(N nodeFrom, N nodeTo) {
        Set<N> visited = new HashSet<>();
        Stack<Edge<N>> stack = new Stack<>();

        depthFirstSearch(nodeFrom, nodeTo, visited, stack);

        if (stack.isEmpty()) {
            // If there is no path between the nodes, return null
            return null;
        } else {
            // Otherwise, extract the path from the stack of edges
            List<Edge<N>> path = new ArrayList<>(stack);
            Collections.reverse(path);
            return path;
        }
    }

    private List<Edge<N>> depthFirstSearch(N node1, N node2, Set<N> visited, Stack<Edge<N>> stack) {
        visited.add(node1);
        if (node1.equals(node2)) {
            return new ArrayList<>(stack);
        }
        for (Edge<N> edge : adjacentNodes.get(node1)) {
            if (!visited.contains(edge.getDestination())) {
                stack.push(edge);
                List<Edge<N>> result = depthFirstSearch(edge.getDestination(), node2, visited, stack);
                if (result != null) {
                    return result;
                }
                stack.pop();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (N city : adjacentNodes.keySet()) {
            Set<Edge<N>> edgeToPrint = getEdges(city);
            sb.append(city).append(" : ").append(edgeToPrint).append("\n");
        }
        return sb.toString();
    }
}