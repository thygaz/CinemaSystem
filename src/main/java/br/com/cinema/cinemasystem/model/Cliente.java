package model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade JPA de cliente.
 */
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingressos> ingressosComprados = new ArrayList<>();

    public Cliente() {}

    public Cliente(String nome) {
        this.nome = nome;
    }

    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public List<Ingressos> getIngressosComprados() { return ingressosComprados; }

    public void comprarIngresso(Ingressos ingresso) {
        ingressosComprados.add(ingresso);
    }

    public boolean cancelarIngresso(String codigo) {
        return ingressosComprados.stream()
                .filter(i -> i.getCodigo().equals(codigo))
                .findFirst()
                .map(i -> { i.cancelar(); return true; })
                .orElse(false);
    }

    public boolean reembolsarIngresso(String codigo) {
        return ingressosComprados.stream()
                .filter(i -> i.getCodigo().equals(codigo))
                .findFirst()
                .map(i -> { i.reembolsar(); return true; })
                .orElse(false);
    }
}