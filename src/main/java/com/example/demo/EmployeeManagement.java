package com.example.demo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeManagement {
    private List<Employee> employeeList = new ArrayList<>();
    private final String JSON_FILE_PATH = "employees.json";

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    // Function to initialize employees with JSON data
    public void initializeEmployees() {
        File file = new File(JSON_FILE_PATH);

        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Gson gson = new Gson();
                Type empListType = new TypeToken<ArrayList<Employee>>() {}.getType();
                employeeList = gson.fromJson(reader, empListType);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            createNewJSONFile();
        }
    }
    // Function to create a new JSON file with initial data
    private void createNewJSONFile() {
        try (FileWriter writer = new FileWriter(JSON_FILE_PATH)) {
            // Add initial data to the JSON file (empty list for employees in this case)
            Gson gson = new Gson();
            gson.toJson(employeeList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Function to add a new employee
    public void addEmployee(Employee newEmployee) {
        employeeList.add(newEmployee);
        saveEmployeeDataToFile();
    }

    // Function to search for an employee by ID
    public Employee findEmployeeByID(int id) {
        return employeeList.stream()
                .filter(employee -> employee.getEmployeeID() == id)
                .findFirst()
                .orElse(null);
    }

    // Function to search for employees by designation
    public List<Employee> findEmployeesByDesignation(String designation) {
        List<Employee> result = new ArrayList<>();
        for (Employee employee : employeeList) {
            if (employee.getDesignation().equalsIgnoreCase(designation)) {
                result.add(employee);
            }
        }
        return result;
    }

    // Function to delete an employee by ID
    public boolean deleteEmployeeByID(int id) {
        Employee employee = findEmployeeByID(id);
        if (employee != null) {
            employeeList.remove(employee);
            saveEmployeeDataToFile();
            return true;
        }
        return false;
    }

    // Function to update an employee's designation by ID
    public boolean updateEmployeeDesignation(int id, String newDesignation) {
        Employee employee = findEmployeeByID(id);
        if (employee != null) {
            employee.setDesignation(newDesignation);
            saveEmployeeDataToFile();
            return true;
        }
        return false;
    }

    // Function to update an employee's Languages by ID
    public boolean updateEmployeeLanguages(int id, List<Language> newLanguages) {
        Employee employee = findEmployeeByID(id);
        if (employee != null) {
            employee.setKnownLanguages(newLanguages);
            saveEmployeeDataToFile();
            return true;
        }
        return false;
    }

    // Function to retrieve employees who know Java with score > 50 sorted in ascending order
    public List<Employee> getEmployeesKnowsJavaWithScoreAbove50() {
        return employeeList.stream()
                .filter(employee ->
                        employee.getKnownLanguages().stream()
                                .anyMatch(language ->
                                        language.getLanguageName().trim().equalsIgnoreCase("Java") &&
                                                language.getScoreOutOf100() > 50
                                )
                )
                .sorted(Comparator.comparingInt(employee ->
                        employee.getKnownLanguages().stream()
                                .filter(language ->
                                        language.getLanguageName().trim().equalsIgnoreCase("Java") &&
                                                language.getScoreOutOf100() > 50
                                )
                                .mapToInt(Language::getScoreOutOf100)
                                .findFirst().orElse(0)
                ))
                .collect(Collectors.toList());
    }

    // Function to save employee data to the JSON file
    private void saveEmployeeDataToFile() {
        try (FileWriter writer = new FileWriter(JSON_FILE_PATH)) {
            Gson gson = new Gson();
            gson.toJson(employeeList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
