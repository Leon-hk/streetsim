package objects;

import java.awt.*;
import java.util.ArrayList;

public class Lane {
    public Point[] points;
    public ArrayList<Car> cars = new ArrayList<>();
    public Lane(int pointlength){
        points = new Point[pointlength];
    }
}
