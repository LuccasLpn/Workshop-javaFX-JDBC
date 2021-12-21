package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {

    private Seller entity;

    private SellerService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtid;

    @FXML
    private TextField txtname;
    @FXML
    private TextField txtemail;
    @FXML
    private DatePicker dpBirthDate;
    @FXML
    private TextField baseSalary;

    @FXML
    private Label labelerrorname;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    public void setSeller(Seller entity) {
        this.entity = entity;
    }

    public void subscribeDataChangeListeners(DataChangeListener listener) {
        dataChangeListeners.add(listener);

    }

    public void SetSellerService(SellerService service) {
        this.service = service;
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {

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
    public void onBtCancelAction(ActionEvent event) {

        Utils.currentStage(event).close();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        initializeNodes();
    }

    private void initializeNodes() {

        Constraints.setTextFieldInteger(txtid);
        Constraints.setTextFieldMaxLength(txtname, 70);
        Constraints.setTextFieldDouble(baseSalary);
        Constraints.setTextFieldMaxLength(txtemail, 60);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");

    }

    public void updateFormData() {

        if (entity == null) {

            throw new IllegalStateException("Entity was null");
        }

        txtid.setText(String.valueOf(entity.getId()));

        txtname.setText(entity.getName());
        txtemail.setText(entity.getEmail());
        Locale.setDefault(Locale.US);
        baseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
        
        if(entity.getBirthDate() != null ){
            
        dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));   
        
        }
        
        
        

    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        if (fields.contains("Name")) {

            labelerrorname.setText(errors.get("Name"));
        }
    }

    private Seller getFormData() {

        Seller obj = new Seller();

        ValidationException exception = new ValidationException("Validation Error");

        obj.setId(Utils.tryParseToInt(txtid.getText()));

        if (txtname.getText() == null || txtname.getText().trim().equals("")) {

            exception.addError("Name", "Field can´t be empty ");

        }

        obj.setName(txtname.getText());

        if (exception.getErrors().size() > 0) {

            throw exception;
        }

        return obj;

    }

    private void notifyDataChangeListeners() {

        for (DataChangeListener listener : dataChangeListeners) {

            listener.onDataChanged();

        }

    }

}
