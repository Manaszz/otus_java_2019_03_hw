package ru.otus.kchu.ATMemul;

public class MoneyBox implements Box
{
    private int qtty;
    private int value;
    public MoneyBox(int val){
        value = val;
        qtty = 0;
    }
    public int getValue() {
        return value;
    }

    public int getAmount() {
        return value * qtty;
    }

    @Override
    public boolean isEmpty() {
        return getQty()>0?false:true;
    }

    @Override
    public boolean insert(int amount) {
        qtty= qtty+ amount;
        return true;
    }

    @Override
    public void wdraw(int amount) throws Exception {
    if(qtty < amount) throw new Exception("Assets too low");
        else qtty=qtty -amount;
    }

    public CashPack wdrawCash(int amount) throws Exception {
    if(qtty < amount) throw new Exception("Assets too low");
        else qtty=qtty -amount;
        return new CashPack(this.value,amount);
    }

    @Override
    public int getQty() {
        return qtty;
    }

    @Override
    public String toString() {
        return "mBox{" +
                "qtty=" + qtty +
                '}';
    }

    public boolean addPack(CashPack note) {
        if(note.getValue() == this.value) return insert(note.getQtty());
        return false;
    }
    public CashPack getPack(){
        return new CashPack(this.value,this.qtty);
    }
}