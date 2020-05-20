package com.learning;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Logger logger = Logger.getLogger(Main.class.getSimpleName());

    public static void main(String[] args) {
        Person person1 = new Person("John", "Doe", 23);
        Person person2 = new Person("Daisy", "Chris", 24);

        Comparator<Person> ageCmp = (p1, p2) -> p2.getAge() - p1.getAge();
        logger.log(Level.INFO, "Age Compare: {0}", ageCmp.compare(person1, person2));

        Comparator<Person> firstNameCmp = (p1, p2) -> p1.getFirstName().compareTo(p2.getFirstName());
        logger.log(Level.INFO, "First Name Compare: {0}", firstNameCmp.compare(person1, person2));

        Comparator<Person> lastNameCmp = (p1, p2) -> p1.getLastName().compareTo(p2.getLastName());
        logger.log(Level.INFO, "Last Name Compare: {0}", lastNameCmp.compare(person1, person2));

        Comparator<Person> personCmp = Comparator.comparing(p -> p.getAge());
        logger.log(Level.INFO, "Passing lambda as parameter");
        logger.log(Level.INFO, "Age Compare: {0}", personCmp.compare(person1, person2));

        personCmp = Comparator.comparing(Person::getAge);
        logger.log(Level.INFO, "Passing method member as parameter");
        logger.log(Level.INFO, "Age Compare: {0}", personCmp.compare(person1, person2));

        Comparator<Person> chainCmp = Comparator.comparing(Person::getAge).thenComparing(Person::getFirstName)
                .thenComparing(Person::getLastName);
        logger.log(Level.INFO, "Chain Compare: {0}", chainCmp.compare(person1, person2));
    }
}
