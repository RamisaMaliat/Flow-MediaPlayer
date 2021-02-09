package MediaPlayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * This class extends the abstract class "Application"
 * and contains main function
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root;
        stage.resizableProperty().setValue(Boolean.FALSE);
        root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        stage.getIcons().add(new Image("file:.\\src\\MediaPlayer\\resources\\icon.png"));
        Scene scene = new Scene(root);
        stage.setTitle("");
        scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This function launches the main window
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}