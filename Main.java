import gamelogic.LogicController;

import objects.Car;
import ui.Display;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


class Main{

    public static void main(String[] args){
        long startTime = System.nanoTime();


        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/1000000;  //divide by 1000000 to get milliseconds.
        System.out.print("Zeit: ");
        System.out.println(duration);


        LogicController.map("map.osm");
        Display.create();
        LogicController logic = new LogicController();
        LogicController.carai.add( new Car("884211829","4935935076"));


            System.out.println("new car");
            for(int i = 0;i<100;i++) {
                LogicController.carai.add(new Car("8313271112", "5167797079"));
                LogicController.carai.add(new Car("8313271112", "4911327313"));
                LogicController.carai.add(new Car("4911327296", "4911327300"));
            }





    }
}