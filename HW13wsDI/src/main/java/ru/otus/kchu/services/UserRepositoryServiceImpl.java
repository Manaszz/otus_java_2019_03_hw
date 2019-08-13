package ru.otus.kchu.services;

import org.springframework.stereotype.Service;
import ru.otus.kchu.dao.User;
import ru.otus.kchu.repository.dbrepository.DBRepository;

import java.util.List;

@Service
public class UserRepositoryServiceImpl implements UserRepositoryService {
    private final DBRepository dbRepository;

    public UserRepositoryServiceImpl(DBRepository dbRepository) {
        this.dbRepository = dbRepository;
    }

    @Override
    public List<User> findAll()
    {

        return  dbRepository.GetEntities(User.class);
    }

    @Override
    public long create(String name, String pass,String role) {

        if (name == null || pass == null || (!role.equals("user")&& !role.equals("admin"))) {
            return 0;
        }
        User newUsr = new User(name,pass, role);
        try {
            dbRepository.create(newUsr);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println("new user id:" +newUsr.getId());
        return newUsr.getId();

    }
}
