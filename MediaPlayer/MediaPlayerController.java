package MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.EqualizerBand;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;

/**
 * This is controller class for MediaPlayer.fxml
 */
public class MediaPlayerController implements Initializable {

    @FXML
    private MediaView mediaView;

    @FXML
    private MenuBar myMenuBar;

    @FXML
    private Button RepeatButton;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider progressBar;

    @FXML
    private Label totalTime;

    @FXML
    private StackPane pane;

    @FXML
    private ImageView imageView;

    private Slider[] sliders = new Slider[10];
    Label balance = new Label("Balance");
    Label volume = new Label("Volume");
    Label eq = new Label("EQUALIZER");
    Label[] freq = new Label[10];
    private DoubleProperty leftVU = new SimpleDoubleProperty(0);
    private DoubleProperty rightVU = new SimpleDoubleProperty(0);
    private Meter[] meters = new Meter[10];
    private AudioSpectrumListener spectrumListener;
    private Slider balanceKnob, volumeKnob;
    private String path;
    private MediaPlayer mediaPlayer;

    /**
     * This function initializes the controller class of MediaPlayer.fxml
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            File file = new File(".\\src\\MediaPlayer\\resources\\audio.png");
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
    }

    /**
     * This function opens an audio file
     * @param event
     */
    @FXML
    private void OpenFileMethod(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a file", "*.mp3", "*.wav", "*.aa",
                "*.aac", "*.aax", "*.aiff",	"*.amr", "*.ape", "*.awb", "*.dss","*.dvf","*.flac","*.gsm","*.iklax","*.ivs",
                "*.m4a","*.m4b","*.m4p","*.mmf","*.mpc", "*.opus","*.org","*.ra", "*.msv","*.nmf","*.org",
                "*.rm","*.raw","*.rf64","*.sln","*.tta","*.voc","*.wma","*.wv","*.cda");
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
     * This function pauses the audio
     * @param event
     */
    public void pauseAudio(ActionEvent event){
        if(mediaPlayer != null) mediaPlayer.pause();
    }

    /**
     * This function stops the audio
     * @param event
     */
    public void stopAudio(ActionEvent event){
        if(mediaPlayer != null) mediaPlayer.stop();
    }

    /**
     * This function plays the audio at normal rate
     * @param event
     */
    public void playAudio(ActionEvent event){
        if(mediaPlayer != null){
            mediaPlayer.play();
            mediaPlayer.setRate(1);
        }
    }

    /**
     * This function skips 5 sec of the audio
     * @param event
     */
    public void skip5(ActionEvent event){
        if(mediaPlayer != null) mediaPlayer.seek(mediaPlayer.getCurrentTime().add(javafx.util.Duration.seconds(5)));
    }

    /**
     * This function speeds up the audio
     * @param event
     */
    public void furtherSpeedUpAudio(ActionEvent event){
        if(mediaPlayer != null) mediaPlayer.setRate(2);
    }

    /**
     * This function takes the audio 5 sec back
     * @param event
     */
    public void back5(ActionEvent event){
        if(mediaPlayer != null) mediaPlayer.seek(mediaPlayer.getCurrentTime().add(javafx.util.Duration.seconds(-5)));
    }

    /**
     * This function slows down the audio
     * @param event
     */
    public void furtherSlowDownAudio(ActionEvent event){
        if(mediaPlayer != null) mediaPlayer.setRate(0.5);
    }

    /**
     * This function creates the window for creating audio playlist
     * @param event
     * @throws Exception
     */
    public void createAudioPlaylist(ActionEvent event) throws Exception{
        try{
            mediaPlayer.stop();
        }
        catch(Exception e){}
        Stage HomePage = (Stage) myMenuBar.getScene().getWindow();
        HomePage.setTitle("");
        Parent root = FXMLLoader.load(getClass().getResource("CreateAudioPlaylist.fxml"));
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
     * This function creates a window for viewing audio playlists
     * @param event
     * @throws IOException
     */
    public void viewAudioPlaylists(ActionEvent event) throws IOException {
        try{
            mediaPlayer.stop();
        }
        catch(Exception e){}
        Stage HomePage = (Stage) myMenuBar.getScene().getWindow();
        HomePage.setTitle("");
        Parent root = FXMLLoader.load(getClass().getResource("ViewAudioPlaylists.fxml"));
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
        HomePage.resizableProperty().setValue(Boolean.FALSE);
        HomePage.show();
    }

    /**
     * This function increases the volume of the audio
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
     * This function decreases the volume of the audio
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
     * This function mutes the audio
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

    /**
     * This function opens a new window with equalizer for the audio and
     * contains all OnAction functions for it
     * @param event
     * @throws Exception
     */
    public void Equalizer(ActionEvent event) throws Exception {
        if(mediaPlayer != null){
            Stage HomePage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            HomePage.resizableProperty().setValue(Boolean.FALSE);
            HomePage.setTitle("");
            Button playBtn = new Button();
            playBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try{
                        mediaPlayer.play();
                    }
                    catch (Exception e) {}
                }
            });

            Button pauseBtn = new Button();
            pauseBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try{
                        mediaPlayer.pause();
                    }
                    catch (Exception e) {}
                }
            });

            Button stopBtn = new Button();
            stopBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    }
                    Stage HomePage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    HomePage.setTitle("");
                    HomePage.resizableProperty().setValue(Boolean.TRUE);
                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("MediaPlayer.fxml"));
                    } catch (IOException e) {
                    }
                    Scene scene = new Scene(root);
                    scene.setOnMouseClicked(e -> {
                        if (e.getClickCount() == 2) {
                            HomePage.setFullScreen(true);
                        }
                    });
                    scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
                    HomePage.setScene(scene);
                    HomePage.show();
                }
            });

            for (int i = 0; i < 10; i++) {
                freq[i] = new Label();
            }
            freq[0].setText("32 Hz");
            freq[1].setText("64 Hz");
            freq[2].setText("125 Hz");
            freq[3].setText("250 Hz");
            freq[4].setText("500 Hz");
            freq[5].setText("1 kHz");
            freq[6].setText("2 kHz");
            freq[7].setText("4 kHz");
            freq[8].setText("8 kHz");
            freq[9].setText("16 kHz");

            for (int i = 0; i < 10; i++) {
                final int fi = i;
                sliders[i] = new Slider(EqualizerBand.MIN_GAIN, EqualizerBand.MAX_GAIN, 0);
                sliders[i].setId("s"+i);
                sliders[i].setOrientation(Orientation.VERTICAL);
                sliders[i].valueProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
                        if (mediaPlayer != null) {
                            mediaPlayer.getAudioEqualizer().getBands().get(fi).setGain(newValue.doubleValue());
                        }
                    }
                });
            }

            for (int j = 0; j < 10; j++) {
                meters[j] = new Meter();
            }

            spectrumListener = new AudioSpectrumListener() {
                @Override
                public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
                    double avarage = 0;
                    for (int i = 0; i < 10; i++) {
                        meters[i].setValue((60 + magnitudes[i]) / 60);
                        if (i < 3) avarage += ((60 + magnitudes[i]) / 60);
                    }

                    // make up meter values
                    avarage = avarage / 3;
                    avarage *= volumeKnob.getValue();
                    if (balanceKnob.getValue() == 0) {
                        leftVU.set(avarage);
                        rightVU.set(avarage);
                    } else if (balanceKnob.getValue() > 0) {
                        leftVU.set(avarage * (1 - balanceKnob.getValue()));
                        rightVU.set(avarage);
                    } else if (balanceKnob.getValue() < 0) {
                        leftVU.set(avarage);
                        rightVU.set(avarage * (balanceKnob.getValue() + 1));
                    }
                }
            };
            balanceKnob = new Slider(-1, 1, 0);
            balanceKnob.setBlockIncrement(0.1);
            balanceKnob.setId("balance");
            balanceKnob.getStyleClass().add("knobStyle");
            balanceKnob.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {
                    if (mediaPlayer != null) mediaPlayer.setBalance(newValue.doubleValue());
                }
            });
            volumeKnob = new Slider(0, 1, 1);
            volumeKnob.setBlockIncrement(0.1);
            volumeKnob.setId("volume");
            volumeKnob.getStyleClass().add("knobStyle");
            volumeKnob.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newValue) {
                    if (mediaPlayer != null) mediaPlayer.setVolume(newValue.doubleValue());
                }
            });

            Group root = new Group();
            root.setAutoSizeChildren(false);
            root.getChildren().addAll(playBtn, stopBtn, pauseBtn, balanceKnob, volumeKnob, volume, balance, eq);
            root.getChildren().addAll(freq);
            root.getChildren().addAll(sliders);
            root.getChildren().addAll(meters);

            for (int i = 0; i < 10; i++) {
                sliders[i].resizeRelocate(43 + (58 * i), 228 - 20, 53, 181 + 40);
            }

            playBtn.resizeRelocate(210, 500, 70, 30);
            playBtn.setText("Play");
            pauseBtn.resizeRelocate(310, 500, 70, 30);
            pauseBtn.setText("Pause");
            stopBtn.resizeRelocate(410, 500, 70, 30);
            stopBtn.setText("Stop");

            balanceKnob.resizeRelocate(160, 440, 87, 60);
            balance.resizeRelocate(110, 448, 80, 40);
            volumeKnob.resizeRelocate(420, 440, 175, 60);
            volume.resizeRelocate(375, 448, 80, 40);
            eq.resizeRelocate(0, 0, 680, 60);
            eq.setAlignment(Pos.CENTER);
            eq.setId("EqLabel");
            balance.setId("b1");
            volume.setId("v1");

            for (int i = 0; i < 10; i++) {
                meters[i].setLayoutX(70 + (58 * i));
                meters[i].setLayoutY(170);
                freq[i].resizeRelocate(55 + (58 * i), 430, 50, 30);
            }

            Scene scene = new Scene(root, 660, 550);
            scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
            HomePage.setScene(scene);
            HomePage.show();

            mediaPlayer.setAudioSpectrumNumBands(10);
            mediaPlayer.setAudioSpectrumListener(spectrumListener);
            mediaPlayer.setAudioSpectrumInterval(1d / 30d);
        }
    }

}