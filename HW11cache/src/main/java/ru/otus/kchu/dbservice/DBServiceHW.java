package ru.otus.kchu.dbservice;

import org.hibernate.SessionFactory;
import ru.otus.kchu.cache.CacheEngineSoftImpl;
import ru.otus.kchu.dao.Account;
import ru.otus.kchu.dao.AddressDataSet;
import ru.otus.kchu.dao.Phone;
import ru.otus.kchu.dao.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class DBServiceHW {
    public static void main(String[] args) throws SQLException, IllegalAccessException {
//        DataSource dataSource = new DataSourceH2();
//        createTable(dataSource);
//        DBService dbServiceUser = new DbServiceJdbc(dataSource);
        SessionFactory sessionFactory =  new HiberCfgBuilder("hibernate.cfg.xml")
                .addClass(User.class)
                .addClass(Account.class)
                .addClass(AddressDataSet.class)
                .addClass(Phone.class)
                .buildFactory();

//        DBService dbServiceUser = new DbServiceHiber(cfgPath,User.class,Account.class ,AddressDataSet.class , Phone.class);
//        DBService dbServiceUser = new DbServiceHiber(sessionFactory);
        DBService dbServiceUser = new DbServiceHiber(sessionFactory,  new CacheEngineSoftImpl<>(10, 1000, 0, false));

        User usr1 = new User(10,"User10", 30);

        List<Phone> listPhone = Arrays.asList(new Phone("+7926888888", usr1),new Phone("+8123456789", usr1));
        AddressDataSet addr = new AddressDataSet("qwe str.",usr1);
        usr1.setPhones(listPhone);
        usr1.setAddres(addr);
        System.out.println(addr);
        System.out.println(listPhone);
        System.out.println(usr1);

        dbServiceUser.create(usr1);
        dbServiceUser.create(new User(11,"User11", 30));

        System.out.println("--------------Loaded not existing:" +dbServiceUser.load(12,usr1.getClass()));

        User usr12 = dbServiceUser.load(usr1.getId(),usr1.getClass());
        System.out.println("--------------Loaded:" +usr12);

        usr1.setAge(35);
        dbServiceUser.update(usr1);

        User usr2 = dbServiceUser.load(usr1.getId(),usr1.getClass());
        System.out.println("--------------Loaded:" +usr2);

        usr1.setAge(40);
        dbServiceUser.createOrUpdate(usr1);
        System.out.println("--------------Load:"+dbServiceUser.load(usr1.getId(),usr1.getClass()));

        User userz = new User(2,"User2", 10);
        dbServiceUser.createOrUpdate(userz);
        System.out.println("--------------Load:"+dbServiceUser.load(userz.getId(),userz.getClass()));

        //----------------------
        Account acc1 = new Account(10,"Account10", 30);
        dbServiceUser.create(acc1);
        dbServiceUser.create(new Account(11,"Account11", 30));

        //dbServiceUser.getUser(acc1.getId());
        acc1.setRest(35);
        dbServiceUser.update(acc1);

        Account acc2 = dbServiceUser.load(acc1.getId(),acc1.getClass());
        System.out.println("Loaded:" +acc2);

        acc1.setRest(40);
        dbServiceUser.createOrUpdate(acc1);
        System.out.println("Load:"+dbServiceUser.load(acc1.getId(),acc1.getClass()));

        dbServiceUser.createOrUpdate(new Account(2,"Account2", 10));
        System.out.println("Load:"+dbServiceUser.load(2,acc1.getClass()));

        ((DbServiceHiber) dbServiceUser).cacheDispose();

    }

    private static void createTable(DataSource dataSource) throws SQLException {
        String usrTableCreate ="create table user(id bigint(20) NOT NULL auto_increment, name varchar(50), age int(3))";
        String accTableCreate ="create table Account(no bigint(20) NOT NULL auto_increment, type varchar(225), rest number)";
        String[] tablesSQL ={usrTableCreate,accTableCreate};
        for (String tableSQL:
             tablesSQL) {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement pst = connection.prepareStatement(tableSQL)
            ) {
                pst.executeUpdate();
            }

            System.out.println("table created");
        }

    }
}
