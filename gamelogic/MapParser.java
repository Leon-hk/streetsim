package gamelogic;

import objects.Street;
import objects.Terrain;
import org.w3c.dom.Element;
import org.xml.sax.helpers.DefaultHandler;

// SAX Parser imports
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.management.MalformedObjectNameException;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MapParser extends DefaultHandler{
    private StringBuilder currentValue = new StringBuilder();
    ArrayList<Street> streets;
    ArrayList<Terrain> terrain;
    Map<String,Terrain> terrain_cache = new HashMap<>();
    ArrayList<String> allowed = new ArrayList<>();
    double maxlat;
    double maxlon;
    double height;
    double width;

    private Map<String,Object[]> connections = new HashMap<>();
     Map<String,Point> cords = new HashMap<>();
    private Map<String,Object[]> nodeconnections = new HashMap<>();
    Map<String, double[]> map = new HashMap<>();
    private ArrayList<String> ids;
    boolean way = false;
    boolean relation = false;
    private ArrayList<String> members;

    int lanesback = 0;
    int lanesforw = 0;
    int speed = 0;
    String name;
    String id;
    ArrayList<Point> nodes;
    String type;
    String landuse;

    public ArrayList<Street> getStreets() {
        return streets;
    }

    public ArrayList<Terrain> getTerrain() {
        return terrain;
    }

    public Object[] getConnections(){
        return new Object[] {connections,nodeconnections,cords};
    }

    public MapParser() {

    }

    @Override
    public void startDocument() {
        allowed.add("residential");
        allowed.add("tertiary");
        allowed.add("secondary");
        allowed.add("primary");
        allowed.add("living_street");
        allowed.add("primary_link");
        streets = new ArrayList<>();
        terrain = new ArrayList<>();
    }

    @Override
    public void endDocument() {


        nodeconnections = new HashMap<>();

        Iterator<Map.Entry<String, Object[]>> entries = connections.entrySet().iterator();
        ArrayList<String> toremove = new ArrayList<>();
        while (entries.hasNext()) {

            Map.Entry<String, Object[]> entry = entries.next();

            Object[] obj = (Object[])(entry.getValue()[1]);
            nodeconnections.put(entry.getKey(), obj);
            if(obj.length == 0 | (int) entry.getValue()[0] < 1){
                toremove.add(entry.getKey());
            }

        }
        for(String rem: toremove){
            connections.remove(rem);
        }

    }
    ArrayList<String> tags = new ArrayList<>();
    @Override
    public void startElement(
            String uri,
            String localName,
            String qName,

            Attributes attributes) {
        // reset the tag value and ids
        currentValue.setLength(0);

        //TODO landscape / landuse

        if (qName.equalsIgnoreCase("bounds")) {
            //TODO deformation correction
            double minlat = Double.parseDouble(attributes.getValue("minlat"));
             maxlat = Double.parseDouble(attributes.getValue("maxlat"));
            double minlon = Double.parseDouble(attributes.getValue("minlon"));
             maxlon = Double.parseDouble(attributes.getValue("maxlon"));


            height = (maxlon - minlon) * 11113000;
            width = (maxlat - minlat) * 7144000;
        }
        // start of loop
        if (qName.equalsIgnoreCase("node")) {
            map.put(attributes.getValue("id"), new double[] {Double.parseDouble(attributes.getValue("lon")), Double.parseDouble(attributes.getValue("lat"))});
            connections.put(attributes.getValue("id"), new Object[]{0,new Object[]{}});


        }

        if (qName.equalsIgnoreCase("way")) {
            name = "";
            id = attributes.getValue("id");
            nodes = new ArrayList<>();
            type = "";
            landuse = "a";
            ids = new ArrayList<>();
            way = true;

            lanesback = 0;
            lanesforw = 0;
            speed = 50;
        }

        if (qName.equalsIgnoreCase("tag") && way) {
            if (!tags.contains(attributes.getValue("k"))) {
                tags.add(attributes.getValue("k"));
            }
            if (attributes.getValue("k").equals("name")) {
                name = attributes.getValue("v");
            }
            else if (attributes.getValue("k").equals("oneway")){
                if(attributes.getValue("v").equals("yes")){
                    lanesforw = 1;
                }

            }

            else if(attributes.getValue("k").equals("lanes:backward")){
                lanesback = Integer.valueOf(attributes.getValue("v"));

            }
            else if(attributes.getValue("k").equals("lanes:forward")){
                lanesforw = Integer.valueOf(attributes.getValue("v"));

            }
            else if(attributes.getValue("k").equals("maxspeed")){
                speed = Integer.valueOf(attributes.getValue("v"));
            }

            else if (attributes.getValue("k").equals("highway")) {
                type = attributes.getValue("v");
            }
            else if (attributes.getValue("k").equals("junction")){
                if(attributes.getValue("v").equals("roundabout")){
                    lanesforw = 1;
                }
            }

        }

        if (qName.equalsIgnoreCase("tag") && (way || relation)) {
            if (attributes.getValue("k").equals("landuse") || attributes.getValue("k").equals("area")) {
                landuse = attributes.getValue("v");

            } else if (attributes.getValue("k").equals("landcover") || attributes.getValue("k").equals("area")) {
                landuse = attributes.getValue("v");

            } else if (attributes.getValue("k").equals("natural") || attributes.getValue("k").equals("area")) {
                landuse = attributes.getValue("v");

            }
        }



        if (qName.equalsIgnoreCase("nd") && way) {

            int rely = (int) (height/2 -((maxlat - map.get(attributes.getValue("ref"))[1])*11113000));
            int relx = (int) (width/2 - ((maxlon - map.get(attributes.getValue("ref"))[0])*7144000));

            Point point = new Point(relx, rely);
            ids.add(attributes.getValue("ref"));
            nodes.add(point);
            cords.put(attributes.getValue("ref"), point);
        }

        if (qName.equalsIgnoreCase("relation")) {
            relation = true;
            landuse = "";
            members = new ArrayList<>();
        }

        if (qName.equalsIgnoreCase("member") && relation) {
            if (attributes.getValue("role").equals("outer")) {
                members.add(attributes.getValue("ref"));
            }
        }
    }

    @Override
    public void endElement(String uri,
                           String localName,
                           String qName) {
        if (qName.equalsIgnoreCase("way")) {
            if (allowed.contains(type)) {
                if(lanesforw == 0 && lanesback == 0){
                    lanesforw = 1;
                    lanesback = 1;
                }
                Street street = new Street(name, nodes.toArray(new Point[0]), type,speed,lanesforw,lanesback);
                streets.add(street);

                ArrayList<Integer> dist = new ArrayList<>();
                for (int i = 0; i < nodes.size()-1; i++) {

                    int d = (int) Math.sqrt(Math.pow(Math.abs(nodes.get(i).x - nodes.get(i+1).x),2 ) * Math.pow(Math.abs(nodes.get(i).y - nodes.get(i+1).y),2));

                    dist.add(d);
                }



                for (int i = 0; i < nodes.size(); i++) {
                    ArrayList<Object[]> connected = new ArrayList<>();

                    int d = 0;

                    //check for oneway -> if lanesforw or lanesbackw is 0 dont make connections in that direction
                    for(int j = i+1;j<nodes.size() && lanesforw != 0;j++){
                        d += dist.get(j-1);
                        connected.add( new Object[] {ids.get(j),d,street,1,j});

                    }
                    d=0;

                    for(int j = i-1;j>= 0 && lanesback != 0;j--){

                        d += dist.get(j);
                        connected.add( new Object[] {ids.get(j),d,street,-1,j});

                    }

                    Object[] connectedold = (Object[]) connections.get(ids.get(i))[1];

                    Object[] newarray = new Object[connectedold.length + connected.size()];
                    System.arraycopy(connectedold,0,newarray,0,connectedold.length);
                    System.arraycopy(connected.toArray(),0,newarray,connectedold.length,connected.size());

                    connections.put(ids.get(i),new Object[] {ids.size()-1 + (int) connections.get(ids.get(i))[0], newarray});

                }
            } else if (!landuse.equals("a")) {
                terrain.add(new Terrain(landuse, nodes.toArray(new Point[0])));
            } else {
                terrain_cache.put(id, new Terrain("unset", nodes.toArray(new Point[0])));
            }
        } else if (qName.equalsIgnoreCase("relation")) {
            relation = false;
            if (!landuse.equals("")) {
                Point[] nodecache = new Point[0];
                for(String member: members) {
                    try {
                        if (!landuse.equals("water")) {
                            terrain.add(new Terrain(landuse, terrain_cache.get(member).nodes));
                        } else {
                            Point[] addnodes = terrain_cache.get(member).nodes;
                            Point[] newnodes = new Point[nodecache.length + addnodes.length];
                            System.arraycopy(nodecache, 0, newnodes, 0, nodecache.length);
                            System.arraycopy(addnodes, 0, newnodes, nodecache.length, addnodes.length);
                            nodecache = newnodes;
                        }
                    } catch (Exception e) {}
                }
                if(landuse.equals("water")) {
                    terrain.add(new Terrain(landuse, nodecache));
                }
            }
        }

    }

    // http://www.saxproject.org/apidoc/org/xml/sax/ContentHandler.html#characters%28char%5B%5D,%20int,%20int%29
    // SAX parsers may return all contiguous character data in a single chunk,
    // or they may split it into several chunks
    @Override
    public void characters(char ch[], int start, int length) {

        // The characters() method can be called multiple times for a single text node.
        // Some values may missing if assign to a new string

        // avoid doing this
        // value = new String(ch, start, length);

        // better append it, works for single or multiple calls
        currentValue.append(ch, start, length);

    }
}
