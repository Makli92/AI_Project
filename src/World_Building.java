import java.awt.Point;

/**
 *
 * @author Makli
 */
public class World_Building {
    private Point coordinates;
    private String name;
    private boolean is_visited;
    private int order;
    
    public World_Building(String in_name, int in_order) {
        coordinates = new Point(-1, -1);
        name = in_name;
        order = in_order;
        is_visited = false;
    }
    
    public World_Building(String in_name) {
        coordinates = new Point(-1, -1);
        name = in_name;
        order = -1;
        is_visited = false;
    }
    
    public void setCoordinates(Point in_coordinates) {
        coordinates.x = in_coordinates.x;
        coordinates.y = in_coordinates.y;
    }
    
    public void setVisited(boolean in_is_visited) {
        is_visited = in_is_visited;
    }
    
    public Point getCoordinates() {
        return coordinates;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isVisited() {
        return is_visited;
    }
    
    public int getOrder() {
        return order;
    }
}
