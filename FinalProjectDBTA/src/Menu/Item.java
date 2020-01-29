package Menu;

public class Item {
    private int ID;
    private String Name;
    private String Type;
    private double Price;

    public Item(int ID, String name, String type, double price) {
        this.ID = ID;
        Name = name;
        Type = type;
        Price = price;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }
}
