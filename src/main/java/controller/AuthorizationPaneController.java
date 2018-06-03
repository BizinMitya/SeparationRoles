package controller;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jdbc.JDBC;
import model.Role;
import model.User;
import org.apache.log4j.Logger;
import session.UserSession;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AuthorizationPaneController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(AuthorizationPaneController.class);
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public TextField loginTextField;
    public PasswordField passwordTextField;
    public Text errorText;
    public Button loginButton;
    public GridPane gridPane;
    private UserDAO userDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userDAO = new UserDAOImpl();
        loginButton.setOnAction(event -> authorization());
        gridPane.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                authorization();
            }
        });
    }

    private void authorization() {
        try {
            String login = loginTextField.getText();
            String password = passwordTextField.getText();
            User user = userDAO.getUser(login, password);
            if (user == null) {
                errorText.setText("Неверный логин/пароль!");
            } else {
                UserSession.loginUser = user;
                Stage stage = (Stage) errorText.getParent().getScene().getWindow();
                stage.setTitle("Разграничение доступа");
                stage.setMaximized(false);
                stage.setResizable(false);
                Scene scene;
                if (UserSession.loginUser.getRole().equals(Role.admin)) {
                    scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/adminPane.fxml")),
                            WIDTH, HEIGHT);
                } else {
                    scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/userPane.fxml")),
                            WIDTH, HEIGHT);
                }
                stage.setScene(scene);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            errorText.setText("Произошла ошибка в программе!");
        }
    }
}


