// Grupp 365
// Cheng che Huang chhu9071
// Adin Farid adfa8505
// Sara Afshar saaf0625
public class Edge<N> {

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

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    @Override
    public String toString() {
        return "till " + getDestination() + " med " + this.name + " " + "tar " + getWeight();
    }
}
