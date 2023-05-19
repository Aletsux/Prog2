import javafx.scene.shape.Circle;

import java.util.Objects;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class City <N> extends Circle {

    public City(double x, double y, double radius, Paint fill) {
        super(x, y, radius);
        setFill(Color.BLUE);
    }




//    @Override
//    public boolean equals (Object other) {
//        if (other instanceof City city) {
////            return name.equals(city.name);
//        } else {
//            return false;
//        }
//    }
//    @Override
//    public int hashCode(){
//        return Objects.hash(name);
//    }
//
//    @Override
//    public String toString() {
//        return name;
//    }
}
