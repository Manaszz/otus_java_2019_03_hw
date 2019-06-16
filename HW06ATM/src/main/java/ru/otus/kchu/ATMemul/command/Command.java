package ru.otus.kchu.ATMemul.command;

import ru.otus.kchu.ATMemul.CashPack;

import java.util.Collections;

/** The Command interface */
public interface Command {
    void execute();
    Command getExecArgs( Object... args);
}
