import java.util.ArrayList;

public class Board {
    private ArrayList<Property> properties;

    public Board() {
        properties = new ArrayList<Property>();
        createProperties();
    }
    private void createProperties(){
        properties.add(new Property("Kassim City", 60, 2));
        properties.add(new Property("Dearborn", 60, 4));
        properties.add(new Property("Michigan", 100, 6));
        properties.add(new Property("Troy", 100, 6));
        properties.add(new Property("LA", 120, 8));
        properties.add(new Property("Detroit", 140, 10));
        properties.add(new Property("Buffalo", 140, 10));
        properties.add(new Property("Virginia", 160, 12));
        properties.add(new Property("Wasel Avenue", 180, 14));
        properties.add(new Property("Ohio", 180, 14));
        properties.add(new Property("New York State", 200, 16));
        properties.add(new Property("Florida", 220, 18));
    }
    public Property getProperty(int location) {
        return properties.get(location);
    }
    public int getBoardSize() {
        return properties.size();
    }

}

