package com.feedback.service;

import com.feedback.dto.AtualizarPessoaDTO;
import com.feedback.dto.SalvarPessoaDTO;
import com.feedback.model.Administrador;
import com.feedback.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    public List<Administrador> buscarTodosAdministradores() {
        return this.administradorRepository.findAll();
    }

    public void salvarAdministrador(SalvarPessoaDTO dto) {
        Administrador administrador = new Administrador();
        administrador.setNome(dto.nome());
        administrador.setEndereco(dto.endereco());
        administrador.setEmail(dto.email());
        this.administradorRepository.save(administrador);
    }

    public void atualizarAdministrador(UUID id, AtualizarPessoaDTO dto) {
        Administrador administrador = this.administradorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Administrador não encontrado"));

        administrador.setNome(dto.nome());
        administrador.setEndereco(dto.endereco());
        administrador.setEmail(dto.email());

        this.administradorRepository.save(administrador);
    }

    public void deletarAdministrador(UUID id) {
        if (!this.administradorRepository.existsById(id)) {
            throw new IllegalArgumentException("Administrador não encontrado");
        }
        this.administradorRepository.deleteById(id);
    }
}
