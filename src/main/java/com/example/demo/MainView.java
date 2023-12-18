package com.example.demo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.util.*;


@Route
public class MainView extends VerticalLayout {
    TextField ID = new TextField("ID");
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField gender = new TextField("Gender");
    TextField GPA = new TextField("GPA");
    TextField level = new TextField("Level");
    TextField address = new TextField("Address");

    TextField sortField = new TextField("sort by:");
    TextField search = new TextField("Search");
    TextField searchField = new TextField("Search field");
    boolean searchFlag = false;
    TextField delete = new TextField("Delete");
    boolean deleteFlag = false;
    Grid<Student> grid = new Grid<>(Student.class);
    Binder<Student> binder = new Binder<>(Student.class);
    Document doc;
    public MainView() {
        add(getPage());
        updateGrid();
    }

    private Component getPage(){
        var layout = new VerticalLayout();

        // my buttons
        // submit button
        var addStudentButton = new Button("add or update Student");
        var searchButton = new Button("search");
        var showAllButton = new Button("show all");
        var deleteButton = new Button("delete Student");

        var searchRow = new HorizontalLayout(search, searchField,searchButton, showAllButton);
        searchRow.setAlignItems(Alignment.BASELINE);
        var deleteRow = new HorizontalLayout(delete,deleteButton);
        deleteRow.setAlignItems(Alignment.BASELINE);

        addStudentButton.addClickListener(e -> {
                if(ID.getValue() == null || ID.getValue().isEmpty()){
                    Notification.show("Please fill id field");
                }
                else if (firstName.getValue() == null || firstName.getValue().isEmpty()){
                    Notification.show("Please fill firsName field");
                }
                else if (lastName.getValue() == null || lastName.getValue().isEmpty()){
                    Notification.show("Please fill lasName field");
                }
                else if (gender.getValue() == null || gender.getValue().isEmpty()){
                    Notification.show("Please fill gender field");
                }
                else if (GPA.getValue() == null || GPA.getValue().isEmpty()){
                    Notification.show("Please fill GPA field");
                }
                else if (level.getValue() == null || level.getValue().isEmpty()){
                    Notification.show("Please fill level field");
                }
                else if (Integer.parseInt(GPA.getValue()) < 0 || Integer.parseInt(GPA.getValue()) > 4) {
                    Notification.show("wrong in gpa field ");
                }
                else if (Integer.parseInt(level.getValue()) < 0 ) {
                    Notification.show("wrong in level field ");
                }
            else if (address.getValue() == null || address.getValue().isEmpty()){
                    Notification.show("Please fill address field");
                }
                else if ( !isValidName(firstName.getValue())){
                    Notification.show("not valid first name, add in chars");
                }
                else if (!isValidName(lastName.getValue())){
                    Notification.show("not valid last name, add in chars");
                }
                else if (!isValidName(address.getValue())){
                    Notification.show("not valid address, add in chars");
                }else{
                    // 1- Build an XML document.

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder;
                try {
                    builder = factory.newDocumentBuilder();
                } catch (ParserConfigurationException ex) {
                    throw new RuntimeException(ex);
                }
                File xmlFile = new File("students.xml");

                // Check if XML file exists
                if (xmlFile.exists()) {
                    try {
                        doc = builder.parse(xmlFile);
                    } catch (SAXException | IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    doc = builder.newDocument();
                    Element root = doc.createElement("University");
                    doc.appendChild(root);
                }

                //add student to xml file or update student
                int index = isDuplicateId(ID.getValue());
                if(index != -1){
                    // update student
                    NodeList students = doc.getElementsByTagName("Student");
                    // loop for each student to find the student with the same id

                        Node student = students.item(index);
                        if (student.getNodeType() == Node.ELEMENT_NODE) {
                            Element studentElement = (Element) student;

                                // update student data
                                studentElement.getElementsByTagName("FirstName").item(0).setTextContent(firstName.getValue());
                                studentElement.getElementsByTagName("LastName").item(0).setTextContent(lastName.getValue());
                                studentElement.getElementsByTagName("Gender").item(0).setTextContent(gender.getValue());
                                studentElement.getElementsByTagName("GPA").item(0).setTextContent(GPA.getValue());
                                studentElement.getElementsByTagName("Level").item(0).setTextContent(level.getValue());
                                studentElement.getElementsByTagName("Address").item(0).setTextContent(address.getValue());
                                Notification.show("Student updated!");

                        }


                } else {
                    // add student to xml file

                    Element student = doc.createElement("Student");
                    student.setAttribute("ID", ID.getValue());
                    doc.getDocumentElement().appendChild(student);

                    // Create and append student data
                    appendElement(doc, student, "FirstName", firstName.getValue());
                    appendElement(doc, student, "LastName", lastName.getValue());
                    appendElement(doc, student, "Gender", gender.getValue());
                    appendElement(doc, student, "GPA", GPA.getValue());
                    appendElement(doc, student, "Level", level.getValue());
                    appendElement(doc, student, "Address", address.getValue());

                    Notification.show("Student added!");
                }


                // Save the XML document to a file
                Transformer transformer;
                try {
                    transformer = TransformerFactory.newInstance().newTransformer();
                } catch (TransformerConfigurationException ex) {
                    throw new RuntimeException(ex);
                }
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                try {
                    transformer.transform(new DOMSource(doc), new StreamResult(xmlFile));
                } catch (TransformerException ex) {
                    throw new RuntimeException(ex);
                }
                ID.clear();
                firstName.clear();
                lastName.clear();
                gender.clear();
                GPA.clear();
                level.clear();
                address.clear();

                updateGrid();
            }
        });
        addStudentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        showAllButton.addClickListener(e -> {
            searchFlag = false;
            deleteFlag = false;
            searchRow.removeAll();
            searchRow.add(search, searchField,searchButton, showAllButton);
            searchRow.setAlignItems(Alignment.BASELINE);
            deleteRow.removeAll();
            deleteRow.add(delete,deleteButton);
            deleteRow.setAlignItems(Alignment.BASELINE);
            updateGrid();
        });
        searchButton.addClickListener(e -> {
            searchFlag = false;

            searchRow.removeAll();
            searchRow.add(search, searchField,searchButton, showAllButton);
            searchRow.setAlignItems(Alignment.BASELINE);

            // Search operation
            String query = search.getValue();
            NodeList students = doc.getElementsByTagName("Student");

            // Search operation by any of the fields
            String searchFieldValue = searchField.getValue();
            ArrayList<Node> studentsFound = new ArrayList<>();

            for (int i = 0; i < students.getLength(); i++) {
                Node student = students.item(i);
                if (student.getNodeType() == Node.ELEMENT_NODE) {
                    Element studentElement = (Element) student;
                    if(searchFieldValue.equals("ID")){
                        if (studentElement.getAttribute(searchFieldValue.trim()).trim().equalsIgnoreCase(query.trim())) {
                            studentsFound.add(student);
                            searchFlag = true;
                        }
                    }else {
                        if (studentElement.getElementsByTagName(searchFieldValue.trim()).item(0).getTextContent().trim().equalsIgnoreCase(query.trim())) {
                            studentsFound.add(student);
                            searchFlag = true;
                        }
                    }
                }
            }
            // Print the found students
//            for (Node student : studentsFound) {
//                assert student != null;
//                if (student.getNodeType() == Node.ELEMENT_NODE) {
//                    Element studentElement = (Element) student;
//                    System.out.println("Found student with ID: " + studentElement.getAttribute("ID"));
//                }
//            }

            // display the found students in a grid
            ArrayList<Student> studentsList = new ArrayList<>();

            for (Node student : studentsFound) {
                if (student.getNodeType() == Node.ELEMENT_NODE) {
                    Element studentElement = (Element) student;

                    var studentID = studentElement.getAttribute("ID");
                    var firstName = studentElement.getElementsByTagName("FirstName").item(0).getTextContent();
                    var lastName = studentElement.getElementsByTagName("LastName").item(0).getTextContent();
                    var gender = studentElement.getElementsByTagName("Gender").item(0).getTextContent();
                    var GPA = studentElement.getElementsByTagName("GPA").item(0).getTextContent();
                    var level = studentElement.getElementsByTagName("Level").item(0).getTextContent();
                    var address = studentElement.getElementsByTagName("Address").item(0).getTextContent();

                    var s = new Student(studentID, firstName, lastName, gender, GPA, level, address);
                    studentsList.add(s);

                }
            }
            grid.setItems(studentsList);
            searchRow.add(new H6("number of students found: " + studentsList.size()));
            Notification.show("number of students found: " + studentsList.size());

            if (!searchFlag) {
                searchRow.add(new H6("Student not found!"));
                Notification.show("Student not found!");
            }


        });

        deleteButton.addClickListener(e -> {

            deleteFlag = false;
// 1- Build an XML document.

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            try {
                builder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException ex) {
                throw new RuntimeException(ex);
            }
            File xmlFile = new File("students.xml");

            // Check if XML file exists
            if (xmlFile.exists()) {
                try {
                    doc = builder.parse(xmlFile);
                } catch (SAXException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                doc = builder.newDocument();
                Element root = doc.createElement("University");
                doc.appendChild(root);
            }

            // Save the XML document to a file
            Transformer transformer;
            try {
                transformer = TransformerFactory.newInstance().newTransformer();
            } catch (TransformerConfigurationException ex) {
                throw new RuntimeException(ex);
            }
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            try {
                transformer.transform(new DOMSource(doc), new StreamResult(xmlFile));
            } catch (TransformerException ex) {
                throw new RuntimeException(ex);
            }

            // Delete operation

            String deleteID = delete.getValue();
            NodeList students = doc.getElementsByTagName("Student");
            for (int i = 0; i < students.getLength(); i++) {
                Node student = students.item(i);
                if (student.getNodeType() == Node.ELEMENT_NODE) {
                    Element studentElement = (Element) student;
                    if (studentElement.getAttribute("ID").equals(deleteID)) {
                        studentElement.getParentNode().removeChild(studentElement);
                        Notification.show("Student deleted!");
                        deleteFlag = true;
                        break;
                    }
                }
            }
            if (!deleteFlag) {
                Notification.show("Student not found!");
            }

            // Save the XML document to a file after deletion
            try {
                transformer.transform(new DOMSource(doc), new StreamResult(xmlFile));
            } catch (TransformerException ex) {
                throw new RuntimeException(ex);
            }

            updateGrid();

        });

        var firstRow = new HorizontalLayout(ID,firstName, lastName, gender);
        var secondRow =new HorizontalLayout(GPA, level, address, addStudentButton);
        secondRow.setAlignItems(Alignment.BASELINE);

        //sorting
        var sortButtonAscendingOrDescending = new RadioButtonGroup<>("", "Ascending", "Descending");
        sortButtonAscendingOrDescending.setValue("Ascending");
        var sortButton = new Button("sort");

        var sortRow = new HorizontalLayout();

        sortButton.addClickListener(
                e -> {

                    updateGrid();
                    NodeList students = doc.getElementsByTagName("Student");
                    // sort operation by any of the fields
                    String sortFieldValue = sortField.getValue();
                    ArrayList<Student> studentsList = new ArrayList<>();

                    for (int i = 0; i < students.getLength(); i++) {
                        Node student = students.item(i);
                        if (student.getNodeType() == Node.ELEMENT_NODE) {
                            Element studentElement = (Element) student;

                            var studentID = studentElement.getAttribute("ID");
                            var firstName = studentElement.getElementsByTagName("FirstName").item(0).getTextContent();
                            var lastName = studentElement.getElementsByTagName("LastName").item(0).getTextContent();
                            var gender = studentElement.getElementsByTagName("Gender").item(0).getTextContent();
                            var GPA = studentElement.getElementsByTagName("GPA").item(0).getTextContent();
                            var level = studentElement.getElementsByTagName("Level").item(0).getTextContent();
                            var address = studentElement.getElementsByTagName("Address").item(0).getTextContent();

                            var s = new Student(studentID,firstName,lastName,gender,GPA,level,address);
                            studentsList.add(s);
                        }
                    }

                    // sort the studentsList by the sortFieldValue
                    if(sortFieldValue.equals("ID")){
                        if(sortButtonAscendingOrDescending.getValue().equals("Ascending")){
                            studentsList.sort(Comparator.comparing(Student::getID));
                        }else{
                            studentsList.sort(Comparator.comparing(Student::getID).reversed());
                        }
                    }else if(sortFieldValue.equals("firstName")){
                        if(sortButtonAscendingOrDescending.getValue().equals("Ascending")){
                            studentsList.sort(Comparator.comparing(Student::getFirstName));
                        }else{
                            studentsList.sort(Comparator.comparing(Student::getFirstName).reversed());
                        }
                    }

                    if(sortButtonAscendingOrDescending.getValue().equals("Ascending")){

                        if(sortFieldValue.trim().equalsIgnoreCase("id")){
                                studentsList.sort(Comparator.comparing(Student::getID));
                        }else if(sortFieldValue.trim().equalsIgnoreCase("firstName")){
                            studentsList.sort(Comparator.comparing(Student::getFirstName));
                        }else if(sortFieldValue.trim().equalsIgnoreCase("lastName")) {
                            studentsList.sort(Comparator.comparing(Student::getLastName));
                        }else if (sortFieldValue.trim().equalsIgnoreCase("gender")){
                            studentsList.sort(Comparator.comparing(Student::getGender));
                        }else if(sortFieldValue.trim().equalsIgnoreCase("gpa")){
                            studentsList.sort(Comparator.comparing(Student::getGPA));
                        } else if(sortFieldValue.trim().equalsIgnoreCase("level")){
                            studentsList.sort(Comparator.comparing(Student::getLevel));
                        }else if(sortFieldValue.trim().equalsIgnoreCase("address")){
                            studentsList.sort(Comparator.comparing(Student::getAddress));
                        }
                    }else{
                        if(sortFieldValue.trim().equalsIgnoreCase("id")){
                            studentsList.sort(Comparator.comparing(Student::getID).reversed());
                        }else if(sortFieldValue.trim().equalsIgnoreCase("firstName")){
                            studentsList.sort(Comparator.comparing(Student::getFirstName).reversed());
                        }else if(sortFieldValue.trim().equalsIgnoreCase("lastName")) {
                            studentsList.sort(Comparator.comparing(Student::getLastName).reversed());
                        }else if (sortFieldValue.trim().equalsIgnoreCase("gender")){
                            studentsList.sort(Comparator.comparing(Student::getGender).reversed());
                        }else if(sortFieldValue.trim().equalsIgnoreCase("gpa")){
                            studentsList.sort(Comparator.comparing(Student::getGPA).reversed());
                        } else if(sortFieldValue.trim().equalsIgnoreCase("level")){
                            studentsList.sort(Comparator.comparing(Student::getLevel).reversed());
                        }else if(sortFieldValue.trim().equalsIgnoreCase("address")){
                            studentsList.sort(Comparator.comparing(Student::getAddress).reversed());
                        }
                    }

                    // update the grid
                    grid.setItems(studentsList);

                    //save the sorted list to the xml file


                    // 1- Build an XML document.

                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder;
                    try {
                        builder = factory.newDocumentBuilder();
                    } catch (ParserConfigurationException ex) {
                        throw new RuntimeException(ex);
                    }
                    Document doc;
                    File xmlFile = new File("students.xml");

                    // Check if XML file exists
//                    if (xmlFile.exists()) {
//                        doc = builder.parse(xmlFile);
//                    } else {
                        doc = builder.newDocument();
                        Element root = doc.createElement("University");
                        doc.appendChild(root);
//                    }

                    // 3- Take student data from the user.
                    for (Student value : studentsList) {

                        Element student = doc.createElement("Student");
                        student.setAttribute("ID", value.getID());
                        doc.getDocumentElement().appendChild(student);

                        // Create and append student data
                        appendElement(doc, student, "FirstName", value.getFirstName());
                        appendElement(doc, student, "LastName", value.getLastName());
                        appendElement(doc, student, "Gender", value.getGender());
                        appendElement(doc, student, "GPA", value.getGPA());
                        appendElement(doc, student, "Level", value.getLevel());
                        appendElement(doc, student, "Address", value.getAddress());
                    }

                    // Save the XML document to a file
                    Transformer transformer;
                    try {
                        transformer = TransformerFactory.newInstance().newTransformer();
                    } catch (TransformerConfigurationException ex) {
                        throw new RuntimeException(ex);
                    }
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    try {
                        transformer.transform(new DOMSource(doc), new StreamResult(xmlFile));
                    } catch (TransformerException ex) {
                        throw new RuntimeException(ex);
                    }

                }
        );
        sortRow.add(sortField,sortButtonAscendingOrDescending,sortButton);
        sortRow.setAlignItems(Alignment.BASELINE);
        var formLayout = new VerticalLayout(searchRow,deleteRow,firstRow, secondRow, sortRow);

        formLayout.setAlignItems(Alignment.BASELINE);

        grid.setColumns("ID", "firstName", "lastName", "gender", "GPA", "level", "address");

        binder.bindInstanceFields(this);



        layout.add(formLayout, grid);

        return layout;
    }

    private static void appendElement(Document doc, Element parent, String name, String text) {
        Element elem = doc.createElement(name);
        elem.appendChild(doc.createTextNode(text));
        parent.appendChild(elem);
    }

    private void updateGrid(){

        // 1- Build an XML document.

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        }
        File xmlFile = new File("students.xml");

        // Check if XML file exists
        if (xmlFile.exists()) {
            try {
                doc = builder.parse(xmlFile);
            } catch (SAXException | IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            doc = builder.newDocument();
            Element root = doc.createElement("University");
            doc.appendChild(root);
        }


        ArrayList<Student> studentsList = new ArrayList<>();
        NodeList students = doc.getElementsByTagName("Student");

        for (int i = 0; i < students.getLength(); i++) {
            Node student = students.item(i);
            if (student.getNodeType() == Node.ELEMENT_NODE) {
                Element studentElement = (Element) student;

                var studentID = studentElement.getAttribute("ID");
                var firstName = studentElement.getElementsByTagName("FirstName").item(0).getTextContent();
                var lastName = studentElement.getElementsByTagName("LastName").item(0).getTextContent();
                var gender = studentElement.getElementsByTagName("Gender").item(0).getTextContent();
                var GPA = studentElement.getElementsByTagName("GPA").item(0).getTextContent();
                var level = studentElement.getElementsByTagName("Level").item(0).getTextContent();
                var address = studentElement.getElementsByTagName("Address").item(0).getTextContent();

                var s = new Student(studentID,firstName,lastName,gender,GPA,level,address);
                studentsList.add(s);

            }
        }
        grid.setItems(studentsList);
    }


    private boolean validateStudentAttributes(String id, String firstName, String lastName, String gender, String gpa, String level, String address) {
        double gpaDouble = gpa.isEmpty() ? -1 : Double.parseDouble(gpa);
        int levelInt = level.isEmpty() ? -1 : Integer.parseInt(level);

        return id != null && !id.isEmpty()
                && firstName != null && !firstName.isEmpty()
                && lastName != null && !lastName.isEmpty()
                && gender != null && !gender.isEmpty()
                && gpaDouble >= 0 && gpaDouble <= 4
                && levelInt > 0 && levelInt <= 4
                && address != null && !address.isEmpty()
//                && !isDuplicateId(id)
                && isValidName(firstName) && isValidName(lastName)
                && isValidName(address);
    }

    private int isDuplicateId(String id) {
        NodeList studentsNodeList = doc.getElementsByTagName("Student");
        for (int i = 0; i < studentsNodeList.getLength(); i++) {
            Node student = studentsNodeList.item(i);
            if (student.getNodeType() == Node.ELEMENT_NODE) {
                Element studentElement = (Element) student;
                if (studentElement.getAttribute("ID").equals(id)) {
                    return i;
                }
            }
        }
//        for (Student student : students) {
//            if (student.getId().equals(id)) {
//                return true;
//            }
//        }
        return -1;
    }

    private boolean isValidName(String name) {
        // Check if the name contains only characters (a-z)
        return name.trim().matches("[a-zA-Z]+");
    }
}