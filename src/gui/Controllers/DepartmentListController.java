package gui;

import application.Main;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmenteService;

public class DepartmentListController implements Initializable{
    
    
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
    public void onBtNewAction(){   
        System.out.println("onBtNewAction"); 
    }

    public void setDepartementeService(DepartmenteService service){
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
    
    
    
    
    
}
