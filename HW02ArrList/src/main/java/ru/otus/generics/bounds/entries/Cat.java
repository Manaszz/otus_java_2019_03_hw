package ru.otus.generics.bounds.entries;

import java.util.Random;

public class Cat extends Animal {
    protected int id ;
    public Cat() {
        this.id = new Random().nextInt(1000);
    }

    @Override
    public String toString() {
        return "Cat("+id+")";
    }

    public int getId(){return id;}

}
