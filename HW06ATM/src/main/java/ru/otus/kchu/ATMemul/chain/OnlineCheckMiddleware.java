package ru.otus.kchu.ATMemul.chain;

import ru.otus.kchu.ATMemul.ATMUnit;

public class OnlineCheckMiddleware extends Middleware {
    public boolean check(ATMUnit atm) {
        if (!atm.isOnline()) {
            System.out.println("ATM "+atm.getId()+" is offline");
            return false;
        }
        System.out.println(atm.getId()+" online.... ready");
        return checkNext(atm);
    }
}
