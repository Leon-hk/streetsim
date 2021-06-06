package gamelogic;
import java.util.ArrayList;
import java.util.Map;

public class Pathfinding{
//Used for pathfinding of cars, takes Ids of starting and end node, a list of intersections, a list of connections between all nodes and a list of coordinates of all nodes
//returns an arraylist of Points for the car on the grid to move to
    public static ArrayList<Object[]> find_path(String id1, String id2){
        System.out.println("pathfinding now");
        Map<String,Object[]> connections = (Map<String,Object[]>) LogicController.connections[0];
        Map<String,Object[]> nodeconnections = (Map<String,Object[]>) LogicController.connections[1];


        //list of all nodes reachable from current open nodes
        ArrayList<Object[]> open = new ArrayList<>(); //{id,total distance,Arraylist<Point>path}
        //List of all previously travelled nodes
        ArrayList<String> travelled = new ArrayList<>();
        //After the first connection is found, the current path with the least distance will be stored here
        Object[] mindist = new Object[] {null,0,null};
        boolean found = false;


        //Searches nearest intersections around the end node to check against
        ArrayList<String> possible_crossings = new ArrayList<>();
        for(Object obj : nodeconnections.get(id2)){

            Object obs = ((Object[]) obj)[0];
            possible_crossings.add((String)  obs);

        }
        //Searches starting points around the first node
        for(Object obj : nodeconnections.get(id1)){
            String obs = (String)((Object[]) obj)[0]; //id of current node to check
            if(connections.get(obs) != null) {
                ArrayList<Object> buffer = new ArrayList<>();

                buffer.add(obj); //Position of the current node to check
                open.add(new Object[] {(String) obs, 0,buffer}); //adds new possible node to the open / travelled list
                travelled.add((String) obs);
            }
        }

        //main loop, runs until there are no possible shorter routes left or no route was found
        while(open.size() != 0){

            //clone of open to prevent concurrent modification
            ArrayList<Object[]> open2= new ArrayList<>(open);

            //iterates through all currently possible nodes and checks if they are one of the destination nodes
            for(Object[] op : open2){
                if(possible_crossings.contains(op[0])){
                    //if yes, the current path is saved as the shortest path yet
                    mindist = op.clone();
                    ArrayList<Object[]> buffer = new ArrayList<Object[]>((ArrayList<Object[]>)mindist[2]);
                    for(Object conn : (Object[]) connections.get(op[0])[1]){
                        if(((Object[])conn)[0].equals(id2)){
                            buffer.add((Object[]) conn); //adds current node to path
                        }
                    }
                    mindist[2] = buffer;
                    found = true;
                    System.out.println("found");
                    return  (ArrayList<Object[]>) mindist[2];

                }
                else{
                    //Iterates through a list of possible connections around the current node
                    for(Object node2 :  ((Object[]) connections.get((String) op[0])[1])){
                        Object[] node = (Object[]) node2;
                        if(!travelled.contains(node[0])) {
                            //buffer list of travelled path
                            ArrayList<Object[]> buffer = new ArrayList<Object[]>((ArrayList<Object[]>)op[2]);
                            buffer.add(node); //adds current node to path

                            //only considers node if its path is not longer than the currently shortest path (or none is found yet)
                            if(!(found && ((int)op[1] + (int) node[1])> (int) mindist[1])){
                                open.add(new Object[]{node[0], (int) op[1] + (int) node[1], buffer});
                                travelled.add((String) node[0]);
                            }
                        }
                    }
                }
                //removes current node from list to prevent double checking, it´s
                open.remove(op);

            }

        }
        //after no possible routes remain the shortest (or no) path is returned

        System.out.println("done \n");
        return (ArrayList<Object[]>) mindist[2];
    }
}
