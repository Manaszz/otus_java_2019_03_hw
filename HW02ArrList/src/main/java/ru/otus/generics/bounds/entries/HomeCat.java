package ru.otus.generics.bounds.entries;

import java.util.Random;

/**
 * @author sergey
 * created on 23.11.18.
 */
public class HomeCat extends Cat {
    private final String name;

    public HomeCat(String name) {
        super();
        this.name = name;
//        this.id = new Random().nextInt(1000);
    }

    @Override
    public String toString() {
        return "HomeCat("+id+"), name:" + name;
    }
}
