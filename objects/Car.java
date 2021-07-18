package objects;

import gamelogic.Camera;
import gamelogic.Controls;
import gamelogic.LogicController;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Car {

    double reactiontime = (Math.random() * ((1.3 - 0.8) + 1)) + 0.8;
    double breakingfactor = Math.random() * ((0.1-0.05)) + 0.05;
    double acceleration = Math.random() * ((0.05-0.02)) + 0.02;
    double personalspeed = (Math.random() * ((1.1 - 0.6))) + 0.6;
    double speed = personalspeed;
    final int[] xa  = new int[] {-70,70,70,-70};
    final int[] ya  = new int[] {-70,-70,70,70};
    Polygon poly1 = new Polygon(xa, ya, 4);
    public int x = 0;
    public int y = 0;
    private boolean dxp = false;
    private boolean dyp = false;

    ArrayList<Object[]> path;
    Street currentstreet;
    int streetindex = 0;
    public int lane = 0;
    int dir = 0;
    Lane[] lanes;
    int nodeinstreet = 0;


    private Map<String,Point> cords = (Map<String,Point>) LogicController.cords;

    public MyObject object;
    public Car(String origin,String destination){


        path = gamelogic.Pathfinding.find_path(origin,destination);

        if(path!=null) {

            currentstreet = (Street) path.get(streetindex)[2];
            streetindex++;
            nodeinstreet = Arrays.asList(currentstreet.nodes).indexOf(cords.get(origin));

            if ((int)(path.get(streetindex-1)[4])-nodeinstreet >0) {
                lanes = currentstreet.forward;

                dir = 1;
            } else {
                lanes = currentstreet.backward;
                dir = -1;
            }

            lane = 0;
            if(lanes.length != 0){
            lanes[lane].cars.add(this);



            x = cords.get(origin).x;
            y = cords.get(origin).y;

            object = new MyObject(new Polygon[]{poly1}, 1, new Color(242, 116, 255));
            move(x, y);

            LogicController.cars.add(object);
        }}

    }

    public void move(int posx,int posy){
        x = posx;

        y = posy;
        int[] xp = xa.clone();
        int[] yp = ya.clone();
        for(int i= 0;i<xp.length;i++){
            xp[i]+= posx;

        }
        for(int i= 0;i<yp.length;i++){
            yp[i]+= posy;

        }
        object.polys = new Polygon[] {new Polygon(xp,yp,object.polys[0].npoints)};

    }
    public void print(){

        System.out.println("");
        System.out.println();
        System.out.println("pos:" + x + "/" + y);
        System.out.println("lane:" + lane);
        System.out.println("nodeinstreet:" + nodeinstreet + "/" + lanes[0].points.length + "/" + (int)(path.get(streetindex-1)[4]));
        System.out.println("streetind:" + streetindex + "/" + path.size());
        System.out.println("path:" + Arrays.asList(currentstreet.nodes).indexOf(cords.get(path.get(streetindex-1)[0])) + "->" + path.get(streetindex-1)[4]);
        System.out.println("dir:"+ dir);
    }
    public boolean update(){
        if(path==null || lanes.length == 0){
            LogicController.cars.remove(object);
            return false;
        }

        if(streetindex < path.size()+1){

            if(nodeinstreet<lanes[0].points.length && nodeinstreet > -1 && nodeinstreet != (int)(path.get(streetindex-1)[4]) + dir){

                int dx = lanes[lane].points[nodeinstreet].x - x;
                int dy = lanes[lane].points[nodeinstreet].y - y;


                if((dx > 0 != dxp) || (dy >0 != dyp)  || (dx == 0) || (dy == 0) ){

                    nodeinstreet += dir;



                    if(nodeinstreet<lanes[0].points.length && nodeinstreet > -1) {
                        dx = lanes[lane].points[nodeinstreet].x - x;
                        dy = lanes[lane].points[nodeinstreet].y - y;

                        if (dx > 0) {
                            dxp = true;
                        } else {
                            dxp = false;
                        }
                        if (dy > 0) {
                            dyp = true;
                        } else {
                            dyp = false;
                        }
                    }

                    update();
                }
                else {
                    double scale = currentstreet.maxspeed/3.6/LogicController.TIMEINTERVAL*1000 / Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2)) * speed;

                    double acc = 1;

                    for(Car car : lanes[lane].cars){
                        if(car!=this) {

                            double dist = Math.abs(Math.sqrt(Math.pow(x - car.x, 2) + Math.pow(y - car.y, 2)));
                            if((x<car.x)==dxp && (y<car.y)==dyp){
                            if (dist < 3000) {
                                if(dist<1){dist=1;}




                                if (lane < lanes.length - 1) {
                                    lane++;
                                } else {
                                    double save = acc;
                                    acc = -(3000-dist)/3000 * speed-car.speed;

                                }
                            }
                            else{
                                double save = acc;
                                if(acc>0) {
                                    if (dist < 5000) {

                                        acc = (dist - 1000) / 4000;
                                    } else {
                                        acc = 1;
                                    }
                                    if (acc > save) {
                                        acc = save;
                                    }
                                }
                            }
                            if(object.color==Color.red){
                                System.out.println("\n infront:"+Boolean.toString((x<car.x)==dxp && (y<car.y)==dyp));
                                System.out.println(" dist:"+ dist);
                                System.out.println("braking:"+ (1000-dist)/1000 * breakingfactor * LogicController.TIMEINTERVAL/100);
                                System.out.println("acc:" + acceleration*LogicController.TIMEINTERVAL/100);

                            }
                        }
                        }
                    }
                    speed+=   acc * acceleration * LogicController.TIMEINTERVAL/100;
                    if(speed>personalspeed){
                        speed = personalspeed;
                    }

                    if(speed<0){
                        speed = 0;
                    }

                    move((int) (x + dx * scale), (int) (y + dy * scale));
                    if(object.color==Color.RED){
                        Camera.moveto(x,y);
                    }
                }
                //move(lanes[lane].points[nodeinstreet].x,lanes[lane].points[nodeinstreet].y);


            }
            else{
                if(streetindex >= path.size()){
                    LogicController.cars.remove(object);
                    return false;
                }

                currentstreet = (Street) path.get(streetindex)[2];
                lanes[lane].cars.remove(this);
                if((int)(path.get(streetindex)[3]) == 1){
                    lanes = currentstreet.forward;
                    dir = 1;
                }
                else{
                    lanes = currentstreet.backward;
                    dir = -1;
                }
                nodeinstreet = Arrays.asList(currentstreet.nodes).indexOf(cords.get(path.get(streetindex-1)[0]));
                streetindex++;


                lane = 0;
                lanes[lane].cars.add(this);

                int dx = lanes[lane].points[nodeinstreet].x - x;
                int dy = lanes[lane].points[nodeinstreet].y - y;

                if(dx >0) {dxp=true;}else{dxp=false;}
                if(dy >0) {dyp=true;}else{dyp=false;}

                update();

            }
        }
        else{

            //reached end of destination
            LogicController.cars.remove(object);
            object = null;

            return false;

        }
    return true;
    }

}
