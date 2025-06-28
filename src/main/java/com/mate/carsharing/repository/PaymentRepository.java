package com.mate.carsharing.repository;

import com.mate.carsharing.model.Payment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findBySessionId(String sessionId);

    @Query("SELECT p FROM Payment p WHERE p.rental.user.id = :userId")
    List<Payment> findPaymentsByUserId(@Param("userId") Long userId);

}
