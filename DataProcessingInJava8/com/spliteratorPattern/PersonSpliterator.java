package com.spliteratorPattern;

import java.util.Spliterator;
import java.util.function.Consumer;

public class PersonSpliterator implements Spliterator<Person> {
    private Spliterator<String> lineSpliterator;

    public PersonSpliterator(Spliterator<String> lineSpliterator) {
        super();
        this.lineSpliterator = lineSpliterator;
    }

    /**
     * Wrapping 3 line of text into 1 Person
     */
    @Override
    public boolean tryAdvance(Consumer<? super Person> action) {
        Person p = new Person("", 1, "");
        if (lineSpliterator.tryAdvance(line -> p.setName(line))
                && lineSpliterator.tryAdvance(line -> p.setAge(Integer.parseInt(line)))
                && lineSpliterator.tryAdvance(line -> p.setCity(line))) {
            action.accept(p);
            return true;
        }
        return false;
    }

    /**
     * If you do not want to iterating in parallel, leave this function return null
     */
    @Override
    public Spliterator<Person> trySplit() {
        return null;
    }

    /**
     * 1 Person has 3 line of information in text file
     */
    @Override
    public long estimateSize() {
        return lineSpliterator.estimateSize() / 3;
    }

    @Override
    public int characteristics() {
        return lineSpliterator.characteristics();
    }
}