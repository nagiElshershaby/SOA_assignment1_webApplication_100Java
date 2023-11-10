package com.example.demo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


@Route
public class MainView extends VerticalLayout {
    TextField ID = new TextField("ID");
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField gender = new TextField("Gender");
    TextField GPA = new TextField("GPA");
    TextField level = new TextField("Level");
    TextField address = new TextField("Address");

    TextField search = new TextField("Search");
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
        var addStudentButton = new Button("add Student");
        var searchButton = new Button("search");
        var deleteButton = new Button("delete Student");

        var searchRow = new HorizontalLayout(search,searchButton);
        searchRow.setAlignItems(Alignment.BASELINE);
        var deleteRow = new HorizontalLayout(delete,deleteButton);
        deleteRow.setAlignItems(Alignment.BASELINE);

        addStudentButton.addClickListener(e -> {
            if(firstName.getValue().isEmpty() || lastName.getValue().isEmpty() || gender.getValue().isEmpty() || GPA.getValue().isEmpty() || level.getValue().isEmpty() || address.getValue().isEmpty()){
                Notification.show("Please fill all fields");
//                System.out.println(textFieldFirstName.getValue());
//                System.out.println(lastName);
//                System.out.println(GPA);
            }else{

                // 1- Build an XML document.

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = null;
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

                // Save the XML document to a file
                Transformer transformer = null;
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

        searchButton.addClickListener(e -> {
            searchFlag = false;

            searchRow.removeAll();
            searchRow.add(search,searchButton);
            searchRow.setAlignItems(Alignment.BASELINE);

            // Search operation
            String query = search.getValue();
            NodeList students = doc.getElementsByTagName("Student");
            for (int i = 0; i < students.getLength(); i++) {
                Node student = students.item(i);
                if (student.getNodeType() == Node.ELEMENT_NODE) {
                    Element studentElement = (Element) student;
                    if (studentElement.getAttribute("ID").equals(query) ||
                            studentElement.getElementsByTagName("FirstName").item(0).getTextContent().equals(query)) {
                        var verticalLayout = new VerticalLayout();
                        verticalLayout.setAlignItems(Alignment.START);
//                        verticalLayout.add(new H6("Found student with ID: " + studentElement.getAttribute("ID")));
                        Notification.show("Found student with ID: " + studentElement.getAttribute("ID"));
                        verticalLayout.add(new H6("ID: " + studentElement.getAttribute("ID") + ",  First Name: " + studentElement.getElementsByTagName("FirstName").item(0).getTextContent() + ",  Last Name: " + studentElement.getElementsByTagName("LastName").item(0).getTextContent() + ",  Gender: " + studentElement.getElementsByTagName("Gender").item(0).getTextContent() + ",  GPA: " + studentElement.getElementsByTagName("GPA").item(0).getTextContent() + ",  Level: " + studentElement.getElementsByTagName("Level").item(0).getTextContent() + ",  Address: " + studentElement.getElementsByTagName("Address").item(0).getTextContent()));
                        searchRow.add(verticalLayout);
                        searchFlag = true;
                        break;
                    }
                }
            }
            if (!searchFlag) {
                searchRow.add(new H6("Student not found!"));
                Notification.show("Student not found!");
            }


        });
        searchButton.addClickShortcut(Key.ENTER);
        deleteButton.addClickListener(e -> {

            deleteFlag = false;
// 1- Build an XML document.

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
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
            Transformer transformer = null;
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
//        deleteButton.addClickShortcut(Key.ENTER);

//        searchRow.add(search,searchButton);
//
//        deleteRow.add(delete,deleteButton);

        var firstRow = new HorizontalLayout(ID,firstName, lastName, gender);
        var secondRow =new HorizontalLayout(GPA, level, address, addStudentButton);
        secondRow.setAlignItems(Alignment.BASELINE);
        var formLayout = new VerticalLayout(searchRow,deleteRow,firstRow, secondRow);

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
        DocumentBuilder builder = null;
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

//        String search = scanner.nextLine();
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
}