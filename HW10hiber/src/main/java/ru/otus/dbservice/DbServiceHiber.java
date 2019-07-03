package ru.otus.dbservice;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import ru.otus.dao.*;
import ru.otus.executor.DbExecutor;
import ru.otus.executor.DbExecutorImpl;

import javax.persistence.Id;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbServiceHiber implements DBService {

    private final SessionFactory sessionFactory;

    public DbServiceHiber() {
        Configuration configuration = new Configuration()
                .configure("hibernate.cfg.xml");

        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        Metadata metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Account.class)
                .addAnnotatedClass(AddressDataSet.class)
                .addAnnotatedClass(Phone.class)
                .getMetadataBuilder()
                .build();

        sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    @Override
    public <T> void create(T objectData) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.save(objectData);
            System.out.println("-----------------created user:" + objectData );
            session.getTransaction().commit();
        }
    }
    @Override
    public <T> T load(long id, Class<T> clazz) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            T record = session.get(clazz, id);
            System.out.println("-----------------new object:"+record);

            return  record;
        }catch (Exception ex) {
            ex.printStackTrace();
            return null;
//            throw new RuntimeException(ex);
        }
    }

    @Override
    public <T> void update(T objectData) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.update(objectData);
            session.getTransaction().commit();

            System.out.println("-----------------updated user:"+objectData);
        }
    }

    @Override
    public <T> void createOrUpdate(T objectData) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.saveOrUpdate(objectData);
            session.getTransaction().commit();
            System.out.println("-----------------create/updated user:"+objectData);
        }

    }
}
