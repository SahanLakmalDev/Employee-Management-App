package lk.ijse.dep11.emp_app.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dep11.emp_app.tm.Employee;

import java.util.List;

public class MainViewController {
    public AnchorPane root;
    public TextField txtId;
    public TextField txtName;
    public TextField txtContact;
    public Button btnSave;
    public Button btnDelete;
    public TableView<Employee> tbvEmployees;
    public TextField txtSearch;
    public Button btnNew;

    public void btnSaveOnAction(ActionEvent actionEvent) {
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
    }

    public void btnNewOnAction(ActionEvent actionEvent) {
    }

    private List<Employee> getEmployeeList(){
        return tbvEmployees.getItems();
    }

    private boolean isDataValid(){
        //Name Validation
        if(!txtName.getText().strip().matches("[a-zA-Z\\s]+")){
            txtName.requestFocus();
            txtName.selectAll();
            return false;
        }
        //Contact Number Validation
        if(!txtContact.getText().strip().matches("0\\d{2}-\\d{7}")){
            txtContact.requestFocus();
            txtContact.selectAll();
            return false;
        }
        return true;
    }
    private String getNewEmployeeId(){
        if(getEmployeeList().isEmpty()){
            return "E-001";
        }else{
            String lastEmpId = getEmployeeList().get(getEmployeeList().size()-1).getId();
            int newEmpId = Integer.parseInt(lastEmpId.substring(2)) + 1;
            return String.format("E-%03d", newEmpId);
        }
    }
}
