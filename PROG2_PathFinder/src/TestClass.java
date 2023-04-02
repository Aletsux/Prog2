import java.util.Arrays;

public class TestClass {
    private static final String NODEONE = "New York";
    private static final String NODETWO = "Stockholm";

    ListGraph listGraphClass = new ListGraph();

    public static void main(String[] args) {
        Edge edgeClass = new Edge();
        TestClass testClass = new TestClass();
        System.out.println(edgeClass.getName());

        testClass.loadData();
        testClass.removeData();
        testClass.printSet();
    }

    private void loadData() {
        listGraphClass.add(NODEONE);
        listGraphClass.add(NODETWO);

    }

    private void removeData() {
        listGraphClass.remove(NODEONE);

    }

    private void printSet() {
        System.out.println(listGraphClass.nodeList);
    }
}
