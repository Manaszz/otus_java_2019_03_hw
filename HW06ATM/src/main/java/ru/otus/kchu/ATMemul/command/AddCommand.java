package ru.otus.kchu.ATMemul.command;

import ru.otus.kchu.ATMemul.CashPack;
import ru.otus.kchu.ATMemul.MoneyUnit;

import java.util.List;

public class AddCommand implements Command {
    private final MoneyUnit moneyUnit;
    List<CashPack> pack;


    public AddCommand(MoneyUnit moneyBoxes) {
        this.moneyUnit = moneyBoxes;

    }

    @Override
    public void execute() {
        pack.forEach((note)-> moneyUnit.addAsset(note));
    }

    @Override
    public Command getExecArgs(Object... args) {
    this.pack = (List) args[0];
    return this;
    }
}
