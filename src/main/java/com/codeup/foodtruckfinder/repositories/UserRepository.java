package com.codeup.foodtruckfinder.repositories;

import com.codeup.foodtruckfinder.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_favorites u WHERE u.user_id = ?1 AND u.truck_id = ?2", nativeQuery = true)
    void deleteFavorite(long userId, Long truckId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_favorites u WHERE u.user_id = ?", nativeQuery = true)
    void deleteUserFavorite(long userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_favorites u WHERE u.truck_id = ?", nativeQuery = true)
    void deleteTruckFavorite(long truckId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_confirmations u WHERE u.user_id = ?", nativeQuery = true)
    void deleteUserConfirmation(long userId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM user_confirmations u WHERE u.truck_id = ?", nativeQuery = true)
    void deleteTruckConfirmation(long truckId);

    boolean existsUserByUsername(String username);

    boolean existsUserByEmail(String email);
@Query(value = "SELECT * FROM users WHERE username LIKE CONCAT('%', ?1, '%')", nativeQuery = true)
    List<User> adminUserSearch(String username);
}