package ru.otus.kchu.ATMemul;

public interface Box {
    void wdraw(int amount)throws Exception;
    boolean insert(int amount) ;
    int getQty();
    boolean isEmpty();
}
