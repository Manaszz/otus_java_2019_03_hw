package ru.otus.kchu.ATMemul.momento;
import ru.otus.kchu.ATMemul.MoneyBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Memento {
    private final ArrayList<MoneyBox> boxesState;

    public Memento(List<MoneyBox> boxList)  {
        boxesState = new ArrayList<>();
        boxList.forEach((b)->boxesState.add(new MoneyBox(b)) );
        System.out.println("Momento!:"+boxesState);
    }

    public List<MoneyBox> getState() {
        return this.boxesState;
    }

    @Override
    public String toString() {
        return "Memento{" +
                "boxesState=" + boxesState +
                '}';
    }
}
