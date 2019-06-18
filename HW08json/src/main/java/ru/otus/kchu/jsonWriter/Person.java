package ru.otus.kchu.jsonWriter;

public class Person   {
    private final int age;
    private final String name;

    Person( String name,int age) {
        System.out.println("new Person");
        this.name = name;
        this.age = age;
       }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
      //          ", newField='" + newField + '\'' +
                '}';
    }
}