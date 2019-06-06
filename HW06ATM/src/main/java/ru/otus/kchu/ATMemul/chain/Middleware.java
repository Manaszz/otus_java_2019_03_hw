package ru.otus.kchu.ATMemul.chain;

import ru.otus.kchu.ATMemul.ATMUnit;

/**
 * Base middleware class.
 */
public abstract class Middleware {
    private Middleware next;

    /**
     * Builds chains of middleware objects.
     */
    public Middleware linkWith(Middleware next) {
        this.next = next;
        return next;
    }

    /**
     * Subclasses will implement this method with concrete checks.
     */
    public abstract boolean check(ATMUnit atm);

    /**
     * Runs check on the next object in chain or ends traversing if we're in
     * last object in chain.
     */
    protected boolean checkNext(ATMUnit atm) {
        if (next == null) {
            return true;
        }
        return next.check(atm);
    }
}
