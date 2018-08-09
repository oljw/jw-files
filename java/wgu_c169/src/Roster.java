import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Jaewoo(Jae) Lee
 * WGU Student ID #000603567
 * Roster.java
 */

public class Roster {

    private static List<Student> studentList = new ArrayList<>();

    public static void main(String[] args) {
        createRoster();

        print_all();
        print_invalid_emails();

        //loop through the ArrayList and for each element:
        for (Student student : studentList)
            print_average_grade(student.getStudentId());

        remove("3");
        remove("3");
    }

    /*
    createRoster() method to create and add Student objects into the ArrayList
     */
    public static void createRoster() {
        Student student1 = new Student("1", 20, new int[]{88, 79, 59}, "John", "Smith", "John1989@gmail.com");
        Student student2 = new Student("2", 19, new int[]{91, 72, 85}, "Suzan", "Erickson", "Erickson_1990@gmailcom");
        Student student3 = new Student("3", 19, new int[]{85, 84, 87}, "Jack", "Napoli", "The_lawyer99yahoo.com");
        Student student4 = new Student("4", 22, new int[]{91, 98, 82}, "Erin", "Black", "Erin.black@comcast.net");
        Student student5 = new Student("5", 27, new int[]{90, 95, 85}, "Jaewoo", "Lee", "jlee188@wgu.com");
        studentList.add(student1);
        studentList.add(student2);
        studentList.add(student3);
        studentList.add(student4);
        studentList.add(student5);
    }

    /*
    public static void remove(String studentID) that removes students from the roster by student ID
     */
    public static void remove(String studentId) {
        System.out.println("Attempting to remove student with student ID: " + studentId + " from the roster..");
        Iterator<Student> iter = studentList.iterator();

        while (iter.hasNext()) {
            Student student = iter.next();

            if (studentId.equals(student.getStudentId())) {
                iter.remove();
                System.out.println("Student with student ID: " + studentId + " removed successfully.");
                return;
            }
        }

        System.out.print("Student with student ID: " + studentId + " does not exist!\n");
    }

    /*
    public static void print_all() that prints a complete tab-separated list of student data using accessor methods
     */
    public static void print_all() {
        System.out.print("Printing the list of students..\n");
        for (Student student : studentList)
            student.print();
    }

    /*
    public static void print_average_grade(String studentID) that correctly prints a student’s average grade by student ID
     */
    public static void print_average_grade(String studentId) {
        int[] grades = {};
        int averageGrade = 0;

        for (Student student : studentList) {
            if (studentId.equals(student.getStudentId())) {
                grades = student.getGrades();
                for (int grade : grades)
                    averageGrade += grade;

                System.out.println("Student with student ID: " + studentId + " has average grade of : " + (averageGrade/grades.length));
                return;
            }
        }
        System.out.print("Student with student ID: " + studentId + " does not exist!\n");
    }

    /*
    public static void print_invalid_emails() that verifies student e-mail addresses and displays all invalid e-mail addresses to the user
     */
    public static void print_invalid_emails() {
        System.out.println("Invalid E-mails:");
        for (Student student : studentList)
            if (!(student.getEmail().contains(".") && student.getEmail().contains("@") && !(student.getEmail().contains(" "))))
                System.out.println(student.getEmail());
    }
}
