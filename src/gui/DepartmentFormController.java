package gui.Controllers;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmenteService;

public class DepartmentFormController implements Initializable{
    
    
    private Department entity;
    
    private DepartmenteService service;
    
    
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

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
    
    public void subscribeDataChangeListeners(DataChangeListener listener){
      dataChangeListeners.add(listener);
               
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
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
   
        } catch (ValidationException e) {
            
            setErrorMessages(e.getErrors());
            
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
    
    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        if (fields.contains("Name")) {
            
            labelerrorname.setText(errors.get("Name"));
        }
    }


    private Department getFormData() {     
        
        Department obj = new Department();
        
        ValidationException exception = new ValidationException("Validation Error");
        
        obj.setId(Utils.tryParseToInt(txtid.getText()));
        
        if(txtname.getText() == null || txtname.getText().trim().equals("")){
            
        exception.addError("Name", "Field can´t be empty ");
            
        }
        
        obj.setName(txtname.getText());
        
        if(exception.getErrors().size() > 0){
            
            throw exception;
        }
        
        return obj;
      
    }

    private void notifyDataChangeListeners() {
        
    for(DataChangeListener listener: dataChangeListeners){
        
        listener.onDataChanged();
        
    }
    
    }


}