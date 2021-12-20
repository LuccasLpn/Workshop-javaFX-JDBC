package application;


import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application {
    
    
    
    private static Scene mainscene;
    
    @Override
    public void start(Stage primaryStage) {
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
            ScrollPane scrollpane = loader.load();     
            scrollpane.setFitToHeight(true);
            scrollpane.setFitToWidth(true);
            
            mainscene = new Scene(scrollpane);
            
            primaryStage.setScene(mainscene);
            primaryStage.setTitle("Sample JavaFX application");
            primaryStage.show();
            
            
        } catch (IOException e) {
            
            
            e.printStackTrace();
            
        }
    }
    
    public static Scene getMainScene(){
        return mainscene;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
