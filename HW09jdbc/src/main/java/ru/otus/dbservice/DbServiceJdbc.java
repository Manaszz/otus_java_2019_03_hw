package ru.otus.dbservice;

import ru.otus.dao.Id;
import ru.otus.executor.DbExecutor;
import ru.otus.executor.DbExecutorImpl;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class DbServiceJdbc implements DBService {

    private final DataSource dataSource;
    Map<Class, SqlScripture> classesMetaData;

    public DbServiceJdbc(DataSource dataSource)  {
        this.dataSource = dataSource;
        classesMetaData =new HashMap<>();
    }

    @Override
    public <T> void create(T objectData) throws IllegalAccessException {

        final Class  clazz = objectData.getClass();
        SqlScripture scriptor ;

        if(classesMetaData.containsKey(clazz))
            scriptor = classesMetaData.get(clazz);
        else {
            System.out.println("------------- New class:"+clazz);
            scriptor = new SqlScripture(clazz);
            classesMetaData.put(clazz,scriptor);
        }

        Field idFld = scriptor.getIdFld();
        List params =new ArrayList<>();

        if(idFld == null){
            System.out.println("ID not found"); return;
        }

        for (Field f : scriptor.getFldsList()) {
             f.setAccessible(true);
            params.add(f.get(objectData));
            f.setAccessible(false);
        }

        String sql = scriptor.getInsertQuery();

        try (Connection connection = dataSource.getConnection()) {
            DbExecutor<?> executor = new DbExecutorImpl<>(connection);
            executor.insertUpdateRecord(sql, params);
            connection.commit();
            System.out.println("created user("+idFld.get(objectData)+"):" + objectData );
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

    }
    @Override
    public <T> T load(long id, Class<T> clazz) {

        try (Connection connection = dataSource.getConnection()) {
            DbExecutor<T> executor = new DbExecutorImpl<>(connection);
            SqlScripture scriptor ;

            if(classesMetaData.containsKey(clazz))
                scriptor = classesMetaData.get(clazz);
            else {
                System.out.println("------------- New class:"+clazz);
                scriptor = new SqlScripture(clazz);
                classesMetaData.put(clazz,scriptor);
            }

            String sql = scriptor.getSelectQuery();
            Optional<T> record = executor.selectRecord(sql, id,
                    resultSet -> {
                        try {
                            if (resultSet.next()) {
                               T newObj = clazz.newInstance();
                                for (Field field : scriptor.getFldsList()){
                                             ReflectionHelper.setFieldValue(newObj,field.getName(),resultSet.getObject(field.getName()));
                                }
                                //T resObj =clazz.getDeclaredConstructor().newInstance(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getInt("age"));
                                return newObj;
                            }
                        } catch (SQLException |  InstantiationException | IllegalAccessException  e) {
                            e.printStackTrace();
                        }
                        return null;
                    });
            System.out.println("new object:"+record);
            return record.orElse(null);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public <T> void update(T objectData) {
        final Class  clazz = objectData.getClass();
        SqlScripture scriptor ;

        if(classesMetaData.containsKey(clazz))
            scriptor = classesMetaData.get(clazz);
        else {
            System.out.println("------------- New class:"+clazz);
            scriptor = new SqlScripture(clazz);
            classesMetaData.put(clazz,scriptor);
        }
        Field idFld = scriptor.getIdFld();

        List params =new ArrayList<>();
        try {
            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);
                params.add(f.get(objectData));
                f.setAccessible(false);
            }

            if(idFld == null){
                System.out.println("ID not found"); return;
            }else {
                params.add(idFld.get(objectData));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        String sql = scriptor.getUpdateQuery();

        try (Connection connection = dataSource.getConnection()) {
            DbExecutor<?> executor = new DbExecutorImpl<>(connection);
            executor.insertUpdateRecord(sql, params);
            connection.commit();
            System.out.println("updated user(" + idFld.get(objectData)+"):"+objectData);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public <T> void createOrUpdate(T objectData) {
        try {
            Class clazz = objectData.getClass();
            SqlScripture scriptor ;

            if(classesMetaData.containsKey(clazz))
                scriptor = classesMetaData.get(clazz);
            else {
                System.out.println("------------- New class:"+clazz);
                scriptor = new SqlScripture(clazz);
                classesMetaData.put(clazz,scriptor);
            }

            //убрал костыль с Count. всё намного проще оказалось. Деформация PL/SQL разраба ))
            if(load( (long)scriptor.getIdFld().get(objectData), clazz) == null)
                    create(objectData);
                else update(objectData);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }


}
