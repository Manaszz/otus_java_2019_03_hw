package ru.otus.dbservice;

public interface DBService {

    <T> void create(T objectData) throws IllegalAccessException;
    <T> void update(T objectData);
    <T> void createOrUpdate(T objectData); // опционально.
    <T> T load(long id, Class<T> clazz);

}
