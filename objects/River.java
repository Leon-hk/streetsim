package objects;

import gamelogic.LogicController;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;


public class River {



    public String name;
    public Point[] nodes;
    public boolean boat;



    public River(String name,int thickness,Point[] nodes,Color color,boolean boat) {

        this.name = name;
        this.nodes = nodes;
        this.boat = boat;
        Polygon poly ;
        Point[] points = new Point[(nodes.length-1) * 4];
        ArrayList<Integer> pointarrx= new ArrayList<>();
        ArrayList<Integer> pointarry= new ArrayList<>();
        int point = 0;
        for(int i = 0; i<nodes.length-1;i++) {
            double angle = Math.atan2((nodes[i+1].y - nodes[i].y), (nodes[i+1].x - nodes[i].x));


            angle += Math.PI / 2;


            int offx = (int) (thickness / 2 * Math.cos(angle));

            int offy= (int) (thickness / 2 * Math.sin(angle));


            points[point] = new Point(nodes[i].x+offx,nodes[i].y+offy);
            point++;
            points[points.length-point] = new Point(nodes[i].x-offx,nodes[i].y-offy);

            points[point] = new Point(nodes[i+1].x+offx,nodes[i+1].y+offy);
            point++;
            points[points.length-point] = new Point(nodes[i+1].x-offx,nodes[i+1].y-offy);

        }




        for(int i = 0; i<points.length;i++){

            if(Line2D.linesIntersect(points[(i==0)? points.length-1:i-1].x,points[(i==0)? points.length-1:i-1].y,points[i].x,points[i].y,points[(i+1)%points.length].x,points[(i+1)%points.length].y,points[(i+2)%points.length].x,points[(i+2)%points.length].y)){

                Point newpoint = intersects(points[(i==0)? points.length-1:i-1],points[i],points[(i+1)%points.length],points[(i+2)%points.length]);


                pointarrx.add(newpoint.x);
                pointarry.add(newpoint.y);
                i++;


            }
            else {


                pointarrx.add(points[i].x);
                pointarry.add(points[i].y);

            }

        }

        poly = new Polygon(pointarrx.stream().mapToInt(i -> i).toArray(),pointarry.stream().mapToInt(i -> i).toArray(),pointarrx.toArray().length);




        LogicController.streets.add(new MyObject(new Polygon[] {poly},1,color));

    }
    public Point intersects(Point p1, Point p2, Point p3, Point p4){

        if(p2.x-p1.x == 0){
            double m2 = (p4.y - p3.y) / (p4.x - p3.x);
            double b2 = p3.y - (m2 * p3.x);
            double y = m2*p1.x + b2;

            return new Point(p1.x,(int)y);

        }
        else if(p4.x-p3.x == 0){
            double m1 = (p2.y - p1.y) / (p2.x - p1.x);
            double b1 = p1.y - (m1 * p1.x);
            double y = m1*p3.x + b1;

            return new Point(p3.x,(int)y);
        }
        else {

            double m1 = (p2.y - p1.y) / (p2.x - p1.x);
            double m2 = (p4.y - p3.y) / (p4.x - p3.x);
            if(m1 == m2){

                return p2;
            }

            double b1 = p1.y - (m1 * p1.x);
            double b2 = p3.y - (m2 * p3.x);

            double x = (b2 - b1) / (m1 - m2);
            double y = m1*x + b1;

            return new Point((int)x,(int)y);



        }


    }
}

