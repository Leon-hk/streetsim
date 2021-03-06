package objects;

import gamelogic.Camera;
import gamelogic.Controls;
import gamelogic.LogicController;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Car {
    int maxdist = 2000;
    boolean reported = false;
    double personalspeed = (Math.random() * (1.1 - 0.6)) + 0.6;
    private double shortest = 5000;
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
            if(lanes.length < lane + 1) {
                lane = lanes.length-1;
            }
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
        System.out.println("short:" + shortest);
        System.out.println("speed:" + speed);
        System.out.println("persspeed:" + personalspeed);

    }
    private Object[] get_shortest(int lane){
        double acc = 1.1;
        shortest = 5000;
        Car shortestc = this;


        for(Car car : lanes[lane].cars) {
            if (car != this) {

                double dist = Math.abs(Math.sqrt(Math.pow(x - car.x, 2) + Math.pow(y - car.y, 2)));
                if ((x < car.x) == dxp && (y < car.y) == dyp) {

                    if(dist<shortest && car.speed < speed + 0.1) {

                        acc = dist / maxdist;
                        shortest = dist;
                        shortestc = car;
                    }
                }
            }
        }
        return new Object[]{acc,shortest,shortestc};
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

                    Object[] data = get_shortest(lane);
                    double acc = (double)data[0];
                    double shortest = (double)data[1];
                    Car shortestc = (Car)data[2];





                    if(shortest < maxdist && shortestc.speed < personalspeed -0.1 && lanes.length > lane +1){
                        lane++;
                        if(!reported) {
                            reported = true;
                            Controls.pause = true;
                            Camera.moveto(x, y);
                        }
                    }
                    else {
                        if(lane>0) {
                            if (get_shortest(lane - 1)[2] == this) {
                                lane--;
                            }
                        }
                        speed = acc * shortestc.speed;
                    }
                    if(speed>personalspeed){
                        speed = personalspeed;
                    }

                    if(!(speed<0.1)) {


                        double scale = currentstreet.maxspeed/3.6/LogicController.TIMEINTERVAL*1000 / Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2)) * speed;
                        move((int) (x + dx * scale), (int) (y + dy * scale));
                    }
                    if(object.color==Color.RED){
                        Camera.moveto(x,y);
                        System.out.println(shortest);
                        System.out.println(shortestc.speed);

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

                if(lanes.length < lane + 1) {
                    lane = lanes.length-1;
                }
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
