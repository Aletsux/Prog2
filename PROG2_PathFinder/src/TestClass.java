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
    private static final City NODEONE = new City<>("New York", 100.0, 200.0);
    private static final City NODETWO = new City<>("Stockholm", 200.0, 300.0);
    private static final City NODETHREE = new City<>("Berlin", 300.0, 400.0);
    private static final City NODEFOUR = new City<>("Tokyo", 400, 500);
    private static final City NODEFIVE = new City<>("London", 500, 600);

    //ListGraph listGraphClass = new ListGraph();
    //WIP

    ListGraph graph = new ListGraph();
    Edge edgeClass;
    PathFinder pathFinder = new PathFinder();

    public static void main(String[] args) {
        TestClass testClass = new TestClass();
        testClass.loadData();

        //testClass.testConnectMethod();
        //testClass.addSelectedNodes();

        //testClass.disconnectEdge();
        //testClass.printSet();

        //testClass.testEdge();
        //testClass.removeData();

        //System.out.println("New: ");
        //testClass.printSet();

        //PathFinder.start();
    }

    public ListGraph runTests() {

        if (pathFinder.getListGraph() == null) {
            System.err.println("graph is null");
        } else {
            graph = pathFinder.getListGraph();
        }
        loadData();
        testConnectMethod();
        addSelectedNodes(pathFinder.selectedNodes);
        return graph;
    }


    //Test functionality of connect method

    public void testConnectMethod() {

        graph.connect(NODEONE, NODETHREE, "aConnection", 300);
        graph.connect(NODETWO, NODETHREE, "S -> B", 500);
        //graph.connect(NODEFOUR, NODEFIVE, "Flight", 12);
        graph.connect(NODETWO, NODEFIVE, "S -> L", 300);
        graph.connect(NODEFIVE, NODEFOUR, "L -> T", 500);

        //Throws exception: IllegalStateException
        //listGraphClass.connect(NODEONE, NODETHREE, "cConnection", 800);
    }

    public void loadData() {
        graph.add(NODEONE);
        graph.add(NODETWO);
        graph.add(NODETHREE);
        graph.add(NODEFOUR);
        graph.add(NODEFIVE);

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
        System.out.println(graph.toString());
        System.out.println(graph.printConnections());
    }


    private void testEdge() {
        Edge edgeA = graph.getEdgeBetween(NODETWO, NODETHREE);
        System.out.println(edgeA);
    }

    private void testReadNodes() {
        PathFinder pf = new PathFinder();
    }

    private void addSelectedNodes(City[] nodes) {
        nodes[0] = NODEONE;
        nodes[1] = NODETHREE;
        System.out.println(nodes.toString());
    }

}
