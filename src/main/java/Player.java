public class Player {
    private String name;
    private int money;
    private int location;
    private boolean inJail;

    public Player(String name){
        this.name = name;
        this.money = 1500;
        this.location = 0;
        this.inJail = false;
    }
    public String getName(){
        return this.name;
        };
    public int getMoney(){
        return this.money;
    }
    public int getLocation(){
        return this.location;
    }
    public boolean getInJail(){
        return this.inJail;
    }

    public void addMoney(int amount){
        money = money + amount;
    }
    public void payMoney(int amount){
        money = money - amount;
    }
    public void setLocation(int location) {
        this.location = location;
    }
    public void setMoney(int money) {
        this.money = money;
    }

    public void setInJail(boolean inJail) {
        this.inJail = inJail;
    }

}
