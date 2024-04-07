package com.goodtrip.goodtripserver.database;

import com.goodtrip.goodtripserver.database.configs.DatabaseConfig;
import com.goodtrip.goodtripserver.database.models.User;
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@EnableJpaRepositories(basePackageClasses = AuthenticationRepository.class)
@ComponentScan(basePackages = {"com.goodtrip.goodtripserver.database.models.*"})
@EntityScan(basePackages = {"com.goodtrip.goodtripserver.database.models.*"})
@SpringBootTest(classes = {AuthenticationRepository.class, DatabaseConfig.class})
@EnableAutoConfiguration(exclude = {JdbcTemplateAutoConfiguration.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthenticationTests {
    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Test
    public void login_UserDoesNotExist_FailedLogin() {
        assertEquals(authenticationRepository.findUserByUsernameAndHashedPassword("a", "a"),
                Optional.empty());
        assertEquals(authenticationRepository.findUserByUsernameAndHashedPassword("a", "a"),
                Optional.empty());
        assertEquals(authenticationRepository.findUserByUsernameAndHashedPassword("ab", "ab"),
                Optional.empty());
    }

    @Test
    @org.springframework.transaction.annotation.Transactional
    public void authentication_UserDoesNotExist_UserLoggedIn() {
        authenticationRepository.deleteUserIfExistsByUsernameAndHashedPassword("a", "c");
        assertEquals(Optional.empty(), authenticationRepository.findUserByUsernameAndHashedPassword("a", "c"));
        authenticationRepository.save(new User("a", "b", "c", "e", "f", "g"));
        Optional<User> user = authenticationRepository.findUserByUsernameAndHashedPassword("a", "c");
        assertTrue(user.isPresent());
        Optional<String> salt = authenticationRepository.getSalt("a");
        if (salt.isEmpty()) {
            fail();
        }
        assertEquals("g", salt.get());

        authenticationRepository.deleteUserIfExistsByUsernameAndHashedPassword("a", "c");
        assertEquals(Optional.empty(), authenticationRepository.findUserByUsernameAndHashedPassword("a", "c"));
    }

    @Test
    @org.springframework.transaction.annotation.Transactional
    public void signUp_UserDoesNotExist_SignUpSeveralTimes() {
        authenticationRepository.deleteUserIfExistsByUsernameAndHashedPassword(
                "a", "c");
        authenticationRepository.save(new User("a", "b", "c", "e", "f", "g"));
        authenticationRepository.deleteUserIfExistsByUsernameAndHashedPassword(
                "a", "c");
        assertEquals(Optional.empty(),
                authenticationRepository.findUserByUsernameAndHashedPassword("a", "c"));
    }


}
