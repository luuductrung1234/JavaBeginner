package com.learning.simple.common.data.model;

import java.io.InvalidClassException;
import java.io.Serializable;

public class Person implements Serializable {

    /**
     * In serialization, Java calculate version unique identifier. it's a secure
     * hash value that identifies the structure of the class.
     * 
     * This version will be attached when serializing an object to output stream. If
     * there is an update to BankAccount type, the new version with be generated.
     * 
     * When deserializing, Java will compare current version with the version from
     * input stream. If they are not matched, throw InvalidClassException
     * 
     * @see Person
     * @see InvalidClassException
     */
    private static final long serialVersionUID = 1L;

    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Person{name:%s,age:%d}", name, age);
    }
}