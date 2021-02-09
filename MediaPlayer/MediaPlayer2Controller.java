package MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * This is controller class for MediaPlayer2.fxml
 */
public class MediaPlayer2Controller implements Initializable {

    private String path;
    private MediaPlayer mediaPlayer;

    @FXML
    private MediaView mediaView;

    @FXML
    private MenuBar myMenuBar;

    @FXML
    private Label totalTime;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider progressBar;

    @FXML
    private Button RepeatButton;

    @FXML
    private StackPane pane;

    @FXML
    private ImageView imageView;

    /**
     * This function opens an video file
     * @param event
     */
    @FXML
    private void OpenFileMethod(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a file","*.mp4",
                "*.mkv","*.flv","*.vob","*.ogv", "*.drc", "*.gif", "*.gifv", "*.mng", "*.avi", "*.MTS", "*.M2TS", "*.TS", "*.mov",
                "*.qt", "*.wmv", "*.yuv", "*.rm", "*.rmvb", "*.rmvb","*.viv","*.asf", "*.amv", "*.m4v", "*.mpg","*.mp2",
                "*.mpeg", "*.mpe", "*.mpv","*.mpg", "*.m2v","*.svi", "*.mxf", "*.roq", "*.nsv","*.flv", "*.f4v", "*.f4p", "*.f4a", "*.f4b");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        try{
            path = file.toURI().toString();
            if(path != null){
                try{
                    mediaPlayer.stop();
                }
                catch(Exception e){}
                Media media = new Media(path);
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);

                //creating bindings
                DoubleProperty widthProp = mediaView.fitWidthProperty();
                DoubleProperty heightProp = mediaView.fitHeightProperty();

                widthProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
                heightProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

                volumeSlider.setValue(mediaPlayer.getVolume()*100);
                volumeSlider.valueProperty().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable) {
                        mediaPlayer.setVolume(volumeSlider.getValue()/100);
                    }
                });

                mediaPlayer.currentTimeProperty().addListener(new ChangeListener<javafx.util.Duration>() {
                                                                  @Override
                                                                  public void changed(ObservableValue<? extends javafx.util.Duration> observable, javafx.util.Duration oldValue, javafx.util.Duration newValue) {
                                                                      progressBar.setValue(newValue.toSeconds());
                                                                  }
                                                              }
                );

                progressBar.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        mediaPlayer.seek(javafx.util.Duration.seconds(progressBar.getValue()));
                    }
                });

                progressBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        mediaPlayer.seek(javafx.util.Duration.seconds(progressBar.getValue()));
                    }
                });

                mediaPlayer.setOnReady(new Runnable() {
                    @Override
                    public void run() {
                        mediaPlayer.setCycleCount(1);
                        RepeatButton.setStyle("-fx-background-color: #e7830f;");
                        javafx.util.Duration total = media.getDuration();
                        progressBar.setMax(total.toSeconds());

                        double seconds = total.toSeconds();
                        long absSeconds = (long) Math.abs(seconds);
                        String t = String.format("%02d:%02d:%02d  ",
                                absSeconds / 3600, (absSeconds % 3600) / 60, absSeconds % 60);
                        totalTime.setText(t);
                    }
                });

                mediaPlayer.play();
            }
        }
        catch (Exception e){
            path = null;
        }
    }

    /**
     * This function initializes the controller class of MediaPlayer2.fxml
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    /**
     * This function pauses the video
     * @param event
     */
    public void pauseVideo(ActionEvent event){
        if(mediaPlayer != null) mediaPlayer.pause();
    }
    /**
     * This function stops the video
     * @param event
     */
    public void stopVideo(ActionEvent event){
        if(mediaPlayer != null) mediaPlayer.stop();
    }

    /**
     * This function plays the video at normal rate
     * @param event
     */
    public void playVideo(ActionEvent event){
        if(mediaPlayer != null){
            mediaPlayer.play();
            mediaPlayer.setRate(1);
        }
    }

    /**
     * This function skips 5 sec of the video
     * @param event
     */
    public void skip5(ActionEvent event){
        if(mediaPlayer != null) mediaPlayer.seek(mediaPlayer.getCurrentTime().add(javafx.util.Duration.seconds(5)));
    }
    /**
     * This function speeds up the video
     * @param event
     */
    public void furtherSpeedUpAudio(ActionEvent event){
        if(mediaPlayer != null) mediaPlayer.setRate(2);
    }
    /**
     * This function takes the video 5 sec back
     * @param event
     */
    public void back5(ActionEvent event){
        if(mediaPlayer != null) mediaPlayer.seek(mediaPlayer.getCurrentTime().add(javafx.util.Duration.seconds(-5)));
    }

    /**
     * This function slows down the video
     * @param event
     */
    public void furtherSlowDownAudio(ActionEvent event){
        if(mediaPlayer != null) mediaPlayer.setRate(0.5);
    }

    /**
     * This function creates the window for creating video playlist
     * @param event
     * @throws Exception
     */
    public void createVideoPlaylist(ActionEvent event) throws Exception{
        try{
            mediaPlayer.stop();
        }
        catch(Exception e){}
        Stage HomePage = (Stage) myMenuBar.getScene().getWindow();
        HomePage.setTitle("");
        Parent root = FXMLLoader.load(getClass().getResource("CreateVideoPlaylist.fxml"));
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
     * This function creates a window for viewing video playlists
     * @param event
     * @throws IOException
     */
    public void viewVideoPlaylists(ActionEvent event) throws IOException {
        try{
            mediaPlayer.stop();
        }
        catch(Exception e){}
        Stage HomePage = (Stage) myMenuBar.getScene().getWindow();
        HomePage.setTitle("");
        Parent root = FXMLLoader.load(getClass().getResource("ViewVideoPlaylists.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        HomePage.setScene(scene);
        HomePage.show();
    }

    /**
     * This function takes back to previous window
     * @param event
     * @throws IOException
     */
    public void Back(ActionEvent event) throws IOException{
        try{mediaPlayer.stop();}
        catch(Exception e){}
        Stage HomePage = (Stage) myMenuBar.getScene().getWindow();
        HomePage.setTitle("");
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        HomePage.setScene(scene);
        HomePage.resizableProperty().setValue(Boolean.TRUE);
        HomePage.show();
    }

    /**
     * This function increases the volume of the video sound
     * @param event
     */
    public void IncreaseVolume(ActionEvent event){
        try{
            mediaPlayer.setVolume(mediaPlayer.getVolume()+0.1);
            volumeSlider.setValue(mediaPlayer.getVolume()*100);
            volumeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(volumeSlider.getValue()/100);
                }
            });
        }
        catch(Exception e){}
    }
    /**
     * This function decreases the volume of the video sound
     * @param event
     */
    public void DecreaseVolume(ActionEvent event){
        try{ mediaPlayer.setVolume(mediaPlayer.getVolume()-0.1);
            volumeSlider.setValue(mediaPlayer.getVolume()*100);
            volumeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(volumeSlider.getValue()/100);
                }
            });
        }
        catch (Exception e){}
    }
    /**
     * This function mutes the video sound
     * @param event
     */
    public void Mute(ActionEvent event){
        try{
            mediaPlayer.setVolume(0.0);
            volumeSlider.setValue(mediaPlayer.getVolume()*100);
            volumeSlider.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    mediaPlayer.setVolume(volumeSlider.getValue()/100);
                }
            });
        }
        catch (Exception e){}
    }

    @FXML
    public void RepeatFunction(ActionEvent event)
    {
        if(mediaPlayer != null){
            if(mediaPlayer.getCycleCount()==MediaPlayer.INDEFINITE)
            {
                mediaPlayer.setCycleCount(1);
                RepeatButton.setStyle("-fx-background-color: #e7830f;");
            }

            else
            {
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                RepeatButton.setStyle("-fx-background-color: silver;");
            }
        }
    }
}
