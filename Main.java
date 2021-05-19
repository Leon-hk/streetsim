import gamelogic.LogicController;
import ui.Display;

class Main {
    public static void main(String[] args) {
        LogicController.map("map.osm");
        Display.create();
        LogicController logic = new LogicController();
    }
}