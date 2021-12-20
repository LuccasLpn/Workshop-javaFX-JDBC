package gui.Controllers;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmenteService;

public class DepartmentFormController implements Initializable{
    
    
    private Department entity;
    private DepartmenteService service;

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
    public void SetDepartmentService(DepartmenteService service){
        this.service = service;
    }
    
    @FXML
    public void onBtSaveAction(ActionEvent event){

        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            Utils.currentStage(event).close();
        } catch (DbException e) {
            Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
        }
        
 
    }
    
    @FXML
    public void onBtCancelAction(ActionEvent event){
        
        Utils.currentStage(event).close();
        
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

    private Department getFormData() {     
        
        Department obj = new Department();

        obj.setId(Utils.tryParseToInt(txtid.getText()));
        obj.setName(txtname.getText());

        return obj;
      
    }

}
