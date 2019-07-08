package ru.otus.kchu.dbservice;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Id;
import java.lang.reflect.Field;

public class DbServiceHiber implements DBService {

    private final SessionFactory sessionFactory;


    public DbServiceHiber(SessionFactory sessionFactory) {
        this.sessionFactory =sessionFactory;
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
            session.getTransaction().commit();
            return  record;
        }catch (Exception ex) {
            ex.printStackTrace();
            return null;

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
        T loaded = getByObject(objectData);

        try (Session session2 = sessionFactory.openSession()) {
        session2.beginTransaction();
            if(loaded == null) {
                session2.save(objectData);
            }
            else   {
                session2.update(objectData);
            }

            session2.getTransaction().commit();
            System.out.println("-----------------create/updated user:"+objectData);
        }

    }

    private <T> T getByObject(T objectData) {
        Class clazz = objectData.getClass();
        Field idFld =checkId(clazz);
        T loaded = null;
        try (Session session = sessionFactory.openSession()) {

            loaded = (T) session.get(clazz, (long) idFld.get(objectData));
            } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return loaded;
    }

    private Field checkId(Class clazz) {

        for (Field field: clazz.getDeclaredFields()){
            field.setAccessible(true);
            if(field.isAnnotationPresent(Id.class)) {
                return  field;
            }
            field.setAccessible(false);
        }
        return  null;
    }
}
