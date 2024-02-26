package ru.job4j.dreamjob.repository;

import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.model.Vacancy;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Optional<User> save(User user);

    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findUserByEmail(String email);

    Collection<User> findAll();

    boolean deleteById(int id);
}