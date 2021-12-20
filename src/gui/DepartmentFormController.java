package gui.Controllers;

import gui.util.Constraints;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable{
    
    
    private Department entity;

    @FXML
    private TextField txtid;
    
    @FXML
    private TextField txtname;
    
    @FXML
    private Label labelerrorname;
    
    
    @FXML
    private Button btSave;
    
    @FXML
    private Button btCancel;
    
    public void setDepartment(Department entity){
        this.entity = entity;
    }
    
    @FXML
    public void onBtSaveAction(){
        System.out.println("onBtSaveAction");
    }
    
    @FXML
    public void onBtCancelAction(){
        System.out.println("onBtCancelAction");
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtid);
        Constraints.setTextFieldMaxLength(txtname, 30);
    }
    
    public void updateFormData(){
        
        if(entity == null ){
            
            throw new IllegalStateException("Entity was null");
        }
        
        txtid.setText(String.valueOf(entity.getId()));
        txtname.setText(entity.getName());
        
    }

}
