package ru.otus.kchu.ATMemul;

import java.util.*;
import java.util.stream.Collectors;

public class MoneyUnit {

    private MoneyBox box100= new MoneyBox(100);
    private MoneyBox box50= new MoneyBox(50);
    private MoneyBox box20= new MoneyBox(20);
    private MoneyBox box10= new MoneyBox(10);
    private MoneyBox box5= new MoneyBox(5);
    private MoneyBox box2= new MoneyBox(2);
    private MoneyBox box1= new MoneyBox(1);
    private List <MoneyBox> boxList = Arrays.asList(box100,box50, box20, box10, box5,  box2,box1);
    private Map <Integer,MoneyBox> moneyBoxMap = new HashMap<>();

    public MoneyUnit(){
        getMoneyBoxMap();
    }

    private void getMoneyBoxMap() {
        boxList.forEach(box -> this.moneyBoxMap.put(box.getValue(),box));
    }

    @Deprecated
    boolean addAsset(int val , int qty){
        moneyBoxMap.get(val).insert(qty);
        return true;
    }

    boolean  addAsset(CashPack note) {
        for (var b : boxList) {
            if(b.addPack(note)) return true;
        }
        return false;
    }
    public List<CashPack> withdraw(int amount) throws Exception {
        int cnt;
        int val ;
        List<CashPack> cash =  new ArrayList<>();
        for ( MoneyBox box : getNotEmtyBoxes()) {
            val = box.getValue();
            cnt = amount / val;
            if(cnt > box.getQty()) cnt= box.getQty();
            amount = amount- (cnt*val);
            cash.add(box.wdrawCash(cnt));
        }
        if(amount>0) throw  new  Exception("Can get combination of notes. Change amount");

        return  cash;
    }

    public CashPack withdraw(int value, int qtty) throws Exception {
        CashPack cash;
                 switch (value){
                    case 100: cash =wdraw100(qtty);
                        break;
                    case 50: cash =wdraw50(qtty);
                        break;
                    case 20: cash =wdraw20(qtty);
                        break;
                    case 10: cash =wdraw10(qtty);
                        break;
                    case 5: cash =wdraw5(qtty);
                        break;
                    case 2: cash =wdraw2(qtty);
                        break;
                    case 1: cash =wdraw1(qtty);
                        break;
                        default: throw new Exception ("Value not found");
                 };

                 return  cash;
    }


    private CashPack wdraw100(int qtty) throws Exception {
        return box100.wdrawCash(qtty);
    }
    private CashPack wdraw50(int qtty) throws Exception {
        return box50.wdrawCash(qtty);
    }
    private CashPack wdraw20(int qtty) throws Exception {
        return box20.wdrawCash(qtty);
    }
    private CashPack wdraw10(int qtty) throws Exception {
        return box10.wdrawCash(qtty);
    }
    private CashPack wdraw5(int qtty) throws Exception {
        return box5.wdrawCash(qtty);
    }
    private CashPack wdraw2(int qtty) throws Exception {
        return box2.wdrawCash(qtty);
    }
    private CashPack wdraw1(int qtty) throws Exception {
        return box1.wdrawCash(qtty);
    }

    public  Double sumAmount()
    {
        int result = boxList.stream().mapToInt(MoneyBox::getAmount).sum();
        return  (double) result;
    }

    public List<CashPack> getPacks(){
        List<CashPack> cashPacks = new ArrayList<>();
        boxList.stream()
                .filter(b -> !b.isEmpty())
                .forEach(b-> cashPacks.add(b.getPack()));
        return cashPacks;
    }

    public List<MoneyBox> getNotEmtyBoxes(){
        return  boxList.stream()
                .filter(b -> !b.isEmpty())
                .collect(Collectors.toList());
    }
    @Override
    public String toString() {
        return "MoneyUnit{" +
                "box100=" + box100 +
                ", box50=" + box50 +
                ", box20=" + box20 +
                ", box10=" + box10 +
                ", box5=" + box5 +
                ", box2=" + box2 +
                ", box1=" + box1 +
                '}';
    }

}
