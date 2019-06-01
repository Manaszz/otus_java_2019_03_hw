package ru.otus.kchu.ATMemul;

import java.util.List;
import java.util.Map;

public class ATM {
    MoneyUnit moneyBoxes = new MoneyUnit();

    @Deprecated
    void  inputCash(Map<Integer,Integer> cash) {
        cash.forEach((val,qty)-> moneyBoxes.addAsset(val,qty));
    }
    void  inputCash(List<CashPack> cash) {
        cash.forEach((note)-> moneyBoxes.addAsset(note));
    }

    void giveCash(int amount) throws Exception {
        if (amount > moneyBoxes.sumAmount()) throw (new Exception("Not enough money"));

        int cnt;
        int val ;
//        List<CashPack> cashInBoxes = moneyBoxes.getPacks();
//        for (CashPack pack :cashInBoxes) {
//            val = pack.getValue();
//            cnt = amount / val;
//            if(cnt > pack.getQtty()) cnt= pack.getQtty();
//            amount = amount- (cnt*val);
 //           cash.add(moneyBoxes.withdraw(val,cnt));
//        }
/**
* Первая попытка↑ была извлекать деньги через кейс по номиналу  (MoneyUnit.withdraw(int,int) . не понравилось)
* Вторая ↓- напрямую из ящиков
*/
//        for ( MoneyBox box : moneyBoxes.getNotEmtyBoxes()) {
//            val = box.getValue();
//            cnt = amount / val;
//            if(cnt > box.getQty()) cnt= box.getQty();
//            amount = amount- (cnt*val);
//            cash.add(box.wdrawCash(cnt));
//        }
//        if(amount>0) throw  new  Exception("Can get combination of notes. Change amount");

/** В итоге перенес работу с ящиками непосредственно в MoneyUnit
 */
        List<CashPack> cash = moneyBoxes.withdraw(amount);

        System.out.println("Got cash:"+cash);
    }

    Double getAmount(){
        return moneyBoxes.sumAmount();
    };

}
