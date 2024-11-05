import java.io.File;
import java.util.Scanner;

public class Test {
    static DLinkedList<Major> majors = new DLinkedList<>();

    public static void main(String[] args) throws Exception {
        openMajorFile(new File("src\\criteria.txt"));
        openStudentsFile(new File("src/students.txt"));
        Student student = new Student(10, "Mustafa", 90, 90);
        majors.printList();
        DLinkedList<Major> stdMajors = majors.acceptedMajorsForStudent(student);
        System.out.println("After --");
        stdMajors.printList();

    }

    static void openStudentsFile(File f) throws Exception {// this opens a file that contains studnets with their
                                                           // desired major, we check if they can proceed or not
        Scanner fileScanner = new Scanner(f);
        fileScanner.nextLine();
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            double grade = Double.parseDouble(parts[2]);
            double placement = Double.parseDouble(parts[3]);
            String majorName = parts[4];

            Student student = new Student(id, name, grade, placement);
            // if the student satisfies the major requirments we add it to the single linked
            // list for a DNode of the major
            DNode<Major> nodeRefrence = majors.searchByName(majorName);
            Major major = majors.searchByName(majorName).data;
            if (student.calculateAdmissionMark(major.getTawjihiWeight(), major.getTestWeight()) >= major
                    .getAcceptGrade()) {
                nodeRefrence.studentsList.insertSorted(student);
                student.accepted = true;
                System.out.println("Student " + student.getName() + " is Accepted.");
            } else {
                student.accepted = false;
                System.out.println("Student " + student.getName() + " is NOT Accepted.");// we must now put a suggested
                                                                                         // majors for the students
            }
        }
        fileScanner.close();
    }

    static void openMajorFile(File f) throws Exception { // this opens a file that suppose to contaiin majors and their
                                                         // accept marks and tests weights
        Scanner fileScanner = new Scanner(f);
        fileScanner.nextLine();
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] parts = line.split(",");
            String name = parts[0];
            double AG = Double.parseDouble(parts[1]);
            double TW = Double.parseDouble(parts[2]);
            double PT = Double.parseDouble(parts[3]);
            Major major = new Major(name, AG, TW, PT);
            majors.insertSorted(new DNode<Major>(major));
        }
        fileScanner.close();
    }
}
