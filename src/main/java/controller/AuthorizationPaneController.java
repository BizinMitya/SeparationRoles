package controller;

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
import session.UserSession;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AuthorizationPaneController implements Initializable {
    private static final String CHECK_USERS_QUERY = "SELECT * FROM Users WHERE login = ? AND password = ?";
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public TextField loginTextField;
    public PasswordField passwordTextField;
    public Text errorText;
    public Button loginButton;
    public GridPane gridPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginButton.setOnAction(event -> authorization());
        gridPane.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                authorization();
            }
        });
    }

    private User createUserFromResultSet(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getLong("idUser"),
                resultSet.getString("login"),
                resultSet.getString("password"),
                Role.valueOf(resultSet.getString("role"))
        );
    }

    private void authorization() {
        String login = loginTextField.getText();
        String password = passwordTextField.getText();
        try (Connection connection = JDBC.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(CHECK_USERS_QUERY)) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    errorText.setText("Неверный логин/пароль!");
                } else {
                    UserSession.loginUser = createUserFromResultSet(resultSet);
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
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            errorText.setText("Произошла ошибка в программе!");
        }
    }
}
