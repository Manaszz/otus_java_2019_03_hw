package ru.otus.kchu.services;

import ru.otus.kchu.dao.User;
import java.util.List;

public interface UserRepositoryService {

    List<User> findAll();

    long create(String name, String pass,String role);
}
