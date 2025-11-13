package br.com.cinema.cinemasystem.controller;

import br.com.cinema.cinemasystem.model.Ingressos;
import br.com.cinema.cinemasystem.service.IngressosService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ingressos")
public class IngressosController {

    private final IngressosService ingressosService;

    public IngressosController(IngressosService ingressosService) {
        this.ingressosService = ingressosService;
    }

    @PostMapping
    public Ingressos criarIngresso(@RequestBody Ingressos ingresso) {
        return ingressosService.criarIngresso(ingresso);
    }

    @GetMapping
    public List<Ingressos> listarTodos() {
        return ingressosService.listarTodos();
    }

    @GetMapping("/{codigo}")
    public Optional<Ingressos> buscarPorCodigo(@PathVariable String codigo) {
        return ingressosService.buscarPorCodigo(codigo);
    }

    @PostMapping("/{codigo}/cancelar")
    public boolean cancelar(@PathVariable String codigo) {
        return ingressosService.cancelarIngresso(codigo);
    }

    @PostMapping("/{codigo}/reembolsar")
    public boolean reembolsar(@PathVariable String codigo) {
        return ingressosService.reembolsarIngresso(codigo);
    }

    @DeleteMapping("/{codigo}")
    public boolean remover(@PathVariable String codigo) {
        return ingressosService.removerIngresso(codigo);
    }
}
