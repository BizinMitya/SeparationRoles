package controller;

import handlers.Handlers;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import jdbc.JDBC;
import model.Product;
import session.UserSession;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserPaneController implements Initializable {
    private static final String GET_DATA_FOR_USER_QUERY = "SELECT * FROM products WHERE (maskAccess & (1 << ?)) > 0";
    public TableView table;
    public Button logoutButton;

    private Product createProductFromResultSet(ResultSet resultSet) throws SQLException {
        return new Product(resultSet.getLong("idProducts"),
                resultSet.getString("name"),
                resultSet.getString("manufacturer"),
                String.valueOf(resultSet.getInt("cost")),
                resultSet.getString("country"),
                resultSet.getString("description"),
                resultSet.getLong("maskAccess")
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources) {
        try (Connection connection = JDBC.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(GET_DATA_FOR_USER_QUERY)) {
            preparedStatement.setLong(1, UserSession.loginUser.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    table.getItems().add(createProductFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void logout(ActionEvent actionEvent) {
        Handlers.logout(actionEvent);
    }
}
