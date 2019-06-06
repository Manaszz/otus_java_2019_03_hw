package ru.otus.kchu.ATMemul.command;

import ru.otus.kchu.ATMemul.CashPack;
import ru.otus.kchu.ATMemul.MoneyUnit;
import ru.otus.kchu.ATMemul.momento.Caretaker;

import java.util.List;

public class WdrawCommand  implements Command {
    private final MoneyUnit moneyUnit;
    List<CashPack> pack;
    private int amount;

    Caretaker  caretaker =new Caretaker();

    public WdrawCommand(MoneyUnit moneyUnit) {
        this.moneyUnit = moneyUnit;
    }

    @Override    // Command
    public void execute() {
//        light.turnOn();
        caretaker.setMemento(moneyUnit.saveState());

        try {
            this.pack = moneyUnit.withdraw(amount);
        } catch (Exception e) {
            moneyUnit.restoreState(caretaker.getMemento());
            System.out.println( e.getMessage());
        }
    }

    @Override
    public Command getExecArgs(Object... args) {
        this.amount= (int) args[0];

//      this.pack= (List)args[1];

        return this;
    }

}

