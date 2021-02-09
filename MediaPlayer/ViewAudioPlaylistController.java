package MediaPlayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import static javafx.collections.FXCollections.observableArrayList;

/**
 * This is controller class for ViewAudioPlaylists.fxml
 */
public class ViewAudioPlaylistController implements Initializable {
    static public String playlistname = "";
    List<String> list;
    ObservableList observableList;
    Stage stage;
    @FXML
    private ListView listView;

    /**
     * This function initializes the controller class of ViewAudioPlaylists.fxml.
     * It connects with the database and fetches the audio playlists
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        list = new ArrayList<>();
        observableList = observableArrayList();
        String username = "flowAudio";
        String password = "audio";
        try{
            String u = "jdbc:oracle:thin:@localhost:1521/XE";
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(u,username,password);
            Statement statement = con.createStatement();
            String query = "SELECT table_name FROM user_tables";
            ResultSet result = statement.executeQuery(query);
            String p = "";
            while(result.next()){
                p = result.getString(1);
                list.add(p);
            }
            for(int i=0;i<list.size();i++){
                p = list.get(i);
                query = "SELECT COUNT(*) FROM "+p;
                Long total = 0L;
                ResultSet r = statement.executeQuery(query);
                while(r.next()){
                    total = r.getLong(1);
                }
                list.set(i, String.format("%s   (Total files : %d)", p, total));
            }
            observableList.setAll(list);
            listView.setItems(observableList);
        }
        catch(SQLException e){ }
        catch(ClassNotFoundException e){ }
    }

    /**
     * This function deletes a playlist
     * @param event
     * @throws IOException
     */
    public void deletePlaylist(ActionEvent event) throws IOException {
        try{
            int idx = listView. getSelectionModel(). getSelectedIndex();
            String p = list.get(idx);
            int space = p.indexOf(" ");
            p = p.substring(0,space);
            String username = "flowAudio";
            String password = "audio";
            try{
                String url = "jdbc:oracle:thin:@localhost:1521/XE";
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection con = DriverManager.getConnection(url,username,password);
                Statement statement = con.createStatement();
                String query = "";
                query = ("DROP TABLE "+p);
                int i = statement.executeUpdate(query);
                list.remove(idx);
                observableList.setAll(list);
                listView.setItems(observableList);
            }
            catch(SQLException e){
                System.out.println(1);
            }
            catch (ClassNotFoundException e){ }
        }
        catch(Exception e) { }
    }

    /**
     * This function plays the playlist
     * @param event
     * @throws IOException
     */
    public void playPlaylist(ActionEvent event) throws IOException{
        int idx = listView. getSelectionModel(). getSelectedIndex();
        if(idx!=-1){
            String p = list.get(idx);
            int space = p.indexOf(" ");
            playlistname = p.substring(0,space);
            Stage HomePage = (Stage)((Node) event.getSource()).getScene().getWindow();
            HomePage.setTitle("");
            Parent root = FXMLLoader.load(getClass().getResource("PlayAudioPlaylist.fxml"));
            Scene scene = new Scene(root);
            scene.setOnMouseClicked(e -> {
                if(e.getClickCount() == 2){
                    HomePage.setFullScreen(true);
                }
            });
            scene.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
            HomePage.setScene(scene);
            HomePage.show();}
    }
    /**
     * This function takes back to previous window
     * @param event
     * @throws IOException
     */
    public void Back(ActionEvent event) throws IOException{
        Stage HomePage =(Stage)((Node) event.getSource()).getScene().getWindow();
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
     * This function creates the window for creating audio playlist
     * @param event
     * @throws Exception
     */
    public void createAudioPlaylist(ActionEvent event) throws Exception{
        Stage HomePage = (Stage)((Node) event.getSource()).getScene().getWindow();
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
}