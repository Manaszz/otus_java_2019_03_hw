package ru.otus.dbservice;

import ru.otus.dao.Id;
import ru.otus.dao.User;
import ru.otus.executor.DbExecutor;
import ru.otus.executor.DbExecutorImpl;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class DbServiceJdbc implements DBService {

    private final DataSource dataSource;
    public DbServiceJdbc(DataSource dataSource)  {
        this.dataSource = dataSource;
    }

    @Override
    public <T> void create(T objectData) throws IllegalAccessException {

        final Class  clazz = objectData.getClass();
        Field idFld = checkId(clazz);
        SqlScripture scriptor = new SqlScripture(clazz);
        List<String> params =new ArrayList<>();

        for (Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            params.add(String.valueOf( f.get(objectData)));
            f.setAccessible(false);
        }

        if(idFld == null){
            System.out.println("ID not found"); return;
        }else System.out.println("ID field:"+idFld.getName() );

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
            SqlScripture scriptor = new SqlScripture(clazz);
            String sql = scriptor.getSelectQuery();
            Optional<T> record = executor.selectRecord(sql, id,
                    resultSet -> {
                        try {
                            if (resultSet.next()) {
                               T newObj = clazz.newInstance();
                                for (Field field :clazz.getDeclaredFields()){
                                             setFieldValue(newObj,field.getName(),resultSet.getObject(field.getName()));
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
        Field idFld = checkId(clazz);
        SqlScripture scriptor = new SqlScripture(clazz);
        List<String> params =new ArrayList<>();
        try {
            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);
                params.add(String.valueOf( f.get(objectData)));
                f.setAccessible(false);
            }

            if(idFld == null){
                System.out.println("ID not found"); return;
            }else {
                System.out.println("ID field:" + idFld.getName());
                params.add(String.valueOf( idFld.get(objectData)));
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
        try (Connection connection = dataSource.getConnection()) {
            Class clazz = objectData.getClass();
            DbExecutor<T> executor = new DbExecutorImpl<>(connection);
            SqlScripture scriptor = new SqlScripture(clazz);
            String sql = scriptor.getCountQuery();

            //да, я знаю, очень кривая конструкция и можно было сделать подругому. Сделал костылем исключительно для экономии времени
            int count = ((DbExecutorImpl<T>) executor).selectCount( sql, (long)checkId(clazz).get(objectData));
            System.out.println("Count:"+ count);
            if(count>0) update(objectData); else create(objectData);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
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

    private  void setFieldValue(Object object, String name, Object value) {
        Field field = null;
        boolean isAccessible = true;
        try {
            field = object.getClass().getDeclaredField(name); //getField() for public fields
            isAccessible = field.canAccess(object);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (field != null && !isAccessible) {
                field.setAccessible(false);
            }
        }
    }

      class SqlScripture {
        private String tableName;
        private String fldNamesStr ="";
        private List<String> fldNamesList = new ArrayList<>();
        private String parmsDummy="";
        private String idFld;

        public  <T> SqlScripture(Class  clazz) {

            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);
                this.fldNamesStr += ("," + f.getName());
                this.parmsDummy+=",?";
                fldNamesList.add(f.getName());
                f.setAccessible(false);
            }
            fldNamesStr = fldNamesStr.substring(1);
            parmsDummy = parmsDummy.substring(1);
            tableName = clazz.getSimpleName();
            idFld =checkId(clazz).getName();
        }

         String getInsertQuery(){
            return "insert into "+ tableName+"("+ fldNamesStr +") values ("+parmsDummy+")";
        }
        String getSelectQuery(){
            return "select "+ fldNamesStr +" from "+ tableName+" where id  = ?";
          }
        String getUpdateQuery(){
            String sql ="update "+tableName+" set ";
            for (String f : fldNamesList) {
                sql = sql.concat(f + "= ?,");
               }
            sql =sql.substring(0,sql.length()-1)+" where "+idFld+" = ?" ;
            return sql;
          }
        String getCountQuery(){
            return "select count(*) from "+ tableName +"  where "+idFld+"  = ?";
        }
    }


}
