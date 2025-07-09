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

    @Query("SELECT p FROM Payment p "
            + "WHERE p.id = : paymentId "
            + "AND p.rental.user.id = :userId")
    Optional<Payment> findByIdAndUserId(@Param("paymentId") Long paymentId,
                                        @Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END "
            + "FROM Payment p "
            + "JOIN p.rental r "
            + "JOIN r.user u "
            + "WHERE u.id = :userId AND p.status = :paymentStatus")
    boolean existsByUserIdAndStatus(@Param("userId") Long userId,
                                    @Param("paymentStatus") Payment.PaymentStatus paymentStatus);

    List<Payment> findByStatus(Payment.PaymentStatus paymentStatus);
}
