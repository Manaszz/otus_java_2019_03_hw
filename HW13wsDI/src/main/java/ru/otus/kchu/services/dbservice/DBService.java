package ru.otus.kchu.services.dbservice;

import org.hibernate.SessionFactory;
import ru.otus.kchu.services.cache.CacheEngine;

import java.util.List;

public interface DBService {
    void init(SessionFactory sessionFactory, CacheEngine cash);
    <T> void create(T objectData) throws IllegalAccessException;
    <T> void update(T objectData);
    <T> void createOrUpdate(T objectData); // опционально.
    <T> T load(long id, Class<T> clazz);
    <T> List<T> GetEntities(Class<T> Clazz);

}
