package Database;

import Login.Staff;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBConnect {
    public Connection connect;
    private Statement statement;

    public DBConnect(){
        String Username = "VED8093";
        String Password = "yu6s9unt";
       // String Username = "JOT6655";
        //String Password = "938s6o36";
        //String Username = "ELL7121";
        //String Password = "vi7dpmzg";
       try{
           connect = DriverManager.getConnection("jdbc:mysql://dbta.1ez.xyz:3306/5_LuxuryClub?useSSL=false" , Username , Password);
           statement = connect.createStatement();
       }catch (SQLException ex){
           System.out.println(ex.getMessage());
       }
    }

    public boolean checkstaff(String Username,String Password){ // to check the username and the password matches based on the database
        String query = String.format("Select * from Staff where Staff_Name='%s' and Staff_Password = '%s'" , Username , Password);
        try{
            statement = connect.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()){
                return true;
            }else{
                return false;
            }
        }catch (SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    public Staff checkaccount(String Username , String Password) { // check account whether the staff's username and password correct or not
        Staff tempUser = new Staff(); // create the variable with an object as it's type to be returned
        String data = "";
        if (checkstaff(Username, Password)) {
            data = String.format("Select * from Staff where Staff_Name='%s' and Staff_Password = '%s'", Username, Password);
        }
        try {
            statement = connect.createStatement();
            ResultSet rs = statement.executeQuery(data);
            rs.next();
            // take the data from the column in the database
            tempUser.setName(rs.getString("Staff_Name"));
            connect.close();
        } catch (SQLException ex) {
            ex.printStackTrace();

        }
        return tempUser; // return the object
    }

    public void addOrder(String itemName , int item_QTY , double price ){
        String query = String.format("insert into Transaction_Cart(ItemName, ItemQTY, Price) values('%s','%s','%s')" , itemName , item_QTY, price);
        try {
            statement = connect.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void applypromo(String Promo , int Percentage){
        String query = String.format("Insert into UsedPromo(promoName , percentage) values('%s' , '%s')" , Promo , Percentage);
        try{
            if (promoable()){
                statement = connect.createStatement();
                statement.executeUpdate(query);
            }else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("only one promo allowed");
                alert.show();
            }


        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    public void clearcart(){
        String query = "truncate Transaction_Cart";
        try{
            statement = connect.createStatement();
            statement.executeUpdate(query);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }

    public void clearpromo(){
        String query = "truncate UsedPromo";
        try{
            statement = connect.createStatement();
            statement.executeUpdate(query);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }

    public Connection getconnection(){
        return connect;
    }

    public boolean promoable(){
        String query = "select count(*) from UsedPromo";
        boolean flag = true;
        try {
            statement = connect.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if(rs.getInt(1) == 0){
                flag =  true;
            }else {
                flag = false;
            }
        }catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return flag;
    }

}
