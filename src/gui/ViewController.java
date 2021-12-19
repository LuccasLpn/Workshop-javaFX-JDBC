package gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
    public void onMenuItemDepartmentAction(){
       System.out.println("onMenuItemDepartmentAction");
    }      
    
    @FXML
    public void onMenuItemAbountAction(){
        System.out.println("onMenuItemAbountAction");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
}
