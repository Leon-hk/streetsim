package gamelogic;

import objects.Car;
import ui.Display;


import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;


public class Controls{


    private static Point lastpos = new Point();
    private static boolean pressed = false;
    private static boolean rpressed = false;
    private static ArrayList<String> keys = new ArrayList<>();
    public static boolean pause = false;

    public static KeyListener key = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            keys.add(Integer.toString(e.getKeyCode()));

        }

        @Override
        public void keyReleased(KeyEvent e) {
            while(keys.remove(Integer.toString(e.getKeyCode()))){}
        }
    };

    public static MouseListener mouse = new MouseListener(){
        @Override
        public void mouseClicked(MouseEvent e) {


        }

        @Override
        public void mousePressed(MouseEvent e) {
            pressed = true;
            if(e.getButton()==3){
                rpressed=true;
            }

            lastpos = new Point(e.getLocationOnScreen());

            //TODO get car info on button press
            /*for(Car car : LogicController.carai){
                int ingameposx =
                if(car.x < lastpos.x.)
            }*/

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            pressed = false;
            rpressed = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    };

    public static MouseWheelListener wheel = e -> {
        if(e.getWheelRotation() == 1) {
            Camera.zoom(0.5);

            int mouseinwindowx = MouseInfo.getPointerInfo().getLocation().x - Display.display.frame.getX() - Display.display.frame.getWidth()/2;
            int mouseinwindowy = MouseInfo.getPointerInfo().getLocation().y - Display.display.frame.getY() - Display.display.frame.getHeight()/2-28;

            Camera.move((-mouseinwindowx/Camera.zoom)/2,(mouseinwindowy/Camera.zoom)/2);




        }
        else{
            Camera.zoom(2);
            int mouseinwindowx = MouseInfo.getPointerInfo().getLocation().x - Display.display.frame.getX() - Display.display.frame.getWidth()/2;
            int mouseinwindowy = MouseInfo.getPointerInfo().getLocation().y - Display.display.frame.getY() - Display.display.frame.getHeight()/2-28;

            Camera.move(mouseinwindowx/Camera.zoom,-mouseinwindowy/Camera.zoom);

        }

    };

    public static void update() {

        if(pressed){
            int dx = MouseInfo.getPointerInfo().getLocation().x -lastpos.x;
            int dy = MouseInfo.getPointerInfo().getLocation().y -lastpos.y;

            lastpos = MouseInfo.getPointerInfo().getLocation();
            //reverse Coordinate grabbing

            if(rpressed) {
                rpressed = false;

                Point corner = Display.display.getLocationOnScreen();
                Point middle = new Point(corner.x + Display.frame.getWidth() / 2, corner.y + Display.frame.getHeight() / 2);
                Point posinwin = new Point(lastpos.x - middle.x, lastpos.y - middle.y);
                Point ingame = new Point((int) (posinwin.x / Camera.zoom + Camera.posx), (int) (-posinwin.y / Camera.zoom + Camera.posy));
                ArrayList<Car> carai2 = (ArrayList<Car>) LogicController.carai.clone();

                for(Car car:carai2){

                    if(Math.abs(car.x-ingame.x)<300 && Math.abs(car.y-ingame.y)<300){

                        car.print();
                        if(car.object.color!=Color.red) {
                            car.object.color = Color.red;
                        }else{
                            car.object.color=Color.pink;
                        }
                    }
                }

            }

            Camera.move(-dx / Camera.zoom,dy / Camera.zoom);

        }
        if(keys.contains("32")){
            keys.remove("32");

            if(pause){
                pause=false;

            }
            else{
                pause=true;

            }

        }
        if (keys.contains("87")) {

            Camera.move(0, 7 / Camera.zoom);
        }

        if (keys.contains("65")) {

            Camera.move(-7 / Camera.zoom, 0);
        }

        if (keys.contains("83")) {

            Camera.move(0, -7 / Camera.zoom);
        }

        if (keys.contains("68")) {

            Camera.move(7 / Camera.zoom, 0);
        }

    }



}
