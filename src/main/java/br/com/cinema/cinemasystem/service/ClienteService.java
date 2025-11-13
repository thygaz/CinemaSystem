package br.com.cinema.cinemasystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.cinema.cinemasystem.model.Cliente;
import br.com.cinema.cinemasystem.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente criarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNome(nome);
    }
}
