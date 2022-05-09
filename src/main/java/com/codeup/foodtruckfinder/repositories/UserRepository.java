package com.codeup.foodtruckfinder.repositories;

import com.codeup.foodtruckfinder.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
User findByUsername(String username);
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_favorites u WHERE u.user_id = ?1 AND u.truck_id = ?2", nativeQuery = true)
    void deleteFavorite(long userId, Long truckId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_favorites u WHERE u.user_id = ?", nativeQuery = true)
    void deleteUserFavorite(long userId);



}
