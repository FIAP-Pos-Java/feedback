package com.feedback.repository;

import com.feedback.model.Estudante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EstudanteRepository extends JpaRepository<Estudante, UUID> {
}
