package com.example.demo;
import java.util.List;

public class Employee {
    private String firstName;
    private String lastName;
    private int employeeID;
    private String designation;
    private List<Language> knownLanguages;

    // Constructors, getters, and setters

    public Employee(String firstName, String lastName, int employeeID, String designation, List<Language> languages) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.employeeID = employeeID;
        this.designation = designation;
        this.knownLanguages = languages;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getDesignation() {
        return designation;
    }

    public List<Language> getKnownLanguages() {
        return knownLanguages;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setKnownLanguages(List<Language> knownLanguages) {
        this.knownLanguages = knownLanguages;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", employeeID=" + employeeID +
                ", designation='" + designation + '\'' +
                ", knownLanguages=" + knownLanguages +
                '}';
    }


    public int getLanguageScore(String languageName) {
        for (Language language : knownLanguages) {
            if (language.getLanguageName().equals(languageName)) {
                return language.getScoreOutOf100();
            }
        }
        return -1;
    }
}
