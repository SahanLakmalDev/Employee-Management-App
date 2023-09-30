package lk.ijse.dep11.emp_app.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
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

    public void initialize(){

        for(Control control : new Control[]{txtId, txtContact, txtName, btnSave, btnDelete}){
            control.setDisable(true);
        }
        tbvEmployees.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tbvEmployees.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tbvEmployees.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("contact"));

        tbvEmployees.getSelectionModel().selectedItemProperty().addListener((observable, old, current) -> {
            if(current == null){
                btnDelete.setDisable(true);
            }else{
                txtId.setText(current.getId());
                txtName.setText(current.getName());
                txtContact.setText(current.getContact());
                btnDelete.setDisable(false);
            }
        });
        tbvEmployees.setOnKeyPressed(event ->{
            if(event.getCode() == KeyCode.DELETE){
                btnDelete.fire();
            }
        });
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
    }

    public void btnNewOnAction(ActionEvent actionEvent) {
        txtId.setText(getNewEmployeeId());
        for(Control control : new Control[]{txtName, txtContact, btnSave}){
            if(control instanceof TextField){
                ((TextField) control).clear();
                control.setDisable(false);
            }else{
                control.setDisable(false);
            }
        }
        txtName.requestFocus();
        tbvEmployees.getSelectionModel().clearSelection();
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
