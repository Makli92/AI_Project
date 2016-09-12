import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

/**
 *
 * @author Makli
 */

public class World_Actor extends Rectangle{
    /// Actor variables
    Color actor_color;
    int actor_num;
    Map<String, World_Building> building_map;
    Point previous_position;
    int steps;
    int knowledge_transactions;
    boolean done;
    
    Pattern wall_pattern;
    Pattern building_pattern;
    Pattern street_pattern;
    Pattern house_pattern;
    
    public World_Actor (Color in_actor_color, int in_actor_num) {
        actor_color = in_actor_color;
        actor_num = in_actor_num;
        building_map = new HashMap<>();
        steps = -1;
        knowledge_transactions = 0;
        done = false;
        
        Initialize_Pattern();
    }
    
    public final boolean Initialize_Pattern() {
        // Regular expressions
        String house = "\\d";
        String wall = "[*]";
        String building = "^(.*?[A-Z])";
        String street = "\\s";
        
        // Initialize Patterns
        wall_pattern = Pattern.compile(wall);
        building_pattern = Pattern.compile(building);
        street_pattern = Pattern.compile(street);
        house_pattern = Pattern.compile(house);
        
        return true;
    }
    
    public void addBuilding(World_Building key_building) {
        building_map.put(key_building.getName(), key_building);
    }
    
    public World_Building getBuilding(String name) {
        return building_map.get(name);
    }
    
    public Color getColor() {
        return actor_color;
    }
    
    public void GetKnowledge() {
        System.out.println( "Actor : " + Integer.toString(actor_num) );
        System.out.println( "Color : " + getColor() );
        System.out.println( "\t Position : " + getLocation() );
        System.out.println( "\t Done : " + done );
        System.out.println( "\t Next : " + getNextBuilding().getName());
        for (String name : building_map.keySet()) {
            System.out.print( "Name : " + building_map.get(name).getName() );
            System.out.print( "\tPosition : " + building_map.get(name).getCoordinates() );
            System.out.print( "\tOrder : " + building_map.get(name).getOrder() );
            System.out.print( "\tIs visited : " + building_map.get(name).isVisited() );
            System.out.println();
        } 
    }
    
    public World_Building getNextBuilding() {
        int max_order = building_map.size();
        String next_building_name = "";
        
        for (String name : building_map.keySet()) {
            World_Building building = building_map.get(name);
            if ( (building.getOrder() != -1) && (!building.isVisited()) ) {
                if ( building.getOrder() <= max_order ) {
                    max_order = building.getOrder();
                    next_building_name = building.getName();
                }
            }
        }
        
        return getBuilding(next_building_name);
    }
    
    public void visitBuilding(String building_name) {
        getBuilding(building_name).setVisited(true);
        
        for (String name : building_map.keySet()) {
            if ( (!building_map.get(name).isVisited()) && (building_map.get(name).getOrder() != -1) && (!name.equals(Integer.toString(actor_num))) ) {
                done = false;
                return;
            }
        }
        
        done = true;
    }
    
    public void learnBuilding(String building_name, Point building_coordinates) {
        getBuilding(building_name).setCoordinates(building_coordinates);
    }
    
    public void setCurrentPosition(Point position) {
        x = position.x;
        y = position.y;
        
        steps++;
    }
    
    public Point getNorthPosition() {
        return new Point(getLocation().x, getLocation().y - 1);
    }
    
    public Point getEastPosition() {
        return new Point(getLocation().x + 1, getLocation().y);
    }
    
    public Point getSouthPosition() {
        return new Point(getLocation().x, getLocation().y + 1);
    }
    
    public Point getWestPosition() {
        return new Point(getLocation().x - 1, getLocation().y);
    }
    
    public void setPreviousPosition(Point point) {
        previous_position = point;
    }
    
    public Point getPreviousPosition() {
        return previous_position;
    }
    
    public boolean isPreviousPosition(Point point, Point previousPoint) {
        return (point.x == previousPoint.x) && (point.y == previousPoint.y);
    }
    
    public void setNextPosition(char [][] world_array, Map<Integer, World_Actor> world_actors) {
        /// Helping variables
        Point[] valid_point_array_ini = new Point[4];
        Point[] valid_point_array_final;
        int valid_points = 0;
        
        // ------- Check neighbor squares ------- 
        String current_point;
        // 1. North
        current_point = Character.toString(world_array[getNorthPosition().y][getNorthPosition().x]);
        valid_points = checkSquare(current_point, getNorthPosition(), valid_point_array_ini, valid_points, world_actors);
        // 2. East
        current_point = Character.toString(world_array[getEastPosition().y][getEastPosition().x]);
        valid_points = checkSquare(current_point, getEastPosition(), valid_point_array_ini, valid_points, world_actors);
        // 3. South
        current_point = Character.toString(world_array[getSouthPosition().y][getSouthPosition().x]);
        valid_points = checkSquare(current_point, getSouthPosition(), valid_point_array_ini, valid_points, world_actors);
        // 4. West
        current_point = Character.toString(world_array[getWestPosition().y][getWestPosition().x]);
        valid_points = checkSquare(current_point, getWestPosition(), valid_point_array_ini, valid_points, world_actors);
        
        // ------- Check if actor is done and home -------
        if ( (this.done) && (this.getLocation().x == getBuilding(Integer.toString(actor_num)).getCoordinates().x) && (this.getLocation().y == getBuilding(Integer.toString(actor_num)).getCoordinates().y)  ) {
            return;
        }
        
        /// ------- Check if any of the nearby squares are home -------
        current_point = Character.toString(world_array[getNorthPosition().y][getNorthPosition().x]);
        if ( current_point.equals(Integer.toString(actor_num)) && done ) {
            visitBuilding(Integer.toString(actor_num));
            setPreviousPosition(getLocation());
            setCurrentPosition(getNorthPosition());
            return;
        }
        // 2. East
        current_point = Character.toString(world_array[getEastPosition().y][getEastPosition().x]);
        if ( current_point.equals(Integer.toString(actor_num)) && done ) {
            visitBuilding(Integer.toString(actor_num));
            setPreviousPosition(getLocation());
            setCurrentPosition(getEastPosition());
            return;
        }
        // 3. South
        current_point = Character.toString(world_array[getSouthPosition().y][getSouthPosition().x]);
        if ( current_point.equals(Integer.toString(actor_num)) && done ) {
            visitBuilding(Integer.toString(actor_num));
            setPreviousPosition(getLocation());
            setCurrentPosition(getSouthPosition());
            return;
        }
        // 4. West
        current_point = Character.toString(world_array[getWestPosition().y][getWestPosition().x]);
        if ( current_point.equals(Integer.toString(actor_num)) && done ) {
            visitBuilding(Integer.toString(actor_num));
            setPreviousPosition(getLocation());
            setCurrentPosition(getWestPosition());
            return;
        }
        
        /// ------- Check if dead end, if not cross out the previous position as a valid option -------
        switch (valid_points) {
            case 1:     /// Dead end
                setPreviousPosition(getLocation());
                setCurrentPosition(valid_point_array_ini[0]);
                
                return;
            case 2:     /// Follow the road
                for ( int array_index = 0; array_index < valid_points; array_index++ ) {
                    if ( !isPreviousPosition(valid_point_array_ini[array_index], getPreviousPosition()) ) {
                        setPreviousPosition(getLocation());
                        setCurrentPosition(valid_point_array_ini[array_index]);
                        
                        return;
                    }
                }
            default:    /// Cross out the previous position
                for ( int array_index = 0; array_index < valid_points; array_index++ ) {
                    if ( (valid_point_array_ini[array_index].x == getPreviousPosition().x) && (valid_point_array_ini[array_index].y == getPreviousPosition().y) ) {
                        valid_point_array_ini[array_index].x = -1;
                        valid_point_array_ini[array_index].y = -1;
                    }
                }
        }
        
        /// ------- Set the final array of choices possible (which means EXCLUDING previous position) -------
        valid_point_array_final = new Point[valid_points - 1];

        for ( int array_index = 0, final_index = 0; array_index <= valid_points - 1; array_index++ ) {
            if ( (valid_point_array_ini[array_index].x != -1) && (valid_point_array_ini[array_index].y != -1) ) {
                valid_point_array_final[final_index] = valid_point_array_ini[array_index];
                final_index++;
            }
        }
        
        /// ------- If next building has no coordinates, random movement,
        ///         else search for best square according to shortest path algorithm -------
        if ( ( getNextBuilding().getCoordinates().x == -1 ) && ( getNextBuilding().getCoordinates().y == -1 ) ) {
            setPreviousPosition(getLocation());
            setCurrentPosition(valid_point_array_final[new Random().nextInt(valid_point_array_final.length)]);
        } else if ( ( getNextBuilding().getCoordinates().x > -1 ) && ( getNextBuilding().getCoordinates().y > -1 ) ) {
            int score = world_array.length + world_array[0].length;
            Point point_to_move = new Point();
            
            for (Point valid_point_array_final_index : valid_point_array_final) {
                int diff_x = Math.abs(getNextBuilding().getCoordinates().x - valid_point_array_final_index.x);
                int diff_y = Math.abs(getNextBuilding().getCoordinates().y - valid_point_array_final_index.y);
                
                if ((diff_x + diff_y) <= score) {
                    score = diff_x + diff_y;
                    point_to_move = valid_point_array_final_index;
                } 
            }
            
            setPreviousPosition(getLocation());
            setCurrentPosition(point_to_move);
        }
    }
    
    private int checkSquare(String name, Point point, Point[] valid_point_array, int valid_points, Map<Integer, World_Actor> world_actors) {
        if ( building_pattern.matcher(name).find() ) {
            if ( getBuilding(name) == null ) {
                addBuilding(new World_Building(name));
            } else if ( getBuilding(name) == getNextBuilding() ) {
                visitBuilding(name);
            }
            learnBuilding(name, point);
        } else if ( street_pattern.matcher(name).find() ) {
            /// Check if any actor exists in this square
            for (Integer num : world_actors.keySet()) {
                if ( (num != actor_num) && ((point.x == world_actors.get(num).getLocation().x) && (point.y == world_actors.get(num).getLocation().y)) ) {
                    /// If so, give and take knowledge
                    takeKnowledge(world_actors.get(num));
                    giveKnowledge(world_actors.get(num));
                }
            }
            
            /// Set valid point
            valid_point_array[valid_points] = point;
            valid_points++;
        }
        
        return valid_points;
    }
    
    public void takeKnowledge(World_Actor wa) {
        for (String name: wa.building_map.keySet()) {
            if ( (!name.equals(Integer.toString(wa.actor_num))) && ((wa.building_map.get(name).getCoordinates().x != -1) && (wa.building_map.get(name).getCoordinates().y != -1)) && ((getBuilding(name) != null) && (getBuilding(name).getOrder() != -1) && ((building_map.get(name).getCoordinates().x == -1) && (building_map.get(name).getCoordinates().y == -1))) ) {
                learnBuilding(name, wa.building_map.get(name).getCoordinates());
                knowledge_transactions++;
            }
        }
    }
    
    public void giveKnowledge(World_Actor wa) {
        for (String name: building_map.keySet()) {
            if ( (!name.equals(Integer.toString(wa.actor_num))) && ((building_map.get(name).getCoordinates().x != -1) && (building_map.get(name).getCoordinates().y != -1)) && ((wa.getBuilding(name) != null) && (wa.getBuilding(name).getOrder() != -1) && ((wa.building_map.get(name).getCoordinates().x == -1) && (wa.building_map.get(name).getCoordinates().y == -1))) ) {
                wa.learnBuilding(name, building_map.get(name).getCoordinates());
                wa.knowledge_transactions++;
            }
        }
    }
}
