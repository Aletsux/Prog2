import java.net.StandardSocketOptions;
import java.util.*;

/* Suggestions:
Type of collection: probably LinkedList
Nodes - separate class? elements in LinkedList?
Graph - Interface we need to create?

ToDo:
 */

public class ListGraph {

    //Set<int[]> nodeSet = new HashSet<>();
    LinkedList<String> nodeList = new LinkedList<>();

    public void add(String node) {
        if(nodeList.contains(node)) {
            return;
        }
        nodeList.add(node);
    }
    public void remove(String node) throws NoSuchElementException {
        if(!nodeList.contains(node)) {
            throw new NoSuchElementException("Not an existing node!");
        }
        nodeList.remove(node);
    }

    // Multiple exceptions need to be thrown
    public void connect(String nodeOne, String nodeTwo, String bindName, int weight) {

    }

    public String toString() {
        return nodeList.toString();
    }

}
