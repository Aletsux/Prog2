import javafx.scene.shape.Circle;

import java.util.Objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class City<N> extends Circle {
    private String name = "";
    private double xPos;
    private double yPos;

    public static double RADIUS = 10;

    public City(double x, double y, Paint fill) {
        super(x, y, RADIUS);
        setCenterY(getCenterY() - RADIUS);
        setFill(Color.BLUE);
    }

    public City(String name, double x, double y) {
        this.name = name;
        xPos = x;
        yPos = y;
    }

    public String getName() {
        return name;
    }

    public String getxPos() {
        return "" + xPos;
    }

    public String getyPos() {
        return "" + yPos;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof City city) {
            return name.equals(city.name) && xPos == city.xPos && yPos == city.yPos;
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
