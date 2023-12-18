package com.example.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Route
public class MainView extends VerticalLayout {

    private EmployeeManagement employeeManagement;

    private final Grid<Employee> grid = new Grid<>(Employee.class);

    private final TextField firstName = new TextField("First Name");
    private final TextField lastName = new TextField("Last Name");
    private final IntegerField employeeId = new IntegerField("Employee ID");
    private final TextField designation = new TextField("Designation");

    private final TextField languageName = new TextField("Language Name");
    private final IntegerField scoreOutof100 = new IntegerField("Score Out of 100");
    private final TextField searchField = new TextField("Search");

    private final Button addLanguageButton = new Button("Add Language", event -> addLanguage());
    private final Button addButton = new Button("Add Employee", event -> addEmployee());
    private final Button deleteButton = new Button("Delete Employee", event -> deleteEmployee());
    private final Button updateSelectedEmployeeButton = new Button("Update Selected Employee Designation", event -> updateDesignation());
    private final Button updateTHEEmployeeButton = new Button("Update Employee 2000 Designation", event -> updateTHEEmployeeDesignation());
    private final Button searchButton = new Button("Search", event -> searchEmployees(searchField.getValue()));
    private final Button showJavaExpertsButton = new Button("Show Java Experts", event -> retrieveEmployeesKnowsJavaWithScoreAbove50());

    public MainView() {
        this.employeeManagement = new EmployeeManagement();
        this.employeeManagement.initializeEmployees();

        grid.setColumns("firstName", "lastName", "employeeID", "designation");
        grid.setItems(employeeManagement.getEmployeeList());
        grid.addColumn(employee -> {
            if (employee.getKnownLanguages() != null) {
                return employee.getKnownLanguages().stream()
                        .map(language -> language.getLanguageName() + " (" + language.getScoreOutOf100() + ")")
                        .collect(Collectors.joining(", "));
            }
            return "";
        }).setHeader("Known Languages").setSortable(true).setKey("knownLanguages");

        HorizontalLayout employeeInfoLayout = new HorizontalLayout(employeeId, firstName, lastName, designation);
        HorizontalLayout languageLayout = new HorizontalLayout(languageName, scoreOutof100, addLanguageButton);
        HorizontalLayout buttonLayout = new HorizontalLayout(addButton, deleteButton);
        HorizontalLayout updateButtonLayout = new HorizontalLayout(updateSelectedEmployeeButton, updateTHEEmployeeButton);
        HorizontalLayout searchLayout = new HorizontalLayout(searchField, searchButton, showJavaExpertsButton);

        languageLayout.setAlignItems(Alignment.BASELINE);
        employeeInfoLayout.setAlignItems(Alignment.BASELINE);
        buttonLayout.setAlignItems(Alignment.BASELINE);
        updateButtonLayout.setAlignItems(Alignment.BASELINE);
        searchLayout.setAlignItems(Alignment.BASELINE);

        add(employeeInfoLayout, languageLayout, buttonLayout, updateButtonLayout, searchLayout, grid);

        setSpacing(true);
        setPadding(true);
    }

    private void addEmployee() {
        List<Language> languages = new ArrayList<>();
        languages.add(new Language(languageName.getValue(), scoreOutof100.getValue()));

        Employee newEmployee = new Employee(firstName.getValue(), lastName.getValue(),
                employeeId.getValue(), designation.getValue(), languages);

        employeeManagement.addEmployee(newEmployee);
        refreshGrid();
        clearFields();
        Notification.show("Employee added successfully");
    }

    private void deleteEmployee() {
        Employee selectedEmployee = grid.asSingleSelect().getValue();
        if (selectedEmployee != null) {
            employeeManagement.deleteEmployeeByID(selectedEmployee.getEmployeeID());
            refreshGrid();
            Notification.show("Employee deleted successfully");
        } else {
            Notification.show("Please select an employee to delete");
        }
    }

    private void updateDesignation() {
        Employee selectedEmployee = grid.asSingleSelect().getValue();
        if (selectedEmployee != null) {
            selectedEmployee.setDesignation(designation.getValue());
            employeeManagement.updateEmployeeDesignation(selectedEmployee.getEmployeeID(), designation.getValue());
            refreshGrid();
            clearFields();
            Notification.show("Designation updated successfully");
        } else {
            Notification.show("Please select an employee to update designation");
        }
    }

    private void updateTHEEmployeeDesignation() {
        Employee selectedEmployee = employeeManagement.findEmployeeByID(2000);
        if (selectedEmployee != null) {
            selectedEmployee.setDesignation("Team Leader");
            employeeManagement.updateEmployeeDesignation(selectedEmployee.getEmployeeID(), "Team Leader");
            refreshGrid();
            clearFields();
            Notification.show("Designation updated successfully");
        } else {
            Notification.show("employee 2000 not found");
        }
    }

    private void addLanguage() {
        Employee selectedEmployee = grid.asSingleSelect().getValue();
        if (selectedEmployee != null) {
            List<Language> languages = selectedEmployee.getKnownLanguages();
            languages.add(new Language(languageName.getValue(), scoreOutof100.getValue()));
            employeeManagement.updateEmployeeLanguages(selectedEmployee.getEmployeeID(), languages);
            refreshGrid();
            clearFields();
            Notification.show("Language added successfully");
        } else {
            Notification.show("Please select an employee to add a language");
        }
    }

    private void searchEmployees(String searchTerm) {
        if (searchTerm != null && !searchTerm.isEmpty()) {
            List<Employee> searchResult = employeeManagement.getEmployeeList().stream()
                    .filter(employee ->
                            String.valueOf(employee.getEmployeeID()).equals(searchTerm.toLowerCase().trim()) ||
                                    employee.getDesignation().toLowerCase().trim().contains(searchTerm.toLowerCase().trim())
                    )
                    .collect(Collectors.toList());

            grid.setItems(searchResult);
        } else {
            // If search term is empty, display all employees
            refreshGrid();
        }
    }

    private void retrieveEmployeesKnowsJavaWithScoreAbove50() {
        List<Employee> filteredEmployees = employeeManagement.getEmployeesKnowsJavaWithScoreAbove50();
        grid.setItems(filteredEmployees);
    }
    private void refreshGrid() {
        grid.setItems(employeeManagement.getEmployeeList());
    }

    private void clearFields() {
        firstName.clear();
        lastName.clear();
        employeeId.clear();
        designation.clear();
        languageName.clear();
        scoreOutof100.clear();
    }
}
