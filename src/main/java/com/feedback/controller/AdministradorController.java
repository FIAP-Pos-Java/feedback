package com.feedback.controller;

import com.feedback.dto.AtualizarPessoaDTO;
import com.feedback.dto.SalvarPessoaDTO;
import com.feedback.model.Administrador;
import com.feedback.service.AdministradorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("administradores")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @PostMapping
    public ResponseEntity<Void> salvarAdministrador(
            @Valid
            @RequestBody SalvarPessoaDTO dto
    ) {
        this.administradorService.salvarAdministrador(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizarAdministrador(
            @PathVariable UUID id,
            @Valid
            @RequestBody AtualizarPessoaDTO dto
    ) {
        this.administradorService.atualizarAdministrador(id, dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAdministrador(
            @PathVariable UUID id
    ) {
        this.administradorService.deletarAdministrador(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<Administrador>> buscarTodosAdministradores() {
        List<Administrador> administrador = this.administradorService.buscarTodosAdministradores();
        return ResponseEntity.ok(administrador);
    }
}
