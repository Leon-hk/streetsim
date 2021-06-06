import gamelogic.LogicController;
import ui.Display;
import objects.Car;

class Main {
    public static void main(String[] args) {
        LogicController.map("map.osm");
        Display.create();
        LogicController logic = new LogicController();


        for(int i = 0;i<100;i++) {
            LogicController.carai.add(new Car("8313271112", "5167797079"));
            LogicController.carai.add(new Car("8313271112", "4911327313"));
            LogicController.carai.add(new Car("4911327296", "4911327300"));
        }
    }
}