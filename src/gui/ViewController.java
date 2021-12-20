package gui;

import application.Main;
import gui.util.Alerts;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmenteService;



public class ViewController implements Initializable {

    @FXML
    private MenuItem menuitemSeller;
    
    @FXML
    private MenuItem menuitemDepartment;
    
    @FXML
    private MenuItem menuitemAbout;
    
    
    @FXML
    public void onMenuItemSellerAction(){
        System.out.println("onMenuItemSellerAction");
    }
    
    @FXML
    public void onMenuItemDepartmentAction() {
        loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
        controller.setDepartementeService(new DepartmenteService());
        controller.updateTableView();});
    }  
    
    @FXML
    public void onMenuItemAbountAction(){
        loadView("/gui/About.fxml", x -> {});
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    
    private synchronized <T> void loadView(String absoluteName, Consumer<T> initiazingAction){

        try {
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newBox = loader.load();
            Scene mainscene = Main.getMainScene();
            VBox mainVBox = (VBox)((ScrollPane) mainscene.getRoot()).getContent();
            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newBox.getChildren());
            
            T controller = loader.getController();
            initiazingAction.accept(controller);
            
            
        } catch (Exception e) {
            
            Alerts.showAlert("IO Exception ", "Error Loading View", e.getMessage(), Alert.AlertType.ERROR);
            
        }
   
    }
}