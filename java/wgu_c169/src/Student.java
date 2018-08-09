import java.util.Arrays;

/**
 * Jaewoo(Jae) Lee
 * WGU Student ID #000603567
 * Student.java
 */

public class Student {
    /*
    Include the following instance variables that describe each student:
        •   student ID
        •   first name
        •   last name
        •   e-mail address
        •   age
        •   array of grades
     */
    private int age;
    private int[] grades;

    private String studentId;
    private String firstName;
    private String lastName;
    private String email;

    /*
    constructor using all of the input parameters
     */
    public Student(String studentId, int age, int[] grades, String firstName, String lastName, String email) {
        this.studentId = studentId;
        this.age = age;
        this.grades = grades;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /*
    Accessors and mutators
     */
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int[] getGrades() {
        return grades;
    }

    public void setGrades(int[] grades) {
        this.grades = grades;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*
    print() to print specific student data (e.g., student ID, first name, last name) using accessors (i.e., getters)
     */
    public void print() {
        System.out.println (
                "\tStudent ID: " + getStudentId() +
                "\tFirst Name: " + getFirstName() +
                "\tLast Name: " + getLastName() +
                "\tAge: " + getAge() +
                "\tEmail Address: " + getEmail() +
                "\tGrades: " + Arrays.toString(getGrades()));
    }
}
