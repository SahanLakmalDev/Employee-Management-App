package lk.ijse.dep11.emp_app.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dep11.emp_app.handeller.EmployeeHandller;
import lk.ijse.dep11.emp_app.tm.Employee;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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

        //Create Search text field
        FilteredList<Employee> filteredList = new FilteredList<>(observableEmployeeList, p -> true);
        //Bind the filtered list to the table view
        tbvEmployees.setItems(filteredList);
        // Add a listener to the textProperty of the search TextField
        txtSearch.textProperty().addListener((observable, old, current) -> {
            filteredList.setPredicate(employee -> {
                // If the search field is empty, show all employees
                if (current == null || current.isEmpty()) {
                    return true;
                }
                // Convert the search string to lowercase for case-insensitive search
                String lowerCaseFilter = current.toLowerCase();

                // Check if the name or contact contains the search string
                return employee.getName().toLowerCase().contains(lowerCaseFilter)
                        || employee.getContact().toLowerCase().contains(lowerCaseFilter)
                        || employee.getId().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Enable the TableView to accept file drops
        tbvEmployees.setOnDragOver(event -> {
            if (event.getGestureSource() != tbvEmployees && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
        // Handle file drop
        tbvEmployees.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasFiles()) {
                success = true;
                File file = db.getFiles().get(0); // Assuming you only allow one file at a time
                importCSVFile(file);
            }

            event.setDropCompleted(success);
            event.consume();
        });

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

    private void importCSVFile(File csvFile){
        List<Employee> employeesToAdd = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            String line;
            boolean flag = true;
            try{
                while((line = br.readLine()) != null){
                    String[] data = line.split(",");
                    if(data.length == 3){
                        String id = data[0].strip();
                        String name = data[1].strip();
                        String contact = data[2].strip();
                        if(isValidId(id) && isValidName(name) && isValidContact(contact)){
                            for(Employee employee : employeesToAdd){
                                if(employee.getId().equals(id) || employee.getContact().equals(contact)){
                                    flag = false;
                                    break;
                                }
                            }
                            if(flag){
                                Employee newEmployee = new Employee(id, name, contact);
                                employeesToAdd.add(newEmployee);
                            }

                        }
                    }


                }
                observableEmployeeList.addAll(employeesToAdd);
                EmployeeHandller.saveEmployees(observableEmployeeList);
                tbvEmployees.refresh();
            }finally {
                br.close();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isValidId(String id){
        for(Employee employee : observableEmployeeList){
            if(employee.getId().equals(id)){
                return false;

            }
        }
        return id.matches("E-\\d{3}");
    }
    private boolean isValidName(String name){
        return name.matches("[a-zA-Z ]+");
    }
    private boolean isValidContact(String contact){
        for(Employee employee : observableEmployeeList){
            if(employee.getContact().equals(contact)){
                return false;
            }
        }
        return contact.matches("0\\d{2}-\\d{7}");
    }
}
