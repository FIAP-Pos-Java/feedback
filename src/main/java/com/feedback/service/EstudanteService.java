package com.feedback.service;


import com.feedback.dto.AtualizarPessoaDTO;
import com.feedback.dto.SalvarPessoaDTO;
import com.feedback.model.Estudante;
import com.feedback.repository.EstudanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EstudanteService {

    @Autowired
    private EstudanteRepository estudanteRepository;

    public List<Estudante> buscarTodosEstudantes() {
        return this.estudanteRepository.findAll();
    }

    public void salvarEstudante(SalvarPessoaDTO dto) {
        Estudante estudante = new Estudante();
        estudante.setNome(dto.nome());
        estudante.setEndereco(dto.endereco());
        estudante.setEmail(dto.email());

        this.estudanteRepository.save(estudante);
    }

    public void atualizarEstudante(UUID id, AtualizarPessoaDTO dto) {
        Estudante estudante = this.estudanteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Estudante não encontrado"));

        estudante.setNome(dto.nome());
        estudante.setEndereco(dto.endereco());
        estudante.setEmail(dto.email());

        this.estudanteRepository.save(estudante);
    }

    public void deletarEstudante(UUID id) {
        if(!this.estudanteRepository.existsById(id)) {
            throw new IllegalArgumentException("Estudante não encontrado");
        }
        this.estudanteRepository.deleteById(id);
    }
}
