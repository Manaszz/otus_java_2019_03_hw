package ru.otus.kchu.repository.dbrepository;

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
    private DBRepository dbRepositoryUser;

    public DBRepository createRepository(String cfgPath, Class... classes) {
        HiberCfgBuilder hiberCfgBuilder = new HiberCfgBuilder(cfgPath);
        for(Class c:classes){
            hiberCfgBuilder.addClass(c);
        }
        SessionFactory sessionFactory =  hiberCfgBuilder
                .buildFactory();

        dbRepositoryUser = new DbRepositoryHiber(sessionFactory,  new CacheEngineSoftImpl<>(10, 1000, 0, false));

        return dbRepositoryUser;
    }

    public DBRepository createRepository(SessionFactory sessionFactory ) {
        dbRepositoryUser = new DbRepositoryHiber(sessionFactory,  new CacheEngineSoftImpl<>(10, 1000, 0, false));

        return dbRepositoryUser;
    }
    public DBRepository createRepository(SessionFactory sessionFactory, CacheEngine cash) {
        dbRepositoryUser = new DbRepositoryHiber(sessionFactory, cash);

        return dbRepositoryUser;
    }

    public  static void  dataIni(DBRepository dbRepositoryUser) throws  IllegalAccessException {
//        DataSource dataSource = new DataSourceH2();
//        createTable(dataSource);
//        DBService dbServiceUser = new DbServiceJdbc(dataSource);


        User usr1 = new User(10,"User10", 30);

        List<Phone> listPhone = Arrays.asList(new Phone("+7926888888", usr1),new Phone("+8123456789", usr1));
        AddressDataSet addr = new AddressDataSet("qwe str.",usr1);
//       usr1.setPhones(listPhone);

        dbRepositoryUser.create(usr1);
        dbRepositoryUser.create(new User(11,"User11", 30));
        User usr12 = dbRepositoryUser.load(usr1.getId(),usr1.getClass());
        System.out.println("--------------Loaded:" +usr12);

        usr1.setAge(35);
        dbRepositoryUser.update(usr1);

        User usr2 = dbRepositoryUser.load(usr1.getId(),usr1.getClass());

        usr1.setAge(40);
        dbRepositoryUser.createOrUpdate(usr1);
        User userz = new User(2,"User2", 10);
        dbRepositoryUser.createOrUpdate(userz);
        //----------------------
        Account acc1 = new Account(10,"Account10", 30);
        dbRepositoryUser.create(acc1);
        dbRepositoryUser.create(new Account(11,"Account11", 30));

        acc1.setRest(35);
        dbRepositoryUser.update(acc1);

        acc1.setRest(40);
        dbRepositoryUser.createOrUpdate(acc1);

        dbRepositoryUser.createOrUpdate(new Account(2,"Account2", 10));

        List userList = dbRepositoryUser.GetEntities(User.class);
        List accList = dbRepositoryUser.GetEntities(Account.class);

        System.out.println("All users:");
        userList.forEach(e-> System.out.println(e));
        System.out.println("All accs:");
        accList.forEach(e-> System.out.println(e));

    }


}
