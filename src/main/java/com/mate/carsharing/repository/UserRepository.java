package com.mate.carsharing.repository;

import com.mate.carsharing.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    /****
 * Retrieves a user by their email address.
 *
 * @param email the email address to search for
 * @return an Optional containing the user if found, or empty if not found
 */
Optional<User> findByEmail(String email);

}
