package gui;

import application.Main;
import gui.Controllers.DepartmentFormController;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmenteService;

public class DepartmentListController implements Initializable, DataChangeListener {

    private DepartmenteService service;

    @FXML
    private TableView<Department> tableViewDepartment;

    @FXML
    private TableColumn<Department, Integer> tableColumnId;

    @FXML
    private TableColumn<Department, String> tableColumnName;

    @FXML
    private Button btn1;

    private ObservableList<Department> obslist;

    @FXML
    public void onBtNewAction(ActionEvent event) {
        
        Stage parentStage = Utils.currentStage(event);
        Department obj = new Department();
        createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
        
    }

    public void setDepartementeService(DepartmenteService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializenODES();

    }

    private void initializenODES() {

        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());

    }

    public void updateTableView() {
        if (service == null) {

            throw new IllegalStateException("Service was Null");
        }

        List<Department> list = service.findAll();
        obslist = FXCollections.observableArrayList(list);
        tableViewDepartment.setItems(obslist);

    }

    private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();
            DepartmentFormController controller = loader.getController();
            controller.setDepartment(obj);
            controller.SetDepartmentService(new DepartmenteService());
            controller.subscribeDataChangeListeners(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Departmente Data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        } catch (IOException e) {

            Alerts.showAlert("IO Expectuion", null, e.getMessage(), Alert.AlertType.ERROR);

        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();

    }

}