package gamelogic;

public class Camera {
    public static double posx = 10000;
    public static double posy = 10000;

    public static double zoom = 0.01;

    public static void move(double x, double y){
        posx += x;
        posy += y;


    }
    public static void moveto(double x, double y){
        posx = x;
        posy = y;

    }
    public static void zoom(double zoom2){

        zoom *= zoom2;
        //TODO make roads bigger or disappear depending on zoom

    }

}
