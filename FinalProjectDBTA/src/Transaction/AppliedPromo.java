package Transaction;

import Database.DBConnect;

public class AppliedPromo extends DBConnect {
    private String Promo;
    private int Percentage;

    public AppliedPromo(String promo, int percentage) {
        Promo = promo;
        Percentage = percentage;
    }

    public String getPromo() {
        return Promo;
    }

    public void setPromo(String promo) {
        Promo = promo;
    }

    public int getPercentage() {
        return Percentage;
    }

    public void setPercentage(int percentage) {
        Percentage = percentage;
    }
}
