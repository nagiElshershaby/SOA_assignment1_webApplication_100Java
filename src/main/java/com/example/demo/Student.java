package com.example.demo;

public class Student {
    private String ID;
    private String firstName;
    private String lastName;
    private String gender;
    private String GPA;
    private String level;
    private String address;

    public Student(String studentID, String firstName, String lastName, String gender, String gpa, String level, String address) {
        ID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.GPA = gpa;
        this.level = level;
        this.address = address;

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGPA() {
        return GPA;
    }

    public void setGPA(String GPA) {
        this.GPA = GPA;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
