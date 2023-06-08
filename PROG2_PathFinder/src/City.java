import javafx.scene.shape.Circle;

import java.util.Objects;

import javafx.scene.paint.Color;

// Grupp 365
// Cheng che Huang chhu9071
// Adin Farid adfa8505
// Sara Afshar saaf0625
public class City<N> extends Circle {
    private static double RADIUS = 10;
    private String name = "";
    private double xpos;
    private double ypos;
    private boolean blue;

    public City(String name, double x, double y) {
        super(x, y, RADIUS);
        setCenterY(getCenterY() - RADIUS);
        this.name = name;
        xpos = x;
        ypos = y;
        setFill(Color.BLUE);
        blue = true;
    }

    public void toggleColor() {
        blue = !blue;
        if (blue) {
            setFill(Color.BLUE);
        } else {
            setFill(Color.RED);
        }
    }

    public String getName() {
        return name;
    }

    public String getxPos() {
        return "" + xpos;
    }

    public String getyPos() {
        return "" + ypos;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof City) {
            City c = (City) other;
            return name.equals(c.name) && xpos == c.xpos && ypos == c.ypos;
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
        return name + ";" + getxPos() + ";" + getyPos();
    }
}
