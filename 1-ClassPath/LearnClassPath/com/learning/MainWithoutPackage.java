
import java.util.ArrayList;
import java.util.List;

import com.teaching.Student;

import org.researching.Subject;

public class MainWithoutPackage {
    public static void main(String[] args) {
        System.out.println("Run class MainWithoutPackage");

        List<Subject> learnedSubjects = new ArrayList<>();
        learnedSubjects.add(new Subject("Propability & Statistic", "Math"));

        Student student = new Student("Luu Duc Trung", 23, learnedSubjects);
        System.out.println("Student Name: " + student.getName());
        System.out.println("Student Age: " + student.getAge());
        System.out.println("Learned Subject: " + student.getLearnedSubjects().get(0).getName());
    }
}