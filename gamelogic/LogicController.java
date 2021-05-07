package gamelogic;
import objects.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class LogicController implements Runnable{



    public static ArrayList<MyObject> terrain = new ArrayList<>();
    public static ArrayList<MyObject> streets = new ArrayList<>();
    public static ArrayList<MyObject> cars = new ArrayList<>();

    public static ArrayList<Car> carai = new ArrayList<>();

    public static ArrayList<MyObject>[] visible = new ArrayList[] {new ArrayList(),new ArrayList(),new ArrayList()};
    public static MapParser handler;
    public static Object[] connections = new Object[]{};

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
                for (Car car : new ArrayList<Car>(carai)) {
                    if (!car.update()) {
                        carai.remove(car);
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


    public static void map(String filename){

        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {

            SAXParser saxParser = factory.newSAXParser();

            handler = new MapParser();
            saxParser.parse(filename, handler);
            connections = handler.getConnections();

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void run() {
        while(running) {



            //TODO update cars and roads for zoom correction
        }
    }
    public LogicController(){
        start();
    }
    public synchronized void start(){
        caraiThead.start();
        controls.start();
        visibilty.start();
        this.running = true;
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
