package objects;

import gamelogic.LogicController;

import java.awt.*;
import java.util.Queue;

public class Terrain {
    public String type;
    public MyObject object;
    private Color color;
    public Terrain(String type, Point[] nodes){
        boolean draw = true;
        this.type = type;
        switch(this.type) {

            case "residential":

                this.color = new Color(201, 201, 201);

                break;

            case "trees":
                System.out.println("trees");
                break;

            case "farmland":
            case "brownfield":
                this.color = new Color(125, 57, 9);

                break;

            case "forest":
            case "wood":
                System.out.println("test");
                this.color = new Color(68, 113, 0);
                break;

            case "industrial":
            case "commercial":
            case "retail":
                this.color = new Color(64, 64, 64);

                break;
            case "water":
            case "basin":
            case "reservoir":
                this.color = new Color(37, 59, 212);

                break;

            case "wetland":
                this.color = new Color(0, 102, 7);

                break;
            case "grassland":
            case "heath":
            case "grass":
            case "allotments":
            case "orchard":
            case "cemetry":
            case "animal_keeping":
                this.color = new Color(1, 92, 7);

                break;
            case "scrub":
                this.color = new Color(46, 90, 0);

                break;
            case "construction":
                this.color = new Color(255, 185, 0);

                break;
            case "farmyard":
                this.color = new Color(127, 95, 0);

                break;

            case "meadow":
                this.color = new Color(26, 94, 0);

                break;
            case "plant_nursery":
                this.color = new Color(219, 77, 77);

                break;
            case "beach":
                this.color = new Color(219, 77, 77);

                break;
            case "bare_rock":
            case "cliff":
            case "scree":
            case "valley":
                this.color = new Color(50, 50, 50);
                break;
            case "sand":
                this.color = new Color(213, 187, 5);
                break;



            case "a":
                this.color = Color.white;
                draw=false;
                break;

            default:

                //System.out.println(this.type);
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
