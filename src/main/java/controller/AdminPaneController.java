package controller;

import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import jdbc.JDBC;
import model.Product;
import model.Role;
import model.User;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AdminPaneController implements Initializable {
    private static final String GET_ALL_USERS_QUERY = "SELECT * FROM users WHERE role <> 'admin'";
    private static final String GET_ALL_PRODUCTS_QUERY = "SELECT * FROM products";

    public TableView table;
    public ComboBox usersComboBox;
    public Button saveButton;
    public TableColumn<Product, Boolean> accessColumn;
    public TableColumn<Product, String> nameColumn;
    public TableColumn<Product, String> manufacturerColumn;
    public TableColumn<Product, String> costColumn;
    public TableColumn<Product, String> countryColumn;
    public TableColumn<Product, String> descriptionColumn;
    private List<User> users;

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        users = getAllUsers();
        usersComboBox.getItems().addAll(users.stream().map(User::getLogin).collect(Collectors.toCollection(ArrayList::new)));
        accessColumn.setCellValueFactory(new PropertyValueFactory<>("Доступ"));
        final Callback<TableColumn<Product, Boolean>, TableCell<Product, Boolean>> cellFactory =
                CheckBoxTableCell.forTableColumn(accessColumn);
        accessColumn.setCellFactory(column -> {
            TableCell<Product, Boolean> cell = cellFactory.call(column);
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        accessColumn.setCellValueFactory(cellData -> cellData.getValue().accessProperty());

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        manufacturerColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        costColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        countryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        usersComboBox.setOnAction(event -> {
            ComboBox usersCB = (ComboBox) event.getSource();
            Optional<User> optionalUser = users.stream().filter(user ->
                    user.getLogin().equals(usersCB.getSelectionModel().getSelectedItem())).findAny();
            if (optionalUser.isPresent()) {
                long idUser = optionalUser.get().getId();
                table.getItems().addAll(getAllProducts(idUser));
            }
        });
    }

    private List<Product> getAllProducts(long idUser) {
        List<Product> products = new ArrayList<>();
        try (Connection connection = JDBC.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(GET_ALL_PRODUCTS_QUERY)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(createProductFromResultSet(resultSet, idUser));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private Product createProductFromResultSet(ResultSet resultSet, long idUser) throws SQLException {
        return new Product(resultSet.getLong("idProducts"),
                resultSet.getString("name"),
                resultSet.getString("manufacturer"),
                String.valueOf(resultSet.getInt("cost")),
                resultSet.getString("country"),
                resultSet.getString("description"),
                (resultSet.getLong("maskAccess") & (1 << idUser)) > 0
        );
    }

    private User createUserFromResultSet(ResultSet resultSet) throws SQLException {
        return new User(resultSet.getLong("idUser"),
                resultSet.getString("login"),
                resultSet.getString("password"),
                Role.valueOf(resultSet.getString("role"))
        );
    }

    private List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = JDBC.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(GET_ALL_USERS_QUERY)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(createUserFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

}
