package ru.otus.kchu.ATMemul;

import ru.otus.kchu.ATMemul.momento.Memento;

import java.util.List;

public interface ATMUnit {
    void  inputCash(List<CashPack> cash);

    void giveCash(int amount) throws Exception;

    Double getAmount();
    String getId();
    void resetState();
    void setDefault(Memento memento);
    void setDefaultCurrent();
    void  ping();
    boolean isOnline();

    void setOnline(boolean b);
}
