package ru.otus.kchu.ATMemul;

import ru.otus.kchu.ATMemul.command.AddCommand;
import ru.otus.kchu.ATMemul.command.Command;
import ru.otus.kchu.ATMemul.command.Proceeder;
import ru.otus.kchu.ATMemul.command.WdrawCommand;
import ru.otus.kchu.ATMemul.momento.Memento;

import java.util.*;

public class ATM extends Observable implements ATMUnit {
    boolean isOnline;
    public final String atm_id ;
    MoneyUnit moneyBoxes = new MoneyUnit();
    private Command withdraw = new WdrawCommand(moneyBoxes);
    private Command addAssets = new AddCommand(moneyBoxes);
    Proceeder  ATMCommand = new Proceeder();

    public ATM(){
    isOnline =true;
    atm_id = "MYATM"+String.valueOf(new Random().nextInt(10000));
    setDefaultCurrent();
    addObservers();
    }

    public ATM(String id){
    isOnline =true;
    this.atm_id=id;
    setDefaultCurrent();
    addObservers();
    }
    private void addObservers(){
        addObserver((obj, arg) -> {
            ATMUnit  atm = (ATMUnit)arg;
            System.out.print("---Default: "); this.moneyBoxes.getDefault();
        });

        addObserver((obj, arg) -> {
            ATMUnit  atm = (ATMUnit)arg;
            System.out.println("---State: " + (atm.isOnline()?"online,":"offline,")+ this.moneyBoxes);
        });

        addObserver((obj, arg) -> {
            System.out.println("Call : " + ((ATMUnit)arg).getId());
        });
    }

    public boolean isOnline(){
        return this.isOnline;
    }

    @Deprecated
    void  inputCash(Map<Integer,Integer> cash) {
        cash.forEach((val,qty)-> moneyBoxes.addAsset(val,qty));
    }

    @Override
    public void  inputCash(List<CashPack> cash) {
//        cash.forEach((note)-> moneyBoxes.addAsset(note));  // replaced by Command
        ATMCommand.runCommand(addAssets,cash);
    }

    @Override
    public void giveCash(int amount) throws Exception {
        if (amount > moneyBoxes.sumAmount()) throw (new IllegalArgumentException("Not enough money"));
        List<CashPack> cash = new ArrayList<>();
/** Replaced by Command:
 *List<CashPack> cash = moneyBoxes.withdraw(amount);
 *System.out.println("Got cash:"+cash);
 */
        ATMCommand.runCommand(withdraw,amount);
    }

    @Override
    public Double getAmount(){
        return moneyBoxes.sumAmount();
    }

    @Override
    public String getId() {
        return atm_id;
    }

    @Override
    public String toString() {
        return "ATM{" +
                "moneyBoxes=" + moneyBoxes +
                '}';
    }

    @Override
    public void resetState() {
    moneyBoxes.restoreDefault();
    }

    @Override
    public void setDefault(Memento memento) {
        moneyBoxes.saveStateToDefault(memento);
    }

    @Override
    public void setDefaultCurrent() {
        moneyBoxes.saveStateToDefault();
    }
    public void setOnline(boolean s)
    {
        isOnline =s;
    }
    public void ping(){
        setChanged();
        notifyObservers(this);
    }
}
