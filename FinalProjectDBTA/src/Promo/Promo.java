package Promo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Promo {

    private int ID;
    private final StringProperty Description;
    private int Percentage;

    public Promo(int ID, String description, int percentage) {
        this.ID = ID;
        Description = new SimpleStringProperty(description);
        Percentage = percentage;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return Description.get();
    }

    public void setDescription(String description) {
        Description.set(description);
    }

    public int getPercentage() {
        return Percentage;
    }

    public void setPercentage(int percentage) {
        Percentage = percentage;
    }
}
