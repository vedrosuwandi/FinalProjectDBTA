package BillPage;

import Database.DBConnect;
import Login.Staff;

import MainPage.MainPageController;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.naming.Name;
import javax.print.DocFlavor;
import java.io.IOException;
import java.sql.*;

public class BillController extends DBConnect {

    @FXML
    private Text Staff;

    @FXML
    private TextField Table;

    @FXML
    private Text Date;

    @FXML
    private Text BillNo;

    @FXML
    private Text SubTotal;

    @FXML
    private Text Service;

    @FXML
    private Text Tax;

    @FXML
    private Text Total;

    @FXML
    private Text Pay;

    @FXML
    private Text Change;

    @FXML
    private TableView<TransactionCart> Bill;

    @FXML
    private TableColumn<TransactionCart, Integer> Qty;

    @FXML
    private TableColumn<TransactionCart, String> Description;

    @FXML
    private TableColumn<TransactionCart, Double> Price;


    private Staff staff;
    private TransactionCart cart;


    ObservableList<TransactionCart> bill = FXCollections.observableArrayList();


    public void initialize(Staff staff){
        this.staff = staff;
        Staff.setText(staff.getName());
        String query = "select * from Transaction_Cart";
        //query = String.format(query, billno);
        try {
            ResultSet rs = connect.createStatement().executeQuery(query);
            while (rs.next()) {
                bill.add(new TransactionCart(rs.getString("ItemName"), rs.getInt("ItemQTY"),rs.getDouble("Price")));
            }
        }catch (SQLException e) {
            System.err.println("Error" + e);
        }

        Description.setCellValueFactory(new PropertyValueFactory<>("Name"));
        Qty.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        Price.setCellValueFactory(new PropertyValueFactory<>("Price"));
        Bill.setItems(bill);
        Table.setText(Integer.toString(staff.getTableNumber()));
        SubTotal.setText(Double.toString(subtotal()));
        Tax.setText(Double.toString(Tax()));
        Service.setText(Double.toString(ServiceTax()));
        Total.setText(Double.toString(TotalPrice()));
        BillNo.setText(Integer.toString(getBillID()));
        Pay.setText(Integer.toString(getPay()));
        Change.setText(Integer.toString( getPay() - TotalPrice().intValue()));
    }


    public Integer getBillID(){
        String query = "select max(Bill_number) from Bill";
        Integer ID = null;
        try {
            PreparedStatement stnt = getconnection().prepareStatement(query);
            ResultSet rs = stnt.executeQuery();
            if(rs.next()){
                ID = rs.getInt(1);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return ID;
    }

    public Integer getPay(){
        String query = "select Pay from Bill where Bill_number = (select max(Bill_number) from Bill);";
        Integer Paid = null;
        try {
            PreparedStatement stnt = getconnection().prepareStatement(query);
            ResultSet rs = stnt.executeQuery();
            if(rs.next()){
                Paid = rs.getInt(1);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return Paid;
    }

    /*public int viewPay(int TableNum){
        String query = String.format("Select Total_Price from Bill where TableNum = '%s'" , TableNum);
        try {
            Statement statement = getconnection().createStatement();
            ResultSet rs = statement.executeQuery(query); // to get return of result set this statement is essential
            while (rs.next()){
                cart.setPay(rs.getInt(8));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return cart.getPay();
    }*/

    public Double Tax(){
        return subtotal() * 0.1;
    }
    public Double ServiceTax(){
        return (subtotal() * 7) / 100;
    }

    public Double subtotal() {
        double Total = 0;
        for (TransactionCart cart : Bill.getItems()) {
            Total += Price.getCellData(cart);
        }
        return Total;
    }

    public Double TotalPrice(){
        return subtotal() + Tax() + ServiceTax(); //-promo
    }

    public void back(ActionEvent event)throws IOException {
        clearcart();
        clearpromo();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/MainPage/Mainpage.fxml"));
        Parent MenuPage = loader.load();

        // Passing object user to the PromoController class
        MainPageController control = loader.getController();
        control.initialize(staff);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Main Page");
        stage.setScene(new Scene(MenuPage));
        stage.setResizable(false);
        stage.show();

    }



}
