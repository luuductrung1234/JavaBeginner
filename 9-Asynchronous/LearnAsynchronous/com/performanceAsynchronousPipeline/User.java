package com.performanceAsynchronousPipeline;

public class User {
    private long id;
    private String name;
    private int age;
    private String city;

    public User(long id, String name, int age, String city) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("User{id:%d,name:%s,age:%d,city:%s}", id, name, age, city);
    }
}