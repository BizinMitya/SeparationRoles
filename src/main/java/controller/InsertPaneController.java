package controller;

import dao.ProductDAO;
import dao.impl.ProductDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.dto.ProductRecord;
import org.apache.log4j.Logger;
import session.UserSession;

import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;

public class InsertPaneController implements Initializable {

    public TextField nameTextField;
    public TextField manufacturerTextField;
    public TextField costTextField;
    public TextField countryTextField;
    public TextField descriptionTextField;
    public CheckBox accessCheckBox;

    public Button addButton;
    public Button cancelButton;
    public Text errorText;
    private ProductDAO productDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productDAO = new ProductDAOImpl();
    }

    public void insertButton(ActionEvent actionEvent) {
        String name = nameTextField.getText();
        String manufacturer = manufacturerTextField.getText();
        String stringCost = costTextField.getText();
        String country = countryTextField.getText();
        String description = descriptionTextField.getText();
        boolean access = accessCheckBox.isSelected();
        int cost;
        try {
            cost = Integer.parseInt(stringCost);
            if (name.equals("") || manufacturer.equals("") || country.equals("")) {
                errorText.setText(name.equals("") ? "Поле 'Наименование' не должно быть пустым!" :
                        manufacturer.equals("") ? "Поле 'Производитель' не должно быть пустым!" :
                                country.equals("") ? "Поле 'Страна' не должно быть пустым!" : "");
            } else {
                productDAO.insertProduct(new ProductRecord(name, manufacturer, cost, country, description,
                        access ? BigInteger.ONE.shiftLeft(UserSession.selectedUser.getId()).toString(16) : "0"));
                closeWindow(actionEvent);
            }
        } catch (NumberFormatException e) {
            errorText.setText("Некорректная стоимость!");
        }
    }

    public void cancelButton(ActionEvent actionEvent) {
        closeWindow(actionEvent);
    }

    private void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Parent) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
