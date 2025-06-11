package com.mate.carsharing.repository;

import com.mate.carsharing.model.Rental;
import com.mate.carsharing.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    Optional<Rental> findByIdAndUserId(Long id, Long userId);

    Page<Rental> findByUserAndIsActive(User user, boolean isActive, Pageable pageable);

}
