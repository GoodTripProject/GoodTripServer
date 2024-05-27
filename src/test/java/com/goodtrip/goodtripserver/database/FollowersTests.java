package com.goodtrip.goodtripserver.database;

import com.goodtrip.goodtripserver.database.configs.DatabaseConfig;
import com.goodtrip.goodtripserver.database.models.User;
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository;
import com.goodtrip.goodtripserver.database.repositories.FollowersRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest(classes = DatabaseConfig.class)
@ComponentScan(basePackages = {"com.goodtrip.goodtripserver.database.models.*"})
@EntityScan(basePackages = {"com.goodtrip.goodtripserver.database.models.*"})
@EnableJpaRepositories("com.goodtrip.goodtripserver.database.repositories")
@EnableAutoConfiguration(exclude = {JdbcTemplateAutoConfiguration.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FollowersTests {
    @Autowired
    private FollowersRepository repository;
    @Autowired
    private AuthenticationRepository authenticationRepository;
    User user1;
    User user2;

    private User createUser() {
        String username = UUID.randomUUID().toString().substring(0, 31);
        String password = UUID.randomUUID().toString().substring(0, 31);
        return authenticationRepository.save(new User(username, UUID.randomUUID().toString().substring(0, 31),
                password, "a", "b", ""));
    }

    @BeforeAll
    public void setAuthenticationRepository() {
        user1 = createUser();
        user2 = createUser();
    }

    @Test
    @Transactional
    public void following_FollowingDoesNotExist_FollowingExist() {
        repository.followUserByHandles(user1.getId(), user2.getHandle());
        Assertions.assertTrue(repository.existsSubscription(user1.getId(), user2.getHandle()));
        repository.deleteSubscription(user1.getId(), user2.getHandle());
        Assertions.assertFalse(repository.existsSubscription(user1.getId(), user2.getHandle()));
    }

    @AfterAll
    public void deleteUsers() {
        authenticationRepository.delete(user1);
        authenticationRepository.delete(user2);
    }
}
