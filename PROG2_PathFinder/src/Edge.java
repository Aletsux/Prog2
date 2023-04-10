public class Edge<N> {

    // weight - int?
    private String name;
    private int weight;
    private N destination;

    public Edge(String name, int weight, N destination)  {
        this.name = name;
        this.weight = weight;
        this.destination = destination;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public N getDestination() {
        return destination;
    }

    public String getName() {
        return name;
    }


}
