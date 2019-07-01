package ru.otus.dbservice;

import ru.otus.dao.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class DBServiceHW {
    public static void main(String[] args) throws SQLException, IllegalAccessException {
        DataSource dataSource = new DataSourceH2();
        createTable(dataSource);

        DBService dbServiceUser = new DbServiceJdbc(dataSource);
        User usr1 = new User(10,"User10", 30);
        dbServiceUser.create(usr1);
        dbServiceUser.create(new User(11,"User11", 30));

        //dbServiceUser.getUser(usr1.getId());
        usr1.setAge(35);
        dbServiceUser.update(usr1);

        User usr2 = dbServiceUser.load(usr1.getId(),usr1.getClass());
        System.out.println("Loaded:" +usr2);

        usr1.setAge(40);
        dbServiceUser.createOrUpdate(usr1);
        System.out.println("Load:"+dbServiceUser.load(usr1.getId(),usr1.getClass()));

        dbServiceUser.createOrUpdate(new User(2,"User2", 10));
        System.out.println("Load:"+dbServiceUser.load(2,usr1.getClass()));


    }

    private static void createTable(DataSource dataSource) throws SQLException {
        String usrTableCreate ="create table user(id bigint(20) NOT NULL auto_increment, name varchar(50), age int(3))";
        String accTableCreate ="create table Account(no bigint(20) NOT NULL auto_increment, type varchar(225), rest number)";
        String[] tablesSQL ={usrTableCreate,accTableCreate};
        for (String tableSQL:
             tablesSQL) {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement pst = connection.prepareStatement(tableSQL)
            ) {
                pst.executeUpdate();
            }

            System.out.println("table created");
        }

    }
}
