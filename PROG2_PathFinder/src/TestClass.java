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
        //testClass.removeData();
        //testClass.disconnectEdge();
        //testClass.getEdge();
        testClass.printSet();

        testClass.testEdge();

    }

    //Test functionality of connect method

    public void testConnectMethod() {

        listGraphClass.connect(NODEONE, NODETHREE, "aConnection", 300);
        listGraphClass.connect(NODETWO, NODETHREE, "bConnection", 500);

        //Throws exception: IllegalStateException
        //listGraphClass.connect(NODEONE, NODETHREE, "cConnection", 800);
    }

    private void loadData() {
        listGraphClass.add(NODEONE);
        listGraphClass.add(NODETWO);
        listGraphClass.add(NODETHREE);
    }

    private void removeData() {
        listGraphClass.remove(NODEONE);

    }

    private void disconnectEdge() {
        listGraphClass.disconnect(NODEONE, NODETHREE);
        //listGraphClass.disconnect(NODEONE, NODETWO);
    }

    private String getEdge() {
        return listGraphClass.getEdgeBetween(NODEONE, NODETHREE).toString();
    }

    private void printSet() {
        //System.out.println(listGraphClass.existingNodes);
        System.out.println(listGraphClass.toString());
    }

    private void testEdge() {
        Edge edgeA = listGraphClass.getEdgeBetween(NODETHREE, NODEONE);
        System.out.println(edgeA);
    }
}
