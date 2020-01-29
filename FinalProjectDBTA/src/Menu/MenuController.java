package Menu;

import Database.DBConnect;
import Login.Staff;
import MainPage.MainPageController;
import Transaction.TransactionCart;
import Transaction.TransactionController;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class MenuController extends DBConnect {

    @FXML
    private TextField amount;
    @FXML
    private TextField ConsumeType;

    @FXML
    private TableView<Item> Item;

    @FXML
    private TableColumn<Item, Integer> ItemID;

    @FXML
    private TableColumn<Item, String> ItemName;

    @FXML
    private TableColumn<Item, Double> ItemPrice;

    @FXML
    private TextField Table;

    private Integer Amount = 1;

    ObservableList<Item> Items = FXCollections.observableArrayList();


    private Staff staff;
    private TransactionCart cart;


    public void Initialize(ActionEvent event){
        initialize(staff);
    }

   public void initialize(Staff staff){
       this.staff = staff;
       ConsumeType.setText("All");
       amount.setText(Amount.toString());
       Table.setText(Integer.toString(staff.getTableNumber()));
       Item.getItems().clear();
       String query = "select * from Item";
       try {
           ResultSet rs = getconnection().createStatement().executeQuery(query);
           while (rs.next()) {
               Items.add(new Item(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getDouble(4)));
           }
       }catch (SQLException e) {
           System.err.println("Error" + e);
       }

       ItemID.setCellValueFactory(new PropertyValueFactory<>("ID"));
       ItemName.setCellValueFactory(new PropertyValueFactory<>("Name"));
       ItemPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));

       Item.setItems(Items);
   }


   public void SelectFood(){
       Item.getItems().clear();
       ConsumeType.setText("Food");
       String query = String.format("Select * from Item where Type = 'food'");
       try {

           ResultSet rs = getconnection().createStatement().executeQuery(query);
           while (rs.next()) {
               Items.add(new Item(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
           }
       }catch (SQLException e) {
           System.err.println("Error" + e);
       }


       ItemID.setCellValueFactory(new PropertyValueFactory<>("ID"));
       ItemName.setCellValueFactory(new PropertyValueFactory<>("Name"));
       ItemPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
       Item.setItems(Items);

   }

   public void SelectDrink(){
        Item.getItems().clear();
        ConsumeType.setText("Drink");
        String query = String.format("Select * from Item where Type = 'drink'");

        try {

           ResultSet rs = getconnection().createStatement().executeQuery(query);
           while (rs.next()) {
               Items.add(new Item(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
           }
       }catch (SQLException e) {
           System.err.println("Error" + e);
       }


       ItemID.setCellValueFactory(new PropertyValueFactory<>("ID"));
       ItemName.setCellValueFactory(new PropertyValueFactory<>("Name"));
       ItemPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
       Item.setItems(Items);

   }

    public void add(){
       String Total = Integer.toString(++Amount);
       amount.setText(Total);
    }

    public void subtract(){
        if(Amount <= 0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("You have 0 Items");
            alert.setContentText("You can't subtract");
            alert.show();
        }else{
            String Total = Integer.toString(--Amount);
            amount.setText(Total);
        }

    }

    @FXML
    public void menu(Staff staff, ActionEvent event) throws IOException , SQLException{
        getconnection().close();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/MainPage/MainPage.fxml"));
        Parent MenuPage = loader.load();

        // Passing object user to the MainPageController class
        MainPageController control = loader.getController();
        control.initialize(staff);

        // Gets stage's info and setting it up
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setResizable(false);
        stage.setScene(new Scene(MenuPage));
        stage.setTitle("Luxury Club");
        stage.show();

    }
    public TransactionCart Cart(TransactionCart cart){
        this.cart = cart;
        return cart;
    }

    public void Bill(ActionEvent event) throws IOException , SQLException{
        getconnection().close();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Transaction/Transaction.fxml"));
        Parent MenuPage = loader.load();

        // Passing object user to the MainPageController class
        TransactionController control = loader.getController();
        control.initialize(Cart(cart) , staff);

        // Gets stage's info and setting it up
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setResizable(false);
        stage.setScene(new Scene(MenuPage));
        stage.setTitle("Bill");
        stage.setResizable(false);
        stage.show();
    }
    public void backtomenu(ActionEvent event)throws IOException,SQLException{
        menu(staff , event);
    }

    public void addItem(ActionEvent event){
        Item item = Item.getSelectionModel().getSelectedItem();
        if(item == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Please Buy Something");
                alert.show();
        }else{
            addOrder(item.getName() , Amount , Amount*item.getPrice());
        }
    }

}
