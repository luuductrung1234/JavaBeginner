package com.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainCollections {
    private static Logger logger = Logger.getLogger(MainCollections.class.getSimpleName());

    public static void main(String[] args) {
        City paris = new City("Paris");
        City shanghai = new City("Shanghai");
        City newYork = new City("New York");

        Person p1 = new Person("Trung", "Luu", 22);
        Person p2 = new Person("Yen", "Thai", 23);
        Person p3 = new Person("Tuyen", "Luu", 16);
        Person p4 = new Person("Doan", "Nguyen", 15);
        Person p5 = new Person("Khoa", "Nguyen", 10);
        Person p6 = new Person("Sang", "Duong", 20);

        // List using lambda
        List<Person> people = new ArrayList<>(Arrays.asList(p1, p2, p3, p4));
        people.removeIf(person -> person.getAge() > 20);
        people.replaceAll(person -> new Person(person.getFirstName().toUpperCase(), person.getLastName().toUpperCase(),
                person.getAge()));
        people.sort(Comparator.comparing(Person::getAge).reversed());
        people.forEach(person -> logger.info(person.toString()));

        // Map using lambda
        Map<City, List<Person>> firstMap = new HashMap<>();
        firstMap.putIfAbsent(paris, new ArrayList<>());
        firstMap.get(paris).add(p1);
        firstMap.computeIfAbsent(newYork, city -> new ArrayList<>()).add(p2);
        firstMap.computeIfAbsent(shanghai, city -> new ArrayList<>()).add(p3);
        logger.log(Level.INFO, "People from Paris : {0}", firstMap.getOrDefault(paris, Collections.EMPTY_LIST));
        logger.log(Level.INFO, "First Map");
        firstMap.forEach((c, p) -> logger.log(Level.INFO, "People from {0} : {1}", new Object[] { c, p }));

        Map<City, List<Person>> secondMap = new HashMap<>();
        secondMap.computeIfAbsent(shanghai, city -> new ArrayList<>()).add(p4);
        secondMap.computeIfAbsent(shanghai, city -> new ArrayList<>()).add(p5);
        secondMap.computeIfAbsent(newYork, city -> new ArrayList<>()).add(p6);
        logger.log(Level.INFO, "People from Paris : {0}", secondMap.getOrDefault(paris, Collections.EMPTY_LIST));
        logger.log(Level.INFO, "Second Map");
        secondMap.forEach((c, p) -> logger.log(Level.INFO, "People from {0} : {1}", new Object[] { c, p }));

        secondMap.forEach((c, p) -> {
            firstMap.merge(c, p, (peopleFromFirstMap, peopleFromSecondMap) -> {
                peopleFromFirstMap.addAll(peopleFromSecondMap);
                return peopleFromFirstMap;
            });
        });
        logger.log(Level.INFO, "After merge from SecondMap to FirstMap");
        firstMap.forEach((c, p) -> logger.log(Level.INFO, "People from {0} : {1}", new Object[] { c, p }));
    }
}