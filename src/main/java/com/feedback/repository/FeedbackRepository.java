package com.feedback.repository;

import com.feedback.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, String> {

    @Query("SELECT f FROM Feedback f WHERE f.dataCriacao >= :dataLimite")
    List<Feedback> buscarPorPeriodo(@Param("dataLimite") LocalDateTime dataLimite);

    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.critico = true")
    long contarCriticos();
}
