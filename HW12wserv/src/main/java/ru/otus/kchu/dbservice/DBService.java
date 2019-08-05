package ru.otus.kchu.dbservice;

import ru.otus.kchu.dao.User;

import java.util.List;
import java.util.Map;

public interface DBService {

    <T> void create(T objectData) throws IllegalAccessException;
    <T> void update(T objectData);
    <T> void createOrUpdate(T objectData); // опционально.
    <T> T load(long id, Class<T> clazz);
    <T> List<T> GetEntities(Class<T> Clazz  );

}
