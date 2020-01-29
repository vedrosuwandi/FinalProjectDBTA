package Transaction;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TransactionCart {
    private final StringProperty Name;
    private final IntegerProperty Quantity;
    private double price;
    private int pay;
   // private int ID;


    public TransactionCart(String name, int quantity, double price) {
        Name = new SimpleStringProperty(name);
        this.Quantity = new SimpleIntegerProperty(quantity);
        this.price = price;
        //this.ID = ID;
    }


    public String getName() {
        return Name.get();
    }

    public void setName(String name) {
        Name.set(name);
    }

    public int getQuantity() {
        return Quantity.get();
    }

    public void setQuantity(int quantity) {
        Quantity.set(quantity);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }
}
