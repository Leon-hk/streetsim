package objects;

import gamelogic.LogicController;

import java.awt.*;

public class Terrain {
    public String type;
    public MyObject object;
    private Color color;
    public Terrain(String type, Point[] nodes){
        boolean draw = false;
        this.type = type;
        switch(this.type) {

            case "residential":
                this.color = new Color(255, 152, 147);
                draw = true;
                break;

            case "farmland":
                this.color = new Color(125, 57, 9);
                draw = true;
                break;

            case "forest":
                this.color = new Color(0, 255, 0);
                draw = true;
                break;

            case "industrial":
                this.color = new Color(0, 0, 255);
                draw = true;
                break;
            //case "a":
                //this.color = new Color(255, 0, 255);
                //break;

            default:
                this.color = Color.white;
                draw = false;
                break;
        }

        if(draw) {
            int[] xs = new int[nodes.length];
            int[] ys = new int[nodes.length];
            for (int i = 0; i < nodes.length; i++) {
                xs[i] = nodes[i].x;
                ys[i] = nodes[i].y;
            }
            Polygon poly = new Polygon(xs, ys, nodes.length);
            LogicController.terrain.add(new MyObject(new Polygon[]{poly}, 1, this.color));
        }
    }
}
