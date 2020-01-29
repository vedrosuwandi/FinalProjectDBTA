package Login;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application{
    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage stage)throws Exception { // the login page is opened as the program is running
        Parent SignUpPageParent = FXMLLoader.load(getClass().getResource("/Login/Login.fxml"));
        stage.setTitle("LoginPage");
        stage.setScene(new Scene(SignUpPageParent));
        stage.setResizable(false);
        stage.show();
    }

}
