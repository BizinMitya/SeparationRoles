package controller;

import dao.ProductDAO;
import dao.UserDAO;
import dao.impl.ProductDAOImpl;
import dao.impl.UserDAOImpl;
import handlers.Handlers;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import model.Product;
import model.User;
import session.UserSession;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AdminPaneController implements Initializable {

    public TableView table;
    public ComboBox usersComboBox;
    public Button updateButton;
    public TableColumn<Product, Boolean> accessColumn;
    public TableColumn<Product, String> nameColumn;
    public TableColumn<Product, String> manufacturerColumn;
    public TableColumn<Product, String> costColumn;
    public TableColumn<Product, String> countryColumn;
    public TableColumn<Product, String> descriptionColumn;
    public Button logoutButton;
    public Button insertButton;
    private List<User> users;
    private ProductDAO productDAO;
    private UserDAO userDAO;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources) {
        productDAO = new ProductDAOImpl();
        userDAO = new UserDAOImpl();
        users = userDAO.getAllUsersWithoutAdmin();
        usersComboBox.getItems().addAll(users.stream().map(User::getLogin).collect(Collectors.toCollection(ArrayList::new)));
        initializeTable();
    }

    @SuppressWarnings("unchecked")
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

        table.setRowFactory(
                (Callback<TableView<Product>, TableRow<Product>>) tableView -> {
                    TableRow<Product> row = new TableRow<>();
                    ContextMenu contextMenu = new ContextMenu();
                    MenuItem removeMenuItem = new MenuItem("Удалить");
                    removeMenuItem.setOnAction(event -> {
                        Product product = row.getItem();
                        if (productDAO.removeProduct(product.getId())) {
                            table.getItems().remove(product);
                        }
                    });
                    contextMenu.getItems().add(removeMenuItem);
                    row.contextMenuProperty().bind(
                            Bindings.when(row.emptyProperty())
                                    .then((ContextMenu) null)
                                    .otherwise(contextMenu)
                    );
                    return row;
                }
        );
    }

    public void update(ActionEvent actionEvent) {
        @SuppressWarnings("unchecked") List<Product> products = table.getItems();
        productDAO.updateProducts(products);
    }

    @SuppressWarnings("unchecked")
    public void selectUser(ActionEvent actionEvent) {
        ComboBox usersCB = (ComboBox) actionEvent.getSource();
        Optional<User> optionalUser = users.stream().filter(user ->
                user.getLogin().equals(usersCB.getSelectionModel().getSelectedItem())).findAny();
        if (optionalUser.isPresent()) {
            updateButton.setDisable(false);
            insertButton.setDisable(false);
            UserSession.selectedUser = optionalUser.get();
            table.getItems().clear();
            table.getItems().addAll(FXCollections.observableArrayList(
                    productDAO.getAllProductsForUser(UserSession.selectedUser.getId())));
        }
    }

    public void logout(ActionEvent actionEvent) {
        Handlers.logout(actionEvent);
    }

    @SuppressWarnings("unchecked")
    public void insert(ActionEvent actionEvent) {
        Handlers.insert(actionEvent);
        table.getItems().clear();
        table.getItems().addAll(FXCollections.observableArrayList(
                productDAO.getAllProductsForUser(UserSession.selectedUser.getId())));
    }
}
