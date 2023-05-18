import java.util.Objects;

public class City<N> {
    private final String name;
    private float x;
    private float y;

    public City(String name, float x, float y) {
        this.name = name;
        this.x = x;
        this.y = y;

    }

    public void setX(float newX) {
        this.x = newX;
    }

    public void setY(float newY) {
        this.y = newY;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof City city) {
            return name.equals(city.name);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + ";" + x + ";" + y;
    }
}
