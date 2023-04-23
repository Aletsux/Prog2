import java.util.Objects;

public class City {
    private final String name;

    public City(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals (Object other) {
        if (other instanceof City city) {
            return name.equals(city.name);
        } else {
            return false;
        }
    }
    @Override
    public int hashCode(){
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
