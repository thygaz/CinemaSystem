package br.com.cinema.cinemasystem.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tb_filme")
@Getter
@Setter
public class Filme {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID uuid;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    public Filme() {
    }

    public Filme(UUID uuid, String nome) {
        this.uuid = uuid;
        this.nome = nome;
    }
}
