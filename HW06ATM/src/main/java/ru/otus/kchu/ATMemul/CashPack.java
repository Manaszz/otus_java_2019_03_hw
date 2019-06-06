package ru.otus.kchu.ATMemul;

public class CashPack{
    final private NoteValues value;
    final private int numValue;
     int qtty;

    @Override
    public String toString() {
        return "CashPack{" +
                "value=" + value +
                ", qtty=" + qtty +
                '}';
    }

    public CashPack(NoteValues val, int qty){
        value=val;
        numValue =val.getVal();
        qtty =qty;
    }
    public CashPack(NoteValues val){
        numValue = val.getVal();
        value =val;
        qtty = 0;
    }

    public void setQtty(int qtty) {
        this.qtty = qtty;
    }

    public int getValue() {
        return value.getVal();
    }
    public NoteValues getNValue() {
        return value;
    }

    public int getQtty() {
        return qtty;
    }
}
