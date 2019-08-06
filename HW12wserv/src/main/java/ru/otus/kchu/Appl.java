package ru.otus.kchu;

import ru.otus.kchu.dao.Account;
import ru.otus.kchu.dao.AddressDataSet;
import ru.otus.kchu.dao.Phone;
import ru.otus.kchu.dao.User;
import ru.otus.kchu.dbservice.*;
import ru.otus.kchu.webservice.WSJettyMain;

public class Appl {
    private final static int PORT = 8080;

public static void main(String[] args) throws Exception {
        DBService dbServ = new DBServiceFactory().createService("hibernate.cfg.xml", User.class, Account.class, AddressDataSet.class, Phone.class);
        DBServiceFactory.dataIni(dbServ);
        WSJettyMain webServ = new WSJettyMain(dbServ);

        webServ.start();
    }
}
