package models;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Mikhail Kadilov
 * The 'Coordinates' class created to work with coordinates
 */
public class Coordinates implements Serializable {
    private Double x;
    private Float y;

    public Coordinates(double x, float y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates(){}

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int compareTo(Coordinates o) {
        if (o == null) {
            return 1;
        }
        int result = Double.compare(this.x, o.x);
        if (result == 0)
            return Double.compare(this.y, o.y);
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coordinates: " +
                "x = " + x +
                ", y = " + y;
    }
}