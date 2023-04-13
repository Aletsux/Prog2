import java.util.Set;

public class TestClass {
    private static final String NODEONE = "New York";
    private static final String NODETWO = "Stockholm";
    private static final String NODETHREE = "Berlin";

    ListGraph listGraphClass = new ListGraph();

    public static void main(String[] args) {
        //Edge edgeClass = new Edge();
        //System.out.println(edgeClass.getName());
        TestClass testClass = new TestClass();

        testClass.testConnectMethod();

    }

    //Test functionality of connect method

    public void testConnectMethod() {
        loadData();
        listGraphClass.connect(NODEONE, NODETHREE, "aConnection?", 300);

        Set<Edge> edges = listGraphClass.getEdges(NODEONE);

        for(Edge edge : edges) {
            System.out.println("Edge: " + edge.toString());
        }

        System.out.println(listGraphClass.toString());
    }

    private void loadData() {
        listGraphClass.add(NODEONE);
        listGraphClass.add(NODETWO);
        listGraphClass.add(NODETHREE);
    }

    private void removeData() {
        //listGraphClass.remove(NODEONE);

    }

    private void printSet() {
        System.out.println(listGraphClass.existingNodes);
        System.out.println(listGraphClass.getNodes());
    }


}
