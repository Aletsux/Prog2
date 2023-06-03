import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

import java.util.Objects;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class City<N> extends Circle {
    private String name = "";
    private double xPos;
    private double yPos;
    private boolean blue = true;

    public static double RADIUS = 10;

    public City(double x, double y) {
        super(x, y, RADIUS);
        setCenterY(getCenterY() - RADIUS);
        setFill(Color.BLUE);
    }

    public City(String name, double x, double y) {
        super(x, y, RADIUS);
        setCenterY(getCenterY() - RADIUS);
        this.name = name;
        xPos = x;
        yPos = y;
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
        return "" + xPos;
    }

    public String getyPos() {
        return "" + yPos;
    }

    @Override
    public boolean equals(Object other) {
//        if (other instanceof City city) {
//            return name.equals(city.name) && xPos == city.xPos && yPos == city.yPos;
//        } else {
//            return false;
//        }
//        if (other instanceof City city) {
//            return name.equals(city.name) && xPos == city.xPos && yPos == city.yPos && clickCount == city.clickCount;
//        } else {
//            return false;
//        }

        if (other instanceof City) {
            City c = (City) other;
            return name.equals(c.name) && xPos == c.xPos && yPos == c.yPos;
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
