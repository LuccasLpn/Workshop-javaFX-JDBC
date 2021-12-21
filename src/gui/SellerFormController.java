package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

    private Seller entity;

    private SellerService service;

    private DepartmentService departmentService;

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
    private ComboBox<Department> comboBoxDepartment;

    @FXML
    private Label labelerrorname;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    @FXML
    private ObservableList<Department> obsList;

    public void setSeller(Seller entity) {
        this.entity = entity;
    }

    public void subscribeDataChangeListeners(DataChangeListener listener) {
        dataChangeListeners.add(listener);

    }

    public void SetServices(SellerService service, DepartmentService departmentService) {
        this.departmentService = departmentService;
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
        initializeComboBoxDepartment();

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

        if (entity.getBirthDate() != null) {

            dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));

        }

        if (entity.getDepartment() == null) {

            comboBoxDepartment.getSelectionModel().selectFirst();

        } else {

            comboBoxDepartment.setValue(entity.getDepartment());

        }

    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        labelerrorname.setText(fields.contains("name") ? errors.get("Name") : "");
        labelerrorname.setText(fields.contains("email") ? errors.get("email") : "");
        labelerrorname.setText(fields.contains("baseSalarry") ? errors.get("Basesalary") : "");
        labelerrorname.setText(fields.contains("BirthDate") ? errors.get("BirthDate") : "");

    }

    private Seller getFormData() {

        Seller obj = new Seller();

        ValidationException exception = new ValidationException("Validation Error");

        obj.setId(Utils.tryParseToInt(txtid.getText()));

        if (txtname.getText() == null || txtname.getText().trim().equals("")) {

            exception.addError("Name", "Field can´t be empty ");

        } else {

            obj.setName(txtname.getText());

        }

        if (txtemail.getText() == null || txtemail.getText().trim().equals("")) {

            exception.addError("email", "Field can´t be empty ");

        }
        obj.setEmail(txtemail.getText());

        if (dpBirthDate.getValue() == null) {

            exception.addError("BirthDate", "Field can´t be empty ");
        } else {
            Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
            obj.setBirthDate(Date.from(instant));

        }

        if (baseSalary.getText() == null || baseSalary.getText().trim().equals("")) {

            exception.addError("baseSalarry", "Field can´t be empty ");

        }
        obj.setBaseSalary(Utils.tryParseDouble(baseSalary.getText()));
        
        obj.setDepartment(comboBoxDepartment.getValue());

        if (exception.getErrors().size() > 0) {

            throw exception;
        }

        return obj;

    }

    public void loadAssociatedObjects() {

        if (departmentService == null) {

            throw new IllegalStateException("DepartmentService was null ");
        }

        List<Department> list = departmentService.findAll();
        obsList = FXCollections.observableArrayList(list);
        comboBoxDepartment.setItems(obsList);

    }

    private void notifyDataChangeListeners() {

        for (DataChangeListener listener : dataChangeListeners) {

            listener.onDataChanged();

        }

    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        comboBoxDepartment.setCellFactory(factory);
        comboBoxDepartment.setButtonCell(factory.call(null));
    }

}
