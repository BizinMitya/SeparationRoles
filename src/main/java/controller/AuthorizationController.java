package controller;

import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthorizationController implements Initializable {

    public TextField loginTextField;
    public PasswordField passwordTextField;
    public Text errorText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
}
