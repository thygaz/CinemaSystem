package br.com.cinema.cinemasystem.controller;

import br.com.cinema.cinemasystem.model.Cliente;
import br.com.cinema.cinemasystem.service.ClienteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public Cliente criarCliente(@RequestBody Cliente cliente) {
        return clienteService.criarCliente(cliente);
    }

    @GetMapping
    public List<Cliente> listarTodos() {
        return clienteService.listarTodos();
    }

    @GetMapping("/{nome}")
    public Optional<Cliente> buscarPorNome(@PathVariable String nome) {
        return clienteService.buscarPorNome(nome);
    }
}
