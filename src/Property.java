public class Property {
    private String name;
    private int purchasePrice;
    private int rentPrice;
    private String owner;

public Property(String name, int purchasePrice, int rentPrice) {
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.rentPrice = rentPrice;
        this.owner = "No one";
    }
    public String getName() {
        return name;
    }
    public int getPurchasePrice() {
        return purchasePrice;
    }
    public int getRentPrice() {
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

};








