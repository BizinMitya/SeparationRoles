package controller;

import dao.ProductDAO;
import dao.impl.ProductDAOImpl;
import handlers.Handlers;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import session.UserSession;

import java.net.URL;
import java.util.ResourceBundle;

public class UserPaneController implements Initializable {

    public TableView table;
    public Button logoutButton;
    private ProductDAO productDAO;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources) {
        productDAO = new ProductDAOImpl();
        table.getItems().addAll(
                FXCollections.observableArrayList(productDAO.getDataForUserById(UserSession.loginUser.getId())));
    }

    public void logout(ActionEvent actionEvent) {
        Handlers.logout(actionEvent);
    }
}
