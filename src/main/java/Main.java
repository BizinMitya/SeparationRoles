import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 275;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/authorizationPane.fxml"));
        primaryStage.setTitle("Авторизация");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.setMaximized(false);
        primaryStage.setResizable(false);
        //primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.show();
    }
}
