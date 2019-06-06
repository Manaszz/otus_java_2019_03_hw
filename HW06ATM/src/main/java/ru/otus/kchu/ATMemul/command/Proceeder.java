package ru.otus.kchu.ATMemul.command;

import ru.otus.kchu.ATMemul.CashPack;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/** The Invoker class */
public class Proceeder {
    private final Deque<Command> history = new LinkedList<>();

    public void runCommand(Command cmd, Object... args) {
        this.history.add(cmd); // optional

            cmd.getExecArgs(args).execute();

    }

    public void undo() {
        if (history.size() > 1) {
            history.removeLast();
//            history.getLast().execute();
        }
    }
}


