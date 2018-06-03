package controller;

import handlers.Handlers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import session.UserSession;

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
    private static final String UPDATE_ALL_PRODUCTS_QUERY =
            "UPDATE products SET name = ?, manufacturer = ?, cost = ?, country = ?, description = ?, maskAccess = ? WHERE idProducts = ?";

    public TableView table;
    public ComboBox usersComboBox;
    public Button saveButton;
    public TableColumn<Product, Boolean> accessColumn;
    public TableColumn<Product, String> nameColumn;
    public TableColumn<Product, String> manufacturerColumn;
    public TableColumn<Product, String> costColumn;
    public TableColumn<Product, String> countryColumn;
    public TableColumn<Product, String> descriptionColumn;
    public Button logoutButton;
    private List<User> users;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources) {
        users = getAllUsers();
        usersComboBox.getItems().addAll(users.stream().map(User::getLogin).collect(Collectors.toCollection(ArrayList::new)));
        initializeTable();
    }

    private boolean hasAccessCurrentUser(long maskAccess) {
        return (maskAccess & (1 << UserSession.selectedUser.getId())) > 0;
    }

    private void initializeTable() {
        accessColumn.setCellValueFactory(new PropertyValueFactory<>("Доступ"));
        Callback<TableColumn<Product, Boolean>, TableCell<Product, Boolean>> cellFactory =
                CheckBoxTableCell.forTableColumn(accessColumn);
        accessColumn.setCellFactory(column -> {
            TableCell<Product, Boolean> cell = cellFactory.call(column);
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        accessColumn.setCellValueFactory(cellData -> cellData.getValue().accessProperty());
        accessColumn.setOnEditCommit((TableColumn.CellEditEvent<Product, Boolean> t)
                -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setAccess(t.getNewValue()));

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit((TableColumn.CellEditEvent<Product, String> t)
                -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setName(t.getNewValue()));
        manufacturerColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        manufacturerColumn.setOnEditCommit((TableColumn.CellEditEvent<Product, String> t)
                -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setManufacturer(t.getNewValue()));
        costColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        costColumn.setOnEditCommit((TableColumn.CellEditEvent<Product, String> t)
                -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setCost(t.getNewValue()));
        countryColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        countryColumn.setOnEditCommit((TableColumn.CellEditEvent<Product, String> t)
                -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setCountry(t.getNewValue()));
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setOnEditCommit((TableColumn.CellEditEvent<Product, String> t)
                -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setDescription(t.getNewValue()));
    }

    private ObservableList<Product> getAllProducts(long idUser) {
        ObservableList<Product> products = FXCollections.observableArrayList();
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
                (resultSet.getLong("maskAccess") & (1 << idUser)) > 0,
                resultSet.getLong("maskAccess")
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

    public void save(ActionEvent actionEvent) {
        @SuppressWarnings("unchecked") List<Product> products = table.getItems();
        try (Connection connection = JDBC.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement(UPDATE_ALL_PRODUCTS_QUERY)) {
            for (Product product : products) {
                try {
                    long maskAccess = product.getMaskAccess();
                    if (hasAccessCurrentUser(product.getMaskAccess()) /*права в базе, т.к. maskAccess не меняется на UI*/
                            ^ product.getAccess() /*измененные права*/) {
                        maskAccess = product.getAccess() ?
                                product.getMaskAccess() + (1 << UserSession.selectedUser.getId()) :
                                product.getMaskAccess() - (1 << UserSession.selectedUser.getId());
                    }
                    int cost = Integer.parseInt(product.getCost());
                    preparedStatement.setString(1, product.getName());
                    preparedStatement.setString(2, product.getManufacturer());
                    preparedStatement.setInt(3, cost);
                    preparedStatement.setString(4, product.getCountry());
                    preparedStatement.setString(5, product.getDescription());
                    preparedStatement.setLong(6, maskAccess);
                    preparedStatement.setLong(7, product.getId());
                    preparedStatement.executeUpdate();
                } catch (NumberFormatException ignored) {
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void selectUser(ActionEvent actionEvent) {
        ComboBox usersCB = (ComboBox) actionEvent.getSource();
        Optional<User> optionalUser = users.stream().filter(user ->
                user.getLogin().equals(usersCB.getSelectionModel().getSelectedItem())).findAny();
        if (optionalUser.isPresent()) {
            UserSession.selectedUser = optionalUser.get();
            table.getItems().addAll(getAllProducts(UserSession.selectedUser.getId()));
        }
    }

    public void logout(ActionEvent actionEvent) {
        Handlers.logout(actionEvent);
    }
}
