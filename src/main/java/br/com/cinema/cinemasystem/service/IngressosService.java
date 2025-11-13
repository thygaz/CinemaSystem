package br.com.cinema.cinemasystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.cinema.cinemasystem.model.Ingressos;
import br.com.cinema.cinemasystem.repository.IngressosRepository;

@Service
public class IngressosService {

    private final IngressosRepository ingressosRepository;

    public IngressosService(IngressosRepository ingressosRepository) {
        this.ingressosRepository = ingressosRepository;
    }

    public Ingressos criarIngresso(Ingressos ingresso) {
        return ingressosRepository.save(ingresso);
    }

    public List<Ingressos> listarTodos() {
        return ingressosRepository.findAll();
    }

    public Optional<Ingressos> buscarPorCodigo(String codigo) {
        return ingressosRepository.findById(codigo);
    }

    public boolean cancelarIngresso(String codigo) {
        Optional<Ingressos> ingresso = ingressosRepository.findById(codigo);
        ingresso.ifPresent(i -> { i.cancelar(); ingressosRepository.save(i); });
        return ingresso.isPresent();
    }

    public boolean reembolsarIngresso(String codigo) {
        Optional<Ingressos> ingresso = ingressosRepository.findById(codigo);
        ingresso.ifPresent(i -> { i.reembolsar(); ingressosRepository.save(i); });
        return ingresso.isPresent();
    }

    public boolean removerIngresso(String codigo) {
        if (ingressosRepository.existsById(codigo)) {
            ingressosRepository.deleteById(codigo);
            return true;
        }
        return false;
    }
}
