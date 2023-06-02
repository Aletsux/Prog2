// Edge<N> - Generic type Edge
public class Edge<N> {

    // weight - int?
    private String name;
    private int weight;
    private N destination;

    public Edge(String name, int weight, N destination) {
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

    public String getDestinationName() {
        City city = (City) destination;
        return city.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    @Override
    public String toString() {
        return  "to " + getDestinationName()+ " by " + this.name + " " + "takes " + getWeight();
    }
}
