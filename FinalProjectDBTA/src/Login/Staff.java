package Login;

public class Staff {
    private int ID;
    private String Name;

    private int TableNumber;

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

    public void setTableNumber(int tableNumber) {
        TableNumber = tableNumber;
    }

    public int getTableNumber() {
        return TableNumber;
    }
}
