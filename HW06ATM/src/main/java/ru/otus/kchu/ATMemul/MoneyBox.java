package ru.otus.kchu.ATMemul;

enum NoteValues{
    v100(100),v50(50), v20(20), v10(10), v5(5), v2(2), v1(1);
    private int numVal;
    NoteValues(int Val){ this.numVal = Val;    }
    public int getVal(){ return numVal;}
}

public class MoneyBox implements Box
{
    private int qtty;
    private NoteValues value;
//    public MoneyBox(int val){
//        value = val;
//        qtty = 0;
//    }

    public MoneyBox(NoteValues val){
        value = val;
        qtty = 0;
    }

    public MoneyBox(MoneyBox b) {
        this.value=b.getNValue();
        this.qtty=b.qtty;
    }

    public NoteValues getNValue() {
        return value;
    }

    public int getValue() {
        return value.getVal();
    }

    public int getAmount() {
        return value.getVal() * qtty;
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
        if(note.getNValue() == this.value) return insert(note.getQtty());
        return false;
    }
    public CashPack getPack(){
        return new CashPack(this.value,this.qtty);
    }
}