package Login;

import Database.DBConnect;
import MainPage.MainPageController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;


public class LoginController extends DBConnect {
    private Staff staff;

    @FXML
    private TextField Username;

    @FXML
    private PasswordField Password;

    @FXML
    public void SignIn(ActionEvent event) throws IOException {
        String username = Username.getText();
        String password = Password.getText();
        if(checkstaff(username , password)){
            Menu(checkaccount(username ,password) , event);
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Wrong Username or Password");
            alert.setContentText("Please Enter a correct Username or Password");
            alert.show();
        }
    }
    public void Menu(Staff staff , ActionEvent event)throws IOException{
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
        stage.setResizable(false);
        stage.show();

    }
}
