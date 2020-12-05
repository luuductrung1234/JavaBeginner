package org.researching;

public class Subject {
    private String name;
    private String department;

    public Subject(String name, String department) {
        super();
        this.setName(name);
        this.setDepartment(department);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}