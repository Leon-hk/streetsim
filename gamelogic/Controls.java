package gamelogic;

import objects.Car;
import ui.Display;


import java.awt.*;
import java.awt.event.*;

import java.util.ArrayList;


public class Controls{


    private static Point lastpos = new Point();
    private static boolean pressed = false;
    private static ArrayList<String> keys = new ArrayList<>();


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
            lastpos = new Point(e.getLocationOnScreen());
            System.out.println(e.getLocationOnScreen());

            //TODO get car info on button press
            /*for(Car car : LogicController.carai){
                int ingameposx =
                if(car.x < lastpos.x.)
            }*/

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            pressed = false;
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

            Camera.move(-dx / Camera.zoom,dy / Camera.zoom);

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
