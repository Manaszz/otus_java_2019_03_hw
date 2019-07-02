package ru.otus.dao;

public class Account {
    @Id
    private final long no;
    private String type;
    private Number rest;

    public Account(long no, String type, Number rest) {
        this.no = no;
        this.type = type;
        this.rest = rest;
    }

    public Account(){
        no=0; type ="";
    }
    public long getId() {
        return no;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Account{" +
                "no=" + no +
                ", type='" + type + '\'' +
                ", rest=" + rest +
                '}';
    }

    public void setRest(int rest) {
        this.rest=rest;
    }
}
