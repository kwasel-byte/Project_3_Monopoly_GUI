import java.util.ArrayList;

public class Board {

    private ArrayList<Property> properties;

    public Board() {
        properties = new ArrayList<>();
        createProperties();
    }

    private void createProperties() {
        addProperty("Kassim City", 60, 2, "Brown");
        addProperty("Dearborn", 60, 4, "Brown");

        addProperty("Chance", 0, 0, "None");

        addProperty("Michigan", 100, 6, "Light Blue");
        addProperty("Troy", 100, 6, "Light Blue");

        addProperty("Community Chest", 0, 0, "None");

        addProperty("LA", 120, 8, "Pink");
        addProperty("Detroit", 140, 10, "Pink");
        addProperty("Buffalo", 140, 10, "Orange");

        addProperty("Chance", 0, 0, "None");

        addProperty("Virginia", 160, 12, "Orange");
        addProperty("Wasel Avenue", 180, 14, "Red");

        addProperty("Community Chest", 0, 0, "None");

        addProperty("Ohio", 180, 14, "Red");
        addProperty("New York State", 200, 16, "Yellow");
        addProperty("Florida", 220, 18, "Yellow");
    }

    private void addProperty(
            String name, int price, int rent, String color) {

        Property property = new Property(name, price, rent);
        property.setColorGroup(color);
        properties.add(property);
    }

    public Property getProperty(int location) {
        return properties.get(location);
    }

    public int getBoardSize() {
        return properties.size();
    }

    public boolean isChance(int location) {
        return properties.get(location)
                .getName().equals("Chance");
    }

    public boolean isCommunityChest(int location) {
        return properties.get(location)
                .getName().equals("Community Chest");
    }

    public boolean ownsCompleteSet(
            String playerName, String colorGroup) {

        if (colorGroup.equals("None")) {
            return false;
        }

        boolean foundProperty = false;

        for (Property property : properties) {

            if (property.getColorGroup().equals(colorGroup)) {
                foundProperty = true;

                if (!property.getOwner().equals(playerName)) {
                    return false;
                }
            }
        }

        return foundProperty;
    }

    public void releaseProperty(String ownerName) {
        for (Property property : properties) {
            if (property.getOwner().equals(ownerName)) {
                property.setOwner("No one");
                property.setHouses(0);
            }
        }
    }
}
