package ru.otus.kchu.ATMemul.chain;

import ru.otus.kchu.ATMemul.ATMUnit;

public class ResetCheckMiddleware extends Middleware {
    @Override
    public boolean check(ATMUnit atm) {
        try {
            atm.resetState();
            System.out.println(atm.getId()+ " setted on default:"+ atm.toString());

        }catch (Exception e)
        {
            System.out.println("Error on reset state");
            return false;
        }
        return checkNext(atm);
    }
}
