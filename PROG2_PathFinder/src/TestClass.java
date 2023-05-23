import javafx.scene.paint.Color;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class TestClass {
    /*
    NOTE:
    * Do not write try/catch clauses in listGraph class
    * I1 - 'NullPointerException' cannot invoke java.util.Set.iterator (methods with problems: getEdgeBetween, disconnect)

    */
    private static final City NODEONE = new City<>("New York");
    private static final City NODETWO = new City<>("Stockholm");
    private static final City NODETHREE = new City<>("Berlin");

    //ListGraph listGraphClass = new ListGraph();
    //WIP

    ListGraph graph = new ListGraph();
    Edge edgeClass;

    public static void main(String[] args) {
        TestClass testClass = new TestClass();
        testClass.loadData();

        testClass.testConnectMethod();

        //testClass.disconnectEdge();
        testClass.printSet();

        //testClass.testEdge();
        //testClass.removeData();

        //System.out.println("New: ");
        //testClass.printSet();

        //PathFinder.start();
    }

    public ListGraph runTests() {
        PathFinder pathFinder = new PathFinder();
        if (pathFinder.getListGraph() == null) {
            System.err.println("graph is null");
        } else {
            graph = pathFinder.getListGraph();
        }
        loadData();
        testConnectMethod();
        return graph;
    }


    //Test functionality of connect method

    public void testConnectMethod() {

        graph.connect(NODEONE, NODETHREE, "aConnection", 300);
        graph.connect(NODETWO, NODETHREE, "S -> B", 500);

        //Throws exception: IllegalStateException
        //listGraphClass.connect(NODEONE, NODETHREE, "cConnection", 800);
    }

    public void loadData() {
        graph.add(NODEONE);
        graph.add(NODETWO);
        graph.add(NODETHREE);

    }

    private void removeData() {
        graph.remove(NODETWO);
    }

    private void disconnectEdge() {
        graph.disconnect(NODETWO, NODETHREE);
        //listGraphClass.disconnect(NODEONE, NODETWO);
    }

    private void printSet() {
        //System.out.println(listGraphClass.existingNodes);
        //System.out.println(graph.toString());
        System.out.println(graph.printConnections());
    }


    private void testEdge() {
        Edge edgeA = graph.getEdgeBetween(NODETWO, NODETHREE);
        System.out.println(edgeA);
    }

    private void testReadNodes() {
        PathFinder pf = new PathFinder();
    }
}
