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
            + "WHERE p.id = :paymentId "
            + "AND p.rental.user.id = :userId")
    Optional<Payment> findByIdAndUserId(@Param("paymentId") Long paymentId,
                                        @Param("userId") Long userId);

    List<Payment> findByStatus(Payment.PaymentStatus paymentStatus);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END "
            + "FROM Payment p "
            + "WHERE p.rental.user.id = :userId AND p.status IN :statuses")
    boolean existsByUserIdAndStatusIn(@Param("userId") Long userId,
                                      @Param("statuses") List<Payment.PaymentStatus> statuses);

}
