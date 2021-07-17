package gamelogic;
import objects.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class LogicController{


    public static int TIMEINTERVAL = 100;
    public static ArrayList<MyObject> terrain = new ArrayList<>();
    public static ArrayList<MyObject> streets = new ArrayList<>();
    public static ArrayList<MyObject> cars = new ArrayList<>();

    public static ArrayList<Car> carai = new ArrayList<>();

    public static ArrayList<MyObject>[] visible = new ArrayList[] {new ArrayList(),new ArrayList(),new ArrayList()};
    public static MapParser handler;
    //public static Object[] connections = new Object[]{};
    public static Map<String,Point> cords = Collections.emptyMap();
    public static Map<String,Object[]> connections = Collections.emptyMap();
    public static Map<String,Object[]> nodeconnections = Collections.emptyMap();

    private boolean running = false;


    Thread controls = new Thread("Logic:controls"){
        public void run(){
            while(running){
                Controls.update();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    Thread caraiThead = new Thread("Logic:carai"){
        public void run() {
            while (running) {
                if (!Controls.pause) {
                    for (Car car : new ArrayList<Car>(carai)) {
                        if (!car.update()) {
                            carai.remove(car);
                        }

                    }
                    try {
                        Thread.sleep(TIMEINTERVAL - 70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    Thread visibilty = new Thread("Logic:visibility"){
        public void run(){
            while (running){
                visible = new ArrayList[]{terrain, streets, cars};
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Thread car_spawning = new Thread("Logic:car_spawning"){
        public void run(){
            while (running){
                Car_Spawning.loop();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    public static void map(String filename){

        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {

            SAXParser saxParser = factory.newSAXParser();

            handler = new MapParser();
            saxParser.parse(filename, handler);

            connections =(Map<String, Object[]>) handler.getConnections()[0];
            nodeconnections = (Map<String, Object[]>) handler.getConnections()[1];
            cords = (Map<String, Point>) handler.getConnections()[2];


        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }


    public LogicController(){
        start();
    }
    public synchronized void start(){
        this.running = true;
        caraiThead.start();
        controls.start();
        visibilty.start();
        car_spawning.start();

    }
    public synchronized void stop(){
        this.running = false;
        try {
            caraiThead.join();
            controls.join();
            visibilty.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
