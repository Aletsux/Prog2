import java.util.Set;

public class TestClass {
    /*
    NOTE:
    * Do not write try/catch clauses in listGraph class
    * I1 - 'NullPointerException' cannot invoke java.util.Set.iterator (methods with problems: getEdgeBetween, disconnect)

    */
    private static final String NODEONE = "New York";
    private static final String NODETWO = "Stockholm";
    private static final String NODETHREE = "Berlin";

    ListGraph listGraphClass = new ListGraph();
    Edge edgeClass;

    public static void main(String[] args) {
        TestClass testClass = new TestClass();
        testClass.loadData();

        testClass.testConnectMethod();

        //testClass.disconnectEdge();
        testClass.printSet();

        testClass.testEdge();
        testClass.removeData();

        System.out.println("New: ");
        testClass.printSet();


    }

    //Test functionality of connect method

    public void testConnectMethod() {

        listGraphClass.connect(NODEONE, NODETHREE, "aConnection", 300);
        listGraphClass.connect(NODETWO, NODETHREE, "S -> B", 500);

        //Throws exception: IllegalStateException
        //listGraphClass.connect(NODEONE, NODETHREE, "cConnection", 800);
    }

    private void loadData() {
        listGraphClass.add(NODEONE);
        listGraphClass.add(NODETWO);
        listGraphClass.add(NODETHREE);

    }

    private void removeData() {
        listGraphClass.remove(NODETWO);

    }

    private void disconnectEdge() {
        listGraphClass.disconnect(NODETWO, NODETHREE);
        //listGraphClass.disconnect(NODEONE, NODETWO);
    }

    private void printSet() {
        //System.out.println(listGraphClass.existingNodes);
        System.out.println(listGraphClass.toString());

    }

    private void testEdge() {
        Edge edgeA = listGraphClass.getEdgeBetween(NODETWO, NODETHREE);
        System.out.println(edgeA);
    }
}
