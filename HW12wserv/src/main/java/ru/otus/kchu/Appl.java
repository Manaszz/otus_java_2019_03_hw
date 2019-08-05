package ru.otus.kchu;

import ru.otus.kchu.dbservice.*;
import ru.otus.kchu.webservice.WSJettyMain;

public class Appl {
    private final static int PORT = 8080;

public static void main(String[] args) throws Exception {
        DBService dbServ = new DBServiceMain().DataIni("hibernate.cfg.xml") ;
        new WSJettyMain(dbServ);

    }
}
