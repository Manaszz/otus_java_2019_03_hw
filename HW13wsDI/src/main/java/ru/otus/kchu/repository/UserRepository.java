package ru.otus.kchu.repository;

import ru.otus.kchu.dao.User;
import java.util.List;

public interface UserRepository {

    List<User> findAll();

    long create(String name, String pass,String role);
}
