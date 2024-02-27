package ru.job4j.dreamjob.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2oException;
import ru.job4j.dreamjob.configuration.DatasourceConfiguration;
import ru.job4j.dreamjob.model.User;

import java.io.IOException;
import java.util.Properties;

import static java.time.LocalDateTime.now;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;



    @BeforeAll
    public static void initRepositories() throws Exception {
        var properties = new Properties();
        try (var inputStream = Sql2oVacancyRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        var url = properties.getProperty("datasource.url");
        var username = properties.getProperty("datasource.username");
        var password = properties.getProperty("datasource.password");

        var configuration = new DatasourceConfiguration();
        var datasource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(datasource);

        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearUsers() {
        var users = sql2oUserRepository.findAll();
        for (var user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }



    @Test
    public void whenSaveAndFindByEmail() {
        var user = sql2oUserRepository.save(new User(0, "123@mail.ru", "Ivanov Ivan", "123")).get();
        var savedUser = sql2oUserRepository.findUserByEmail("123@mail.ru").get();
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void whenSaveAndFindByEmailAndPassword() {
        var user = sql2oUserRepository.save(new User(1, "ivanov@mail.ru", "Jonson John", "jonson123")).get();
        var savedUser = sql2oUserRepository.findByEmailAndPassword("ivanov@mail.ru", "jonson123").get();
        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }


    @Test
    public void whenSaveAndFindByEmailMoreThenOne() {
        var user1 = sql2oUserRepository.save(new User(2, "pascal@mail.ru", "Pedro Pascal", "123pascal")).get();
        var user2 = sql2oUserRepository.save(new User(3, "456@mail.ru", "Petrov Petr", "petro")).get();
        var savedUser1 = sql2oUserRepository.findUserByEmail("pascal@mail.ru").get();
        var savedUser2 = sql2oUserRepository.findUserByEmail("456@mail.ru").get();
        assertThat(savedUser1).usingRecursiveComparison().isEqualTo(user1);
        assertThat(savedUser2).usingRecursiveComparison().isEqualTo(user2);
    }

    @Test
    public void whenSaveAndFindByEmailAndPasswordMoreThenOne() {
        var user1 = sql2oUserRepository.save(new User(4, "igorogor@mail.ru", "Igor Jibrailov", "123igor")).get();
        var user2 = sql2oUserRepository.save(new User(5, "petryan@mail.ru", "Petr Yan", "yanyan")).get();
        var savedUser1 = sql2oUserRepository.findByEmailAndPassword("igorogor@mail.ru", "123igor").get();
        var savedUser2 = sql2oUserRepository.findByEmailAndPassword("petryan@mail.ru", "yanyan").get();
        assertThat(savedUser1).usingRecursiveComparison().isEqualTo(user1);
        assertThat(savedUser2).usingRecursiveComparison().isEqualTo(user2);
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(sql2oUserRepository.findUserByEmail("nothing@mail.ru")).isEqualTo(empty());
        assertThat(sql2oUserRepository.findByEmailAndPassword("nothing2@mail.ru", "nothing"))
                .isEqualTo(empty());
    }

    @Test
    public void whenSaveExistEmail() {
        sql2oUserRepository.save(new User(6, "exist@mail.ru", "Serj Tankjan", "123")).get();
        assertThat(sql2oUserRepository.save(new User(6, "exist@mail.ru", "Serj Tankjan", "123")))
                .isEqualTo(empty());
    }
}