package handlers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import session.UserSession;

import java.io.IOException;

public abstract class Handlers {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 275;

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
                    WIDTH, HEIGHT);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
