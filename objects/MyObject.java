package objects;

import java.awt.*;

public class MyObject {
    public Polygon[] polys;
    public int layer;
    public Color color;

    public MyObject(Polygon[] polys,int layer,Color color){
        this.polys = polys;
        this.layer = layer;
        this.color = color;
    }


}
