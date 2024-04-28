package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
public interface FollowersRepository extends CrudRepository<User, Integer> {
}
