package ru.otus.kchu.ATMemul;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATM_hw {

    public static void main(String[] args) {
        Map<Integer,Integer> cash = new HashMap<>();
        cash.put(100,3);
        cash.put(10,5);
        cash.put(50,2);

        List<CashPack> cashPacks= Arrays.asList(new CashPack(100,1), new CashPack(10,5),new CashPack(20,3));

        System.out.println("Cash Map:"+cash);
        System.out.println("Cash List:"+cashPacks);

        ATM  myATM = new ATM();
        myATM.inputCash(cash);
        myATM.inputCash(cashPacks);

        System.out.println(myATM.moneyBoxes);
        System.out.println(myATM.getAmount());
        int bablo = 550;
        try {
            System.out.println("withdraw "+bablo);
            myATM.giveCash(bablo);
            System.out.println("New sum left:"+myATM.getAmount());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
}
