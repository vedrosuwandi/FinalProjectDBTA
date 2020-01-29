package Transaction;

import BillPage.BillController;
import Database.DBConnect;
import Login.Main;
import Login.Staff;
import MainPage.MainPageController;
import Menu.Item;
import Menu.MenuController;
import Promo.Promo;
import Promo.PromoController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class TransactionController extends DBConnect {
    @FXML
    private TextField Table;

    @FXML
    private TableView<TransactionCart> Transaction;

    @FXML
    private TableColumn<TransactionCart, String> TransactionName;

    @FXML
    private TableColumn<TransactionCart, Integer> TransactionQTY;

    @FXML
    private TableColumn<TransactionCart, Double> TransactionPrice;

    @FXML
    private TableView<AppliedPromo> TransactionPromo;

    @FXML
    private TableColumn<AppliedPromo, String> TransactionPromoName;

    @FXML
    private TableColumn<AppliedPromo, Integer> TransactionPercentage;

    @FXML
    private Text Total;

    @FXML
    private Text SubTotal;

    @FXML
    private Text Change;

    @FXML
    private Text Tax;

    @FXML
    private Text Service;

    @FXML
    private TextField Cash;

    @FXML
    private Text TotalPromo;

    @FXML
    private Button ViewBill;

    @FXML
    private Button Pay;

    ObservableList<TransactionCart> Cart = FXCollections.observableArrayList();
    ObservableList<AppliedPromo> PromoList = FXCollections.observableArrayList();
    private Staff staff;
    private Promo promo;
    private TransactionCart cart;
    private AppliedPromo promolist;
    private DBConnect DBConnect;

    private int StaffID;

    public void initialize (TransactionCart cart , Staff staff){
        this.staff = staff;
        this.cart = cart;
        this.DBConnect = new DBConnect();
        Table.setText(Integer.toString(staff.getTableNumber()));
        StaffID = staff.getID();
        showpurchase();
        TransactionName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        TransactionQTY.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        TransactionPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        Transaction.setItems(Cart);
        promolist(promolist);

    }
    public void showpurchase(){
        String query = "Select * from Transaction_Cart";
        try {
            ResultSet rs = getconnection().createStatement().executeQuery(query);
            while (rs.next()) {
                Cart.add(new TransactionCart(rs.getString(1), rs.getInt(2),rs.getDouble(3)));
            }
        }catch (SQLException e) {
            System.err.println("Error" + e);
        }
    }

    public void refresh(){
        SubTotal.setText(Double.toString(SubTotal()));
        Total.setText(Double.toString(TotalPrice()));
        Tax.setText(Double.toString(Tax()));
        Service.setText(Double.toString(ServiceTax()));
        TotalPromo.setText(Double.toString(Promo()));
        Pay.setDisable(false);
    }

    public Double SubTotal() {
        double Total = 0;
        for (TransactionCart cart : Transaction.getItems()) {
            Total += TransactionPrice.getCellData(cart);
        }
        return Total;
    }
    public Double Tax(){
        return SubTotal() * 0.1;
    }
    public Double ServiceTax(){
        return (SubTotal() * 7) / 100;
    }


    public Double Promo(){
        double Total = SubTotal();
        if(TransactionPromo.getItems().isEmpty()){
            Total = 0;
        }else{
            for(AppliedPromo promo : TransactionPromo.getItems()){
                Total = SubTotal() * ((double)TransactionPercentage.getCellData(promo)/100);
            }
        }
        return Total;
    }
    public int Change(){
        String cash = Cash.getText();
        int change = Integer.parseInt(cash) - TotalPrice().intValue();
        Change.setText(Integer.toString(change));
        return change;
    }
    public Double TotalPrice(){
        return SubTotal() + Tax() + ServiceTax() - Promo();
    }

    public Date getime() throws ParseException{
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String now = format.format(date);
        return format.parse(now);
    }


    public void promolist(AppliedPromo promolist){
        this.promolist = promolist;
        String query = String.format("Select * from UsedPromo");
        try {
            ResultSet rs = getconnection().createStatement().executeQuery(query);
            while (rs.next()) {
                PromoList.add(new AppliedPromo(rs.getString(1), rs.getInt(2)));
            }
        }catch (SQLException e) {
            System.err.println("Error" + e);
        }
        TransactionPromoName.setCellValueFactory(new PropertyValueFactory<>("Promo"));
        TransactionPercentage.setCellValueFactory(new PropertyValueFactory<>("Percentage"));

        TransactionPromo.setItems(PromoList);
    }

    public Promo Promo(Promo promo){
        this.promo = promo;
        return promo;
    }

    public void PromoList(ActionEvent event) throws IOException , SQLException {
        getconnection().close();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Promo/Promo.fxml"));
        Parent MenuPage = loader.load();

        // Passing object user to the PromoController class
        PromoController control = loader.getController();
        control.initialize(Promo(promo) , staff);

        // Gets stage's info and setting it up
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setResizable(false);
        stage.setScene(new Scene(MenuPage));
        stage.setTitle("PromoList");
        stage.show();
    }

    public void Order(ActionEvent event) throws IOException , SQLException{
        getconnection().close();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Menu/Menu.fxml"));
        Parent MenuPage = loader.load();

        // Passing object user to the Menu Controller class
        MenuController control = loader.getController();
        control.initialize(staff);

        // Gets stage's info and setting it up
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setResizable(false);
        stage.setScene(new Scene(MenuPage));
        stage.setTitle("Menu");
        stage.show();

    }


    public void pay(ActionEvent event){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        String cash = Total.getText();
        double money = Double.parseDouble(cash);
        if (cash.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Insert Money");
            alert.show();
        }else{
            if (Integer.parseInt(Cash.getText()) < TotalPrice()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Insufficient Money");
                alert.show();
            }else{
                addBill(money, date);
                addTransactionDetail();
                Change();
                ViewBill.setDisable(false);
            }
        }
    }

    public void addBill(Double cash, String date) {
        String query = "Insert into Bill(STAFF_ID , address , name, Promo_ID , TableNum , Transaction_Date , Total_Price, Pay) values (?, ?,?,?,?,?,?,?) " ;
        boolean flag = false;
        if (getPromoID() == 0) {
            query = "Insert into Bill(STAFF_ID , address , name , TableNum , Transaction_Date , Total_Price, Pay) values (?,?,?,?,?,?,?) " ;
            flag = true;
        }
        try{
            Connection con = DBConnect.getconnection();
            PreparedStatement stnt = con.prepareStatement(query);

            stnt.setInt(1, getStaffID(staff.getName()));
            stnt.setString(2, "Sun City");
            stnt.setString(3 , "Luxury Club");

            if (!flag) {
                stnt.setInt(4, getPromoID());
                stnt.setInt(5,staff.getTableNumber());
                stnt.setString(6, date);
                stnt.setDouble(7 , cash);
                stnt.setInt(8, Integer.parseInt(Cash.getText()));
            } else {
                stnt.setInt(4,staff.getTableNumber());
                stnt.setString(5, date);
                stnt.setDouble(6 , cash);
                stnt.setInt(7, Integer.parseInt(Cash.getText()));
            }
            stnt.execute();
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public int getItemCartID(TransactionCart cart){
        String query = "select ID from Item where Name = '%s'";
        query = String.format(query, cart.getName());
        Integer ID = null;
        try {
            Connection con = DBConnect.getconnection();
            PreparedStatement stnt = con.prepareStatement(query);
            ResultSet rs = stnt.executeQuery();

            if(rs.next())
                ID = rs.getInt(1);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return ID;
    }


    public void addTransactionDetail() {
        try {
            String query2 = "insert into Transaction_Detail(Bill, Item_ID, Item_QTY) values(?, ?, ?)";
            Connection con = DBConnect.getconnection();
            PreparedStatement stnt = con.prepareStatement(query2);

            for (TransactionCart cart : Cart) {
                stnt.setInt(1, getBillID());
                stnt.setInt(2, getItemCartID(cart));
                stnt.setInt(3, cart.getQuantity());
                stnt.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ViewBill(ActionEvent event)throws IOException{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/BillPage/Bill.fxml"));
        Parent MenuPage = loader.load();

        // Passing object user to the PromoController class
        BillController control = loader.getController();
        control.initialize(staff);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("Receipt");
        stage.setScene(new Scene(MenuPage));
        stage.setResizable(false);
        stage.show();
    }


    public int getPromoID(){
        int usedPromoID = 0;
        String query = String.format("select ID from UsedPromo up inner join Promo p on up.promoName = p.Description");
        try {
            Connection con = DBConnect.getconnection();
            PreparedStatement stnt = con.prepareStatement(query);
            ResultSet rs = stnt.executeQuery();
            if(rs.next()){
                usedPromoID = rs.getInt(1);
            }
//            stnt.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } catch (NullPointerException ex) {
            return 0;
        }
        return usedPromoID;
    }

    public Integer getStaffID(String Staffname){
        Integer ID = null;
        String query = "select Staff_ID from Staff where Staff_Name = '" + Staffname + "'";
        try {
            Connection con = DBConnect.getconnection();
            PreparedStatement stnt = con.prepareStatement(query);
            ResultSet rs = stnt.executeQuery();
            if(rs.next()){
                ID = rs.getInt(1);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return ID;
    }

    public Integer getBillID(){
        String query = "select max(Bill_number) from Bill";
        Integer ID = null;
        try {
            Connection con = DBConnect.getconnection();
            PreparedStatement stnt = con.prepareStatement(query);
            ResultSet rs = stnt.executeQuery();
            if(rs.next()){
                ID = rs.getInt(1);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return ID;
    }



   /*public void LogOut(ActionEvent event)throws IOException , SQLException{
       getconnection().close();
       Parent LoginParent = FXMLLoader.load(getClass().getResource("/Login/Login.fxml"));
       Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
       stage.setTitle("LoginPage");
       stage.setScene(new Scene(LoginParent));
       stage.show();
   }*/
}
