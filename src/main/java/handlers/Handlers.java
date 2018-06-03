package handlers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import session.UserSession;

import java.io.IOException;
import java.math.BigInteger;

public abstract class Handlers {
    
    private static final Logger LOGGER = Logger.getLogger(Handlers.class);

    public static void logout(ActionEvent actionEvent) {
        try {
            Button button = (Button) actionEvent.getSource();
            UserSession.loginUser = null;
            UserSession.selectedUser = null;
            Stage stage = (Stage) button.getParent().getScene().getWindow();
            stage.setTitle("Авторизация");
            stage.setMaximized(false);
            stage.setResizable(false);
            Scene scene;
            scene = new Scene(FXMLLoader.load(Handlers.class.getResource("/fxml/authorizationPane.fxml")),
                    300, 275);
            stage.setScene(scene);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void insert(ActionEvent actionEvent) {
        try {
            Parent window;
            Stage oldStage = (Stage) ((Parent) actionEvent.getSource()).getScene().getWindow();

            Stage popUp = new Stage();
            window = FXMLLoader.load(Handlers.class.getResource("/fxml/insertPane.fxml"));
            Scene scene = new Scene(window, 400, 300);

            popUp.initModality(Modality.APPLICATION_MODAL);
            popUp.initOwner(oldStage);

            popUp.setTitle("Добавление");
            popUp.setScene(scene);
            popUp.setResizable(false);

            oldStage.setOpacity(0.9);
            popUp.showAndWait();
            oldStage.setOpacity(1);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
