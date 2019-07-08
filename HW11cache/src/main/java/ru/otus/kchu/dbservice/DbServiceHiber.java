package ru.otus.kchu.dbservice;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.otus.kchu.cache.CacheEngine;
import ru.otus.kchu.cache.CacheEngineSoftImpl;
import ru.otus.kchu.cache.MyElement;

import javax.persistence.Id;
import java.lang.reflect.Field;

public class DbServiceHiber <K, T>implements DBService {
    int CashSize = 10;
    private final SessionFactory sessionFactory;
    CacheEngine<Long, MyElement<K, T>> cache ;


    public DbServiceHiber(SessionFactory sessionFactory) {
        this.sessionFactory =sessionFactory;
        this.cache = new CacheEngineSoftImpl<>(CashSize, 1000, 0, false);
    }
    public DbServiceHiber(SessionFactory sessionFactory, CacheEngine cache) {
        this.sessionFactory =sessionFactory;
        this.cache = cache;
    }

    @Override
    public <T> void create(T objectData) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(objectData);
            System.out.println("-----------------created user:" + objectData );
            session.getTransaction().commit();

            putInCache(objectData);
        }

    }
    @Override
    public <T> T load(long id, Class<T> clazz) {

        MyElement cachedEl = cache.get(id);
        System.out.println("Cashed for " + id + ": " + (cachedEl != null ? cachedEl.getValue() : "null"));
        if (cachedEl != null) {
            return (T) cachedEl.getValue();
        } else {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                T record = session.get(clazz, id);
                System.out.println("-----------------new object:" + record);
                session.getTransaction().commit();

                putInCache(record);

                return record;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public <T> void update(T objectData) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.update(objectData);
            session.getTransaction().commit();

            putInCache(objectData);

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

            putInCache(objectData);
            System.out.println("-----------------create/updated user:"+objectData);
        }
    }

    private <T> T getByObject(T objectData) {

        Class clazz = objectData.getClass();
        Field idFld =checkId(clazz);
        T loaded = null;
        Long id = null;
        try {
            id = (long) idFld.get(objectData);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        MyElement cachedEl = cache.get(id);
        System.out.println("Cashed for " + id + ": " + (cachedEl != null ? cachedEl.getValue() : "null"));

        if (cachedEl != null) {
            return (T) cachedEl.getValue();
        } else {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                loaded = (T) session.get(clazz,id);
                session.getTransaction().commit();
                putInCache(loaded);
            }
            return loaded;
        }
    }

    private <T> void putInCache(T objectData) {
        MyElement el = null;
        if(objectData==null) return;
        try {
            el = new MyElement<>(getIdVal(objectData),objectData);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        cache.put(el);
    }

    public void  cacheDispose(){
        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());
        cache.dispose();
    }
    private <T> Long getIdVal(T objectData) throws IllegalAccessException {
        Class clazz = objectData.getClass();
        Field idFld =checkId(clazz);
        return (long) idFld.get(objectData);

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
