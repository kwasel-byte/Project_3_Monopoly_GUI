public class Property {

    private String name;
    private int purchasePrice;
    private int rentPrice;
    private String owner;

    private String colorGroup;
    private int houses;
    private int housePrice;

    public Property(String name, int purchasePrice, int rentPrice) {
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.rentPrice = rentPrice;
        this.owner = "No one";

        this.colorGroup = "None";
        this.houses = 0;
        this.housePrice = 50;
    }

    public String getName() {
        return name;
    }

    public int getPurchasePrice() {
        return purchasePrice;
    }

    public int getRentPrice() {
        return rentPrice * (houses + 1);
    }

    public int getBaseRent() {
        return rentPrice;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
       }

    public boolean isOwned() {
        return !owner.equals("No one");
       }

    public String getColorGroup() {
        return colorGroup;
      }

     public void setColorGroup(String colorGroup) {
        this.colorGroup = colorGroup;
      }

    public int getHouses() {
        return houses;
    }

    public int getHousePrice() {
        return housePrice;
      }

    public void setHousePrice(int housePrice) {
        this.housePrice = housePrice;
    }

    public boolean canBuildHouse() {
        return isOwned()
                && !colorGroup.equals("None")
                && houses < 4;
      }

    public void addHouse() {
        if (canBuildHouse()) {
            houses++;
          }
    }

    public void setHouses(int houses) {
        if (houses >= 0 && houses <= 4) {
            this.houses = houses;
         }
     }
}
