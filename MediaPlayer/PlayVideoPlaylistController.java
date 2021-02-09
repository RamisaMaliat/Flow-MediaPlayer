package MediaPlayer;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * This is controller class for PlayVideoPlaylist.fxml
 */
public class PlayVideoPlaylistController implements Initializable {
    List<String> list;
    ObservableList observableList = observableArrayList();
    Stage stage;
    public boolean listVisible;
    @FXML
    private ListView listView;

    @FXML
    private VBox vboxView;

    @FXML
    private Button listButton;

    @FXML
    private Label title;

    private String path;
    private MediaPlayer mediaPlayer;

    @FXML
    private MediaView mediaView;

    @FXML
    private MenuBar myMenuBar;

    @FXML
    private Button openFile;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider progressBar;

    @FXML
    private Label label;

    @FXML
    private StackPane pane;

    @FXML
    private ImageView imageView;
    /**
     * This function initializes the controller class of PlayVideoPlaylist.fxml.
     * It connects with the database and fetches the files of the playlist
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        title.setText("Currently Playling : "+ViewVideoPlaylistController.playlistname);
        listVisible = true;
        list = new ArrayList<>();
        observableList = observableArrayList();
        String username = "flowVideo";
        String password = "video";
        String p = ViewVideoPlaylistController.playlistname;
        ResultSet result = null;

        try{
            String u = "jdbc:oracle:thin:@localhost:1521/XE";
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(u,username,password);
            Statement statement = con.createStatement();
            String query = "SELECT files FROM "+p;
            result = statement.executeQuery(query);
            while(result.next()){
                p = result.getString(1);
                list.add(p);
            }
            observableList.setAll(list);
            listView.setItems(observableList);
            InitialPlayVideo();
        }
        catch(SQLException e){ }
        catch(ClassNotFoundException e){ }

    }
    /**
     * This function plays the next file in the playlist
     * @param event
     */
    public void nextFile(ActionEvent event){
        int idx = listView.getSelectionModel().getSelectedIndex() + 1;
        if(idx == list.size()) idx = 0;

        try{
            mediaPlayer.stop();
        }
        catch (Exception e) {}
        try{
            path = list.get(idx);
            listView.getSelectionModel().select(idx);
            path = "file:/" + path.replace("\\", "/");
            //System.out.println(path);
            if(path != null){
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
                        javafx.util.Duration total = media.getDuration();
                        progressBar.setMax(total.toSeconds());
                    }
                });

                mediaPlayer.play();
                mediaPlayer.setRate(1);
                idx ++;
                final int next;
                if(idx == list.size()) next = 0;
                else next = idx;
                mediaPlayer.setOnEndOfMedia(() -> {
                    listView. getSelectionModel().select(next);
                    ActionEvent e = null;
                    playVideo(e);
                });
            }
        }
        catch (Exception e){
            ActionEvent temp = null;
            nextFile(temp);
        }
    }

    public void previousFile(ActionEvent event){
        int idx = listView.getSelectionModel().getSelectedIndex() - 1;
        if(idx == -1) idx = list.size() - 1;
        try{
            mediaPlayer.stop();
        }
        catch (Exception e) {}
        try{
            listView.getSelectionModel().select(idx);
            path = list.get(idx);
            path = "file:/" + path.replace("\\", "/");
            //System.out.println(path);
            if(path != null){
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
                        javafx.util.Duration total = media.getDuration();
                        progressBar.setMax(total.toSeconds());
                    }
                });

                mediaPlayer.play();
                mediaPlayer.setRate(1);
                idx ++;
                final int next;
                if(idx == list.size()) next = 0;
                else next = idx;
                mediaPlayer.setOnEndOfMedia(() -> {
                    listView. getSelectionModel().select(next);
                    ActionEvent e = null;
                    playVideo(e);
                });
            }
        }
        catch (Exception e){
            ActionEvent temp = null;
            previousFile(temp);
        }
    }
    /**
     * This function plays the video
     * @param event
     */
    public void playVideo(ActionEvent event) {
        try{
            mediaPlayer.stop();
        }
        catch (Exception e) {}
        int idx = listView. getSelectionModel(). getSelectedIndex();
        try{
            if(idx == -1||idx == list.size()) idx = 0;
            path = list.get(idx);
            path = "file:/" + path.replace("\\", "/");

            if(path != null){
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
                        javafx.util.Duration total = media.getDuration();
                        progressBar.setMax(total.toSeconds());
                    }
                });

                mediaPlayer.play();
                mediaPlayer.setRate(1);
                idx ++;
                final int next;
                if(idx == list.size()) next = 0;
                else next = idx;
                mediaPlayer.setOnEndOfMedia(() -> {
                    listView. getSelectionModel().select(next);
                    ActionEvent e = null;
                    playVideo(e);
                });
            }
        }
        catch (Exception e){
            ActionEvent temp = null;
            nextFile(temp);
        }
    }
    /**
     * This function skips 5 sec of the video
     * @param event
     */
    public void skip5(ActionEvent event){
        try{
            mediaPlayer.seek(mediaPlayer.getCurrentTime().add(javafx.util.Duration.seconds(5)));
        }
        catch (Exception e) {}
    }
    /**
     * This function speeds up the video
     * @param event
     */
    public void furtherSpeedUpAudio(ActionEvent event){
        try{
            mediaPlayer.setRate(2);
        }
        catch (Exception e) {}
    }
    /**
     * This function takes the video 5 sec back
     * @param event
     */
    public void back5(ActionEvent event){
        try{
            mediaPlayer.seek(mediaPlayer.getCurrentTime().add(javafx.util.Duration.seconds(-5)));
        }
        catch (Exception e) {}

    }
    /**
     * This function slows down the video
     * @param event
     */
    public void furtherSlowDownAudio(ActionEvent event){
        try{
            mediaPlayer.setRate(0.5);
        }
        catch (Exception e) {}

    }
    /**
     * This function creates a window for viewing video playlists
     * @param event
     * @throws IOException
     */
    public void viewVideoPlaylists(ActionEvent event) throws IOException {
        try{mediaPlayer.stop();}
        catch(Exception e){}
        Stage HomePage = (Stage) myMenuBar.getScene().getWindow();
        HomePage.setTitle("Media Player");
        Parent root = FXMLLoader.load(getClass().getResource("ViewVideoPlaylists.fxml"));
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
     * This function stops the video
     * @param event
     */
    public void stopVideo(ActionEvent event){
        try{
            mediaPlayer.stop();
        }
        catch (Exception e) {}
    }
    /**
     * This function plays the initial video file
     */
    public void InitialPlayVideo() {
        int idx = listView. getSelectionModel(). getSelectedIndex();
        try{
            if(idx == -1) idx = 0;
            listView.getSelectionModel().select(idx);
            path = list.get(idx);
            path = "file:/" + path.replace("\\", "/");

            if(path != null){
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
                        javafx.util.Duration total = media.getDuration();
                        progressBar.setMax(total.toSeconds());
                    }
                });

                mediaPlayer.play();
                final int next = idx + 1;
                mediaPlayer.setOnEndOfMedia(() -> {
                    listView. getSelectionModel().select(next);
                    ActionEvent event = null;
                    playVideo(event);
                });
            }
        }
        catch (Exception e){
            ActionEvent temp = null;
            nextFile(temp);
        }
    }

    /**
     * This function controls visibility of the playlist details
     * @param event
     */
    public void visibleList(ActionEvent event){
        if(listVisible) closeList();
        else showList();
    }
    /**
     * This function hides the playlist details
     */
    public void closeList() {
        listView.setPrefWidth(0);
        listView.setPrefHeight(0);
        vboxView.setPrefWidth(0);
        vboxView.setPrefHeight(0);
        listButton.setText("+ Show playlist details");
        listVisible = false;

    }
    /**
     * This function shows the playlist details
     */
    public void showList() {
        listView.setPrefWidth(262);
        listView.setPrefHeight(355);
        vboxView.setPrefWidth(262);
        vboxView.setPrefHeight(355);
        listButton.setText("X Close playlist details");
        listVisible = true;
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
        Parent root = FXMLLoader.load(getClass().getResource("MediaPlayer2.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        scene.setOnMouseClicked(e -> {
            if(e.getClickCount() == 2){
                HomePage.setFullScreen(true);
            }
        });
        HomePage.setScene(scene);
        HomePage.show();
    }
    /**
     * This function takes back to main menu
     * @param event
     * @throws IOException
     */
    public void ToMenu(ActionEvent event) throws IOException{
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
     * This function increases the volume of the sound
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
     * This function decreases the volume of the sound
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
}