package MediaPlayer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * This is controller class for Main.fxml
 */
public class MainController implements Initializable {
    @FXML
    private ImageView imageView;

    /**
     * This function initializes the controller class of Main.fxml
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        File file = new File(".\\src\\MediaPlayer\\resources\\flow.png");
        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);
    }

    /**
     * This function opens the window to play audio
     * @param event
     * @throws Exception
     */
    @FXML
    public void PlayAudio(ActionEvent event) throws Exception {
        Stage HomePage = (Stage)((Node) event.getSource()).getScene().getWindow();
        HomePage.setTitle("");
        HomePage.resizableProperty().setValue(Boolean.TRUE);
        Parent root = FXMLLoader.load(getClass().getResource("MediaPlayer.fxml"));
        Scene scene = new Scene(root);
        scene.setOnMouseClicked(e -> {
            if(e.getClickCount() == 2){
                HomePage.setFullScreen(true);
            }
        });
        scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        HomePage.setScene(scene);
        HomePage.show();
    }

    /**
     * This function opens the window to play video
     * @param event
     * @throws Exception
     */
    @FXML
    public void PlayVideo(ActionEvent event) throws Exception {
        Stage HomePage = (Stage)((Node) event.getSource()).getScene().getWindow();
        HomePage.setTitle("");
        Parent root = FXMLLoader.load(getClass().getResource("MediaPlayer2.fxml"));
        Scene scene = new Scene(root);
        HomePage.resizableProperty().setValue(Boolean.TRUE);
        scene.setOnMouseClicked(e -> {
            if(e.getClickCount() == 2){
                HomePage.setFullScreen(true);
            }
        });
        scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        HomePage.setScene(scene);
        HomePage.show();
    }
}
