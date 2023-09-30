package lk.ijse.dep11.emp_app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dep11.emp_app.handeller.EmployeeHandller;
import lk.ijse.dep11.emp_app.tm.Employee;

import java.util.ArrayList;
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

    private List<Employee> employeeList = new ArrayList<>();
    private ObservableList<Employee> observableEmployeeList;

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

        //Load existing employee data from the file and convert to an ObservableList
        employeeList = EmployeeHandller.loadEmployees();
        observableEmployeeList = FXCollections.observableArrayList(employeeList);

        // Set the ObservableList as the data source for the TableView
        tbvEmployees.setItems(observableEmployeeList);
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        if(!isDataValid()){
            return;
        }
        Employee selectedEmployee = tbvEmployees.getSelectionModel().getSelectedItem();
        String contact = txtContact.getText().strip();
        if (observableEmployeeList.stream().anyMatch(employee -> {
            if (selectedEmployee != null && selectedEmployee.equals(employee)) {
                return false; // Skip the selected employee
            }
            return employee.getContact().equals(contact);
        })) {
            new Alert(Alert.AlertType.ERROR, "Contact Already Exists").show();
            txtContact.requestFocus();
            txtContact.selectAll();
            return;
        }
        if(tbvEmployees.getSelectionModel().isEmpty()){
            //New Record
            Employee newEmployee = new Employee(txtId.getText().strip(), txtName.getText().strip(), txtContact.getText().strip());
            observableEmployeeList.add(newEmployee);
            btnNew.fire();
        }else{
            //Update
            selectedEmployee.setName(txtName.getText().strip());
            selectedEmployee.setContact(txtContact.getText().strip());
            tbvEmployees.refresh();
            btnNew.fire();
        }
        EmployeeHandller.saveEmployees(observableEmployeeList);
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Employee selectedEmployeeToDelete = tbvEmployees.getSelectionModel().getSelectedItem();
        observableEmployeeList.remove(selectedEmployeeToDelete);
        if(getEmployeeList().isEmpty()){
            btnNew.fire();
        }
        EmployeeHandller.saveEmployees(observableEmployeeList);

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
