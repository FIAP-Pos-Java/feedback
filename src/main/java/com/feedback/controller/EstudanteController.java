package com.feedback.controller;

import com.feedback.dto.AtualizarPessoaDTO;
import com.feedback.dto.SalvarPessoaDTO;
import com.feedback.model.Estudante;
import com.feedback.service.EstudanteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("estudantes")
public class EstudanteController {

    @Autowired
    private EstudanteService estudanteService;

    @PostMapping
    public ResponseEntity<Void> salvarEstudante(
            @Valid
            @RequestBody SalvarPessoaDTO dto
    ) {
        this.estudanteService.salvarEstudante(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarEstudante(
            @PathVariable UUID id,
            @Valid
            @RequestBody AtualizarPessoaDTO dto
    ) {
        this.estudanteService.atualizarEstudante(id, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEstudante(
            @PathVariable UUID id
    ) {
        this.estudanteService.deletarEstudante(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<Estudante>> buscarTodosEstudantes() {
        List<Estudante> estudantes = this.estudanteService.buscarTodosEstudantes();
        return ResponseEntity.ok(estudantes);
    }

}
