package ru.otus.kchu.services.dbservice;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import ru.otus.kchu.dao.Account;
import ru.otus.kchu.dao.AddressDataSet;
import ru.otus.kchu.dao.Phone;
import ru.otus.kchu.dao.User;
import ru.otus.kchu.services.cache.CacheEngine;
import ru.otus.kchu.services.cache.CacheEngineSoftImpl;

import java.util.Arrays;
import java.util.List;

@Service
public class DBServiceFactory {
    private DBService dbServiceUser;

    public DBService createService(String cfgPath, Class... classes) {
        HiberCfgBuilder hiberCfgBuilder = new HiberCfgBuilder(cfgPath);
        for(Class c:classes){
            hiberCfgBuilder.addClass(c);
        }
        SessionFactory sessionFactory =  hiberCfgBuilder
                .buildFactory();

        dbServiceUser = new DbServiceHiber(sessionFactory,  new CacheEngineSoftImpl<>(10, 1000, 0, false));

        return dbServiceUser;
    }

    public DBService createService(SessionFactory sessionFactory ) {
        dbServiceUser = new DbServiceHiber(sessionFactory,  new CacheEngineSoftImpl<>(10, 1000, 0, false));

        return dbServiceUser;
    }
    public DBService createService(SessionFactory sessionFactory, CacheEngine cash) {
        dbServiceUser = new DbServiceHiber(sessionFactory, cash);

        return dbServiceUser;
    }

    public  static void  dataIni(DBService dbServiceUser) throws  IllegalAccessException {
//        DataSource dataSource = new DataSourceH2();
//        createTable(dataSource);
//        DBService dbServiceUser = new DbServiceJdbc(dataSource);


        User usr1 = new User(10,"User10", 30);

        List<Phone> listPhone = Arrays.asList(new Phone("+7926888888", usr1),new Phone("+8123456789", usr1));
        AddressDataSet addr = new AddressDataSet("qwe str.",usr1);
//       usr1.setPhones(listPhone);

        dbServiceUser.create(usr1);
        dbServiceUser.create(new User(11,"User11", 30));
        User usr12 = dbServiceUser.load(usr1.getId(),usr1.getClass());
        System.out.println("--------------Loaded:" +usr12);

        usr1.setAge(35);
        dbServiceUser.update(usr1);

        User usr2 = dbServiceUser.load(usr1.getId(),usr1.getClass());

        usr1.setAge(40);
        dbServiceUser.createOrUpdate(usr1);
        User userz = new User(2,"User2", 10);
        dbServiceUser.createOrUpdate(userz);
        //----------------------
        Account acc1 = new Account(10,"Account10", 30);
        dbServiceUser.create(acc1);
        dbServiceUser.create(new Account(11,"Account11", 30));

        acc1.setRest(35);
        dbServiceUser.update(acc1);

        acc1.setRest(40);
        dbServiceUser.createOrUpdate(acc1);

        dbServiceUser.createOrUpdate(new Account(2,"Account2", 10));

        List userList = dbServiceUser.GetEntities(User.class);
        List accList = dbServiceUser.GetEntities(Account.class);

        System.out.println("All users:");
        userList.forEach(e-> System.out.println(e));
        System.out.println("All accs:");
        accList.forEach(e-> System.out.println(e));

    }


}
