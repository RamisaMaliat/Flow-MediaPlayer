package MediaPlayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.io.File;
import java.net.URL;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

/**
 * This is controller class for CreateAudioPlaylist.fxml
 */
public class CreateAudioPlaylistController implements Initializable {
    private String path;
    @FXML
    private ListView listView;
    @FXML
    private MenuBar myMenuBar;
    @FXML
    private TextField text;
    List <File> list;
    ObservableList observableList = FXCollections.observableArrayList();
    Stage stage;

    /**
     * This function initializes the controller class of CreateAudioPlaylist.fxml
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        list = new ArrayList<>();
    }

    /**
     * This function adds a file to audio playlist
     * @param event
     */
    public void AddFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a file", "*.mp3", "*.wav", "*.aa",
                "*.aac", "*.aax", "*.aiff",	"*.amr", "*.ape", "*.awb", "*.dss","*.dvf","*.flac","*.gsm","*.iklax","*.ivs",
                "*.m4a","*.m4b","*.m4p","*.mmf","*.mpc", "*.opus","*.org","*.ra", "*.msv","*.nmf","*.org",
                "*.rm","*.raw","*.rf64","*.sln","*.tta","*.voc","*.wma","*.wv","*.cda");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        if(file != null){
            boolean add = list.add(file);
            observableList.setAll(list);
            listView.setItems(observableList);
        }
    }

    /**
     * This function adds multiple files to audio playlist
     * @param event
     */
    public void AddFiles(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Select a file", "*.mp3", "*.wav", "*.aa",
                "*.aac", "*.aax", "*.aiff",	"*.amr", "*.ape", "*.awb", "*.dss","*.dvf","*.flac","*.gsm","*.iklax","*.ivs",
                "*.m4a","*.m4b","*.m4p","*.mmf","*.mpc", "*.opus","*.org","*.ra", "*.msv","*.nmf","*.org",
                "*.rm","*.raw","*.rf64","*.sln","*.tta","*.voc","*.wma","*.wv","*.cda");
        fileChooser.getExtensionFilters().add(filter);
        List<File> files = fileChooser.showOpenMultipleDialog(null);
        if(files != null){
            boolean add = list.addAll(files);
            observableList.setAll(list);
            listView.setItems(observableList);
        }
    }

    /**
     * This function deletes a file from audio playlist
     * @param event
     */
    public void DeleteFile(ActionEvent event){
        int idx = listView. getSelectionModel(). getSelectedIndex();
        list.remove(idx);
        observableList.setAll(list);
        listView.setItems(observableList);
    }

    /**
     * This function deletes all file from audio playlist
     * @param event
     */
    public void DeleteAll(ActionEvent event){
        list.removeAll(list);
        observableList.setAll(list);
        listView.setItems(observableList);
    }

    /**
     * This function creates a new audio playlist
     * @param event
     * @throws IOException
     */
    public void Create(ActionEvent event) throws IOException {
        String username = "flowAudio";
        String password = "audio";
        String name = text.getText().strip();
        if(list.size() == 0){
            stage = new Stage();
            stage.setTitle("Playlist Not Created!");
            Parent root = FXMLLoader.load(getClass().getResource("NoFile.fxml"));
            stage.resizableProperty().setValue(Boolean.FALSE);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        else if(name.equals("")) {
            stage = new Stage();
            stage.setTitle("Playlist Not Created!");
            Parent root = FXMLLoader.load(getClass().getResource("PlaylistName.fxml"));
            stage.resizableProperty().setValue(Boolean.FALSE);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }
        else {
            try{
                String url = "jdbc:oracle:thin:@localhost:1521/XE";
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection con = DriverManager.getConnection(url,username,password);
                Statement statement = con.createStatement();
                String query = "";
                try{
                    query = "CREATE TABLE "+name+"(files VARCHAR2(3000) NOT NULL)";
                    statement.execute(query);
                    for(int i = 0; i<list.size(); i++){
                        query = "INSERT INTO "+name+"(files) VALUES (" +"'"+list.get(i).toString()+"')";
                        statement.execute(query);
                    }
                    stage = new Stage();
                    stage.setTitle("Playlist Created!");
                    Parent root = FXMLLoader.load(getClass().getResource("PlaylistCreated.fxml"));
                    stage.resizableProperty().setValue(Boolean.FALSE);
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                }
                catch (Exception e){
                    stage = new Stage();
                    stage.setTitle("Playlist Not Created!");
                    Parent root = FXMLLoader.load(getClass().getResource("PlaylistName2.fxml"));
                    stage.resizableProperty().setValue(Boolean.FALSE);
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();
                }
            }
            catch(SQLException e){ }
            catch (ClassNotFoundException e){ }
        }

    }

    /**
     * This function closes confirmatian window
     * @param event
     */
    public void OK(ActionEvent event){
        Stage stage =(Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    /**
     * This function takes back to previous window
     * @param event
     * @throws IOException
     */
    public void Back(ActionEvent event) throws IOException{
        Stage HomePage = (Stage) myMenuBar.getScene().getWindow();
        HomePage.setTitle("");
        Parent root = FXMLLoader.load(getClass().getResource("MediaPlayer.fxml"));
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
        Stage HomePage = (Stage) myMenuBar.getScene().getWindow();
        HomePage.setTitle("");
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
        HomePage.setScene(scene);
        HomePage.resizableProperty().setValue(Boolean.FALSE);
        HomePage.show();
    }
}