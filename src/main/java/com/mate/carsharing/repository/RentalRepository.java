package com.mate.carsharing.repository;

import com.mate.carsharing.model.Rental;
import com.mate.carsharing.model.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    Optional<Rental> findByIdAndUserId(Long id, Long userId);

    Page<Rental> findByUserAndIsActive(User user, Boolean isActive, Pageable pageable);

    @Query("""
                SELECT r FROM Rental r
                WHERE r.returnDate <= :tomorrow
                AND r.isActive = true
            """)
    List<Rental> findOverdueRentals(@Param("tomorrow") LocalDate tomorrow);
}
