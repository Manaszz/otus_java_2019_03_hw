package ru.otus.kchu.ATMemul;

public class CashPack{
    final private int value;
     int qtty;

    @Override
    public String toString() {
        return "CashPack{" +
                "value=" + value +
                ", qtty=" + qtty +
                '}';
    }

    public CashPack(int val, int qty){
        value =val;
        qtty =qty;
    }
    public CashPack(int val){
        value =val;
        qtty = 0;
    }

    public void setQtty(int qtty) {
        this.qtty = qtty;
    }

    public int getValue() {
        return value;
    }

    public int getQtty() {
        return qtty;
    }
}
