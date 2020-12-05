package com.teaching;

import java.util.List;
import org.researching.Subject;

public class Student {
    private String name;
    private int age;
    private List<Subject> learnedSubjects;

    public Student(String name, int age, List<Subject> learnedSubjects) {
        super();
        this.name = name;
        this.age = age;
        this.learnedSubjects = learnedSubjects;
    }

    /**
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the learnedSubjects
     */
    public List<Subject> getLearnedSubjects() {
        return learnedSubjects;
    }

    /**
     * @param learnedSubjects the learnedSubjects to set
     */
    public void setLearnedSubjects(List<Subject> learnedSubjects) {
        this.learnedSubjects = learnedSubjects;
    }
}