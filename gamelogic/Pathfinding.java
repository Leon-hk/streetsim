package gamelogic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class Pathfinding {
    //Used for pathfinding of cars, takes Ids of starting and end node, a list of intersections, a list of connections between all nodes and a list of coordinates of all nodes
//returns an arraylist of Points for the car on the grid to move to
    static ArrayList<String> travelled = new ArrayList<>();
    static Map<String, Object[]> connections = (Map<String, Object[]>) LogicController.connections;
    static Map<String, Object[]> nodeconnections = (Map<String, Object[]>) LogicController.nodeconnections;

    static int x = 0;
    static int y = 0;
    static String id2 = "";
    static double maxdist = 0;
    static boolean found = false;
    static ArrayList<Object[]> mindist = null;
    static long startTime= 0;
    static ArrayList<String> possible_crossings = new ArrayList<>();

    public static ArrayList<Object[]> find_path(String id1, String id2a) {
        maxdist = 0;
        startTime = System.nanoTime();
        found = false;
        mindist = null;
        possible_crossings = new ArrayList<>();
        travelled = new ArrayList<>();
        id2 = id2a;
        x = LogicController.cords.get(id2).x;
        y = LogicController.cords.get(id2).y;

        //list of all nodes reachable from current open nodes
        ArrayList<Object[]> open = new ArrayList<>(); //{id,total distance,Arraylist<Point>path}
        //List of all previously travelled nodes

        //After the first connection is found, the current path with the least distance will be stored here




        //Searches nearest intersections around the end node to check against

        for (Object obj : nodeconnections.get(id2)) {

            Object obs = ((Object[]) obj)[0];
            possible_crossings.add((String) obs);

            double dist = Math.sqrt(Math.pow(LogicController.cords.get(obs).x - x, 2) + Math.pow(LogicController.cords.get(obs).y - y, 2));
            if (dist > maxdist) {
                maxdist = dist;

            }


        }
        //Searches starting points around the first node


        search(new Object[]{id1,new ArrayList<Object[]>()});

        return mindist;
    }

    private static void search(Object[] start2) {

        if(!found && (System.nanoTime()-startTime)<100000000){

            Object[] start = start2.clone();
            Map<String,Object[]> distmap = new TreeMap<String,Object[]>();
            for (Object node2 : ((Object[]) connections.get((String) start[0])[1])) {
                Object[] node = (Object[]) node2;
                if (!travelled.contains(node[0])) {
                    travelled.add((String)node[0]);
                    //buffer list of travelled path
                    ArrayList<Object[]> pathbuffer = new ArrayList<Object[]>((ArrayList<Object[]>) start[1]);
                    pathbuffer.add(node); //adds current node to path

                    double dist = Math.sqrt(Math.pow((LogicController.cords.get(node[0]).x - x), 2) + Math.pow((LogicController.cords.get(node[0]).y - y), 2));

                    if(dist<=maxdist ){
                        for(Object conn : (Object[]) connections.get(node[0])[1]){

                            if(((Object[])conn)[0].equals(id2)){

                                pathbuffer.add((Object[]) conn); //adds current node to path
                                mindist = pathbuffer;
                                System.out.println(Arrays.deepToString(mindist.get(1)));

                                found = true;
                            }
                        }



                    }
                    distmap.put(Double.toString(dist), new Object[]{node[0], pathbuffer});

                }
            }
            for(String key : distmap.keySet()){
                search(distmap.get(key));
            }
        }
    }
}