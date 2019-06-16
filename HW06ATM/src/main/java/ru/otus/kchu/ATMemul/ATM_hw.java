package ru.otus.kchu.ATMemul;

import ru.otus.kchu.ATMemul.composit.ATMGroup;

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
        List<CashPack> cashPacks= Arrays.asList(new CashPack(NoteValues.v100,1), new CashPack(NoteValues.v10,5),new CashPack(NoteValues.v20,3));
        System.out.println("Cash Map:"+cash);
        System.out.println("Cash List:"+cashPacks);

        ATM  myATM = new ATM();
        myATM.inputCash(cash);
        myATM.inputCash(cashPacks);

        System.out.println(myATM.moneyBoxes);
        System.out.println(myATM.getAmount());
        myATM.setDefaultCurrent();
        int bablo = 150;
        try {
            System.out.println("withdraw "+bablo);
            myATM.giveCash(bablo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            System.out.println("zNew sum left:"+myATM.getAmount());
        }
        System.out.println("New sum left:"+myATM.getAmount());

        System.out.println("---Start ATM department---");
        ATMGroup ATMDept = new ATMGroup();
        ATMDept.addUnit(myATM);
        ATMUnit atm2 = new ATM("Atm2");
        atm2.inputCash(cashPacks);
        atm2.setDefaultCurrent();
        atm2.inputCash(cashPacks);
        ATMDept.addUnit(atm2);

        ATMUnit atm3 = new ATM();
        ATMDept.addUnit(atm3);

        ATMDept.getAtm("Atm2").setOnline(false);
        ATMDept.getAtm(3).inputCash(cashPacks);

        ATMDept.ping();
        System.out.println("Total amount:"+ATMDept.getAmount());

        ATMDept.resetState();
        ATMDept.ping();

    }
}
