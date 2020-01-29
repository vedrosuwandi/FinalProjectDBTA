package MainPage;

import Database.DBConnect;
import Login.Staff;
import Menu.MenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;

public class MainPageController extends DBConnect {
    private Staff staff;
    @FXML
    private Text Name;
    @FXML
    private TextField Table;
    private String tablenumber;

    public void initialize(Staff staff){
        this.staff = staff;
        Name.setText(staff.getName());
    }

    public void LogOut(ActionEvent event) throws IOException , SQLException {
        getconnection().close();
        Parent LoginParent = FXMLLoader.load(getClass().getResource("/Login/Login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setTitle("LoginPage");
        stage.setScene(new Scene(LoginParent));
        stage.setResizable(false);
        stage.show();

    }

    public void MenuList(ActionEvent event) throws IOException ,SQLException{
        if(Table.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Table not Inserted");
            alert.setContentText("Please Insert Table Number");
            alert.show();
        }else{
            String table = Table.getText();
            staff.setTableNumber(Integer.parseInt(table));
            getconnection().close();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Menu/Menu.fxml"));
            Parent MenuPage = loader.load();

            // Passing object user to the MainPageController class
            MenuController control = loader.getController();
            control.initialize(staff);

            // Gets stage's info and setting it up
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setResizable(false);
            stage.setScene(new Scene(MenuPage));
            stage.setTitle("Menu");
            stage.setResizable(false);
            stage.show();
            Table.setEditable(false);
        }

    }
}
