package gamelogic;

import objects.Car;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class Car_Spawning{
    public static int carmax = 10000;
    private static ArrayList<String> keyset = new ArrayList<>(LogicController.connections.keySet());
    private static Random r = new Random();
    public static void new_car(){

        String id1 = keyset.get(r.nextInt(keyset.size()));
        String id2 = keyset.get(r.nextInt(keyset.size()));

        LogicController.carai.add(new Car(id1, id2));


    }
    public static void loop(){

        //TODO daytime related spawning
        while(LogicController.carai.size() < carmax){
            new_car();

        }

    }


}
