package ru.otus.kchu.ATMemul.composit;


import ru.otus.kchu.ATMemul.ATMUnit;
import ru.otus.kchu.ATMemul.CashPack;
import ru.otus.kchu.ATMemul.chain.Middleware;
import ru.otus.kchu.ATMemul.chain.OnlineCheckMiddleware;
import ru.otus.kchu.ATMemul.chain.ResetCheckMiddleware;
import ru.otus.kchu.ATMemul.momento.Memento;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ATMGroup extends Observable implements ATMUnit {
    List<ATMUnit> atms = new ArrayList<>();
    private Middleware middleware;

    public ATMGroup() {
        Middleware mw = new OnlineCheckMiddleware();
        mw.linkWith(new ResetCheckMiddleware());

        setMiddleware(mw);
    }

    public void setMiddleware(Middleware middleware) {
        this.middleware = middleware;
    }

    public void addUnit(ATMUnit unit) {
        atms.add(unit);
    }


    @Override
    public void inputCash(List<CashPack> cash) {
                atms.forEach(unit -> inputCash(cash));
    }

    @Override
    public void giveCash(int amount) {

    }

    @Override
    public Double getAmount() {
        double sum =0;
        for (ATMUnit unit :atms) {
            sum = sum + unit.getAmount();
        }
        return sum;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void resetState() {
    int succeed =0;
        for (ATMUnit atm : atms) {
//            atm.resetState();
            if (middleware.check(atm)) {
                System.out.println(atm.getId()+ "Reseted");
                succeed++;
            }
        }
        System.out.println("On default :" + succeed+"/" +atms.size());
    }

    @Override
    public void setDefault(Memento memento) {

    }

    @Override
    public void setDefaultCurrent() {
        atms.forEach(ATMUnit::setDefaultCurrent);
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public void setOnline(boolean b) {

        atms.forEach(unit -> setOnline(false));
    }

    public ATMUnit getAtm(int idx){
        return atms.get(idx-1);
    }
    public ATMUnit getAtm(String id){
        for ( var unit :atms) {
            if( id.equalsIgnoreCase(unit.getId())) return unit;
        }
        return null;
    }

    public void ping(){
        atms.forEach(ATMUnit::ping);
    }
}
