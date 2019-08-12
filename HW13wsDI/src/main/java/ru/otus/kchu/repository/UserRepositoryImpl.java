package ru.otus.kchu.repository;

import org.springframework.stereotype.Repository;
import ru.otus.kchu.dao.User;
import ru.otus.kchu.services.dbservice.DBService;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final DBService dbService;

    public UserRepositoryImpl(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public List<User> findAll()
    {

        return  dbService.GetEntities(User.class);
    }

    @Override
    public long create(String name, String pass,String role) {

        if (name == null || pass == null || (!role.equals("user")&& !role.equals("admin"))) {
            return 0;
        }
        User newUsr = new User(name,pass, role);
        try {
            dbService.create(newUsr);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println("new user id:" +newUsr.getId());
        return newUsr.getId();

    }
}
