package com.codeup.foodtruckfinder.repositories;

import com.codeup.foodtruckfinder.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
User findByUsername(String username);
}
