package lk.ijse.dep11.emp_app.handeller;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import lk.ijse.dep11.emp_app.tm.Employee;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeHandller {
    private static final String DATA_FILE = "employee.db";
    private static final File databaseFile = new File(DATA_FILE);

    public static void saveEmployees(ObservableList<Employee> employees) {
        List<Employee> employeeList = new ArrayList<>(employees);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            outputStream.writeObject(employeeList);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to save the employee data").show();
            e.printStackTrace();
        }
    }

    public static List<Employee> loadEmployees() {
        List<Employee> employees = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            Object object = inputStream.readObject();
            if (object instanceof List) {
                employees = (List<Employee>) object;
            }
        } catch (IOException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load data").show();
            e.printStackTrace();
        }
        return employees;
    }
}
