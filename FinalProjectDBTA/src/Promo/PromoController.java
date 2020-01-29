package Promo;

import Database.DBConnect;
import Login.Staff;
import MainPage.MainPageController;
import Menu.Item;
import Transaction.TransactionCart;
import Transaction.TransactionController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PromoController extends DBConnect {

    @FXML
    private TableView<Promo> Promo;
    @FXML
    private TableColumn<Promo, Integer> PromoID;
    @FXML
    private TableColumn<Promo, String> PromoName;
    @FXML
    private TableColumn<Promo, Integer> PromoPercentage;
    private Promo promo;
    private Staff staff;
    private TransactionCart cart;

    ObservableList<Promo> PromoList = FXCollections.observableArrayList();

    public void Initialize(){
        initialize(promo ,staff);
    }

    public void initialize(Promo pro , Staff staff){
        this.staff = staff;
        this.promo = pro;
        String query = "Select * from Promo";
        try {
            ResultSet rs = getconnection().createStatement().executeQuery(query);
            while (rs.next()) {
                PromoList.add(new Promo(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
        }catch (SQLException e) {
            System.err.println("Error" + e);
        }

        PromoID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        PromoName.setCellValueFactory(new PropertyValueFactory<>("Description"));
        PromoPercentage.setCellValueFactory(new PropertyValueFactory<>("Percentage"));

       Promo.setItems(PromoList);

    }

    public TransactionCart Cart(TransactionCart cart){
        this.cart = cart;
        return cart;
    }

    public void Bill(ActionEvent event)throws IOException ,SQLException {
        getconnection().close();
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

    public void usePromo(ActionEvent event)throws SQLException{
        Promo promo = Promo.getSelectionModel().getSelectedItem();

            if(promo == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Please Select Promo");
                alert.show();
            }else{
                applypromo(promo.getDescription(), promo.getPercentage());

            }
        }

}




