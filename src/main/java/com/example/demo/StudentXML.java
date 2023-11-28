package com.example.demo;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

import java.util.Objects;
import java.util.Scanner;

public class StudentXML {
    public static void main(String[] args) throws Exception {
        // 1- Build an XML document.

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc;
        File xmlFile = new File("students.xml");

        // Check if XML file exists
        if (xmlFile.exists()) {
            doc = builder.parse(xmlFile);
        } else {
            doc = builder.newDocument();
            Element root = doc.createElement("University");
            doc.appendChild(root);
        }

        // 2- Ask the user to enter the number of students

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of students:");
        int numStudents = scanner.nextInt();
        scanner.nextLine();  // Consume newline left-over


        // 3- Take student data from the user.
        for (int i = 0; i < numStudents; i++) {
            System.out.println("Enter data for student " + (i+1) + ":");

            Element student = doc.createElement("Student");
            student.setAttribute("ID", prompt(scanner, "ID"));
            doc.getDocumentElement().appendChild(student);

            // Create and append student data
            appendElement(doc, student, "FirstName", prompt(scanner, "FirstName"));
            appendElement(doc, student, "LastName", prompt(scanner, "LastName"));
            appendElement(doc, student, "Gender", prompt(scanner, "Gender"));
            appendElement(doc, student, "GPA", prompt(scanner, "GPA"));
            appendElement(doc, student, "Level", prompt(scanner, "Level"));
            appendElement(doc, student, "Address", prompt(scanner, "Address"));
        }

        // Save the XML document to a file
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(xmlFile));

        // Search operation
        System.out.println("Enter GPA or FirstName to search:");
        String search = scanner.nextLine();
        NodeList students = doc.getElementsByTagName("Student");
        for (int i = 0; i < students.getLength(); i++) {
            Node student = students.item(i);
            if (student.getNodeType() == Node.ELEMENT_NODE) {
                Element studentElement = (Element) student;
                if (studentElement.getElementsByTagName("GPA").item(0).getTextContent().equals(search) ||
                        studentElement.getElementsByTagName("FirstName").item(0).getTextContent().equals(search)) {
                    System.out.println("Found student with ID: " + studentElement.getAttribute("ID"));
                    break;
                }
            }
        }

        // Search operation by any of the fields
        System.out.println("Enter any field to search:");
        String searchField = scanner.nextLine();
        NodeList studentsFound = new NodeList() {
            @Override
            public Node item(int index) {
                return null;
            }

            @Override
            public int getLength() {
                return 0;
            }
        };
        for (int i = 0; i < students.getLength(); i++) {
            Node student = students.item(i);
            if (student.getNodeType() == Node.ELEMENT_NODE) {
                Element studentElement = (Element) student;
                if (studentElement.getElementsByTagName(searchField).item(0).getTextContent().equals(search)) {
                    Objects.requireNonNull(studentsFound.item(studentsFound.getLength() + 1)).appendChild(studentElement);
                }
            }
        }
        // Print the found students
        for (int i = 0; i < studentsFound.getLength(); i++) {
            Node student = studentsFound.item(i);
            assert student != null;
            if (student.getNodeType() == Node.ELEMENT_NODE) {
                Element studentElement = (Element) student;
                System.out.println("Found student with ID: " + studentElement.getAttribute("ID"));
            }
        }

        // Delete operation
        System.out.println("Enter ID of student to delete:");
        String deleteID = scanner.nextLine();
        for (int i = 0; i < students.getLength(); i++) {
            Node student = students.item(i);
            if (student.getNodeType() == Node.ELEMENT_NODE) {
                Element studentElement = (Element) student;
                if (studentElement.getAttribute("ID").equals(deleteID)) {
                    studentElement.getParentNode().removeChild(studentElement);
                    System.out.println("Deleted student with ID: " + deleteID);
                    break;
                }
            }
        }

        // Save the XML document to a file after deletion
        transformer.transform(new DOMSource(doc), new StreamResult(xmlFile));
    }

    private static String prompt(Scanner scanner, String field) {
        System.out.println("Enter " + field + ":");
        return scanner.nextLine();
    }

    private static void appendElement(Document doc, Element parent, String name, String text) {
        Element elem = doc.createElement(name);
        elem.appendChild(doc.createTextNode(text));
        parent.appendChild(elem);
    }
}