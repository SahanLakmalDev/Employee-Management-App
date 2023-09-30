package lk.ijse.dep11.emp_app.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lk.ijse.dep11.emp_app.tm.Employee;

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
}
