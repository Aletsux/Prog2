import java.util.Set;

public class TestClass {
    private static final String NODEONE = "New York";
    private static final String NODETWO = "Stockholm";
    private static final String NODETHREE = "Berlin";

    ListGraph listGraphClass = new ListGraph();

    public static void main(String[] args) {
        TestClass testClass = new TestClass();
        testClass.loadData();

        testClass.testConnectMethod();
        testClass.removeData();
        testClass.printSet();
    }

    //Test functionality of connect method

    public void testConnectMethod() {

        listGraphClass.connect(NODEONE, NODETHREE, "aConnection", 300);
        listGraphClass.connect(NODETWO, NODETHREE, "bConnection", 500);
    }

    private void loadData() {
        listGraphClass.add(NODEONE);
        listGraphClass.add(NODETWO);
        listGraphClass.add(NODETHREE);
    }

    private void removeData() {
        listGraphClass.remove(NODEONE);

    }

    private void printSet() {
        //System.out.println(listGraphClass.existingNodes);
        System.out.println(listGraphClass.toString());
    }


}
