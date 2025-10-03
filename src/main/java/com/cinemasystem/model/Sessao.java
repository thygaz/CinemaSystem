package com.cinemasystem.model;

import java.time.LocalDateTime;

public class Sessao {

    private long id;
    private Filme filme;
    private LocalDateTime horaSessao;
    private Assento assentos;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Filme getFilme() {
        return filme;
    }

    public void setFilme(Filme filme) {
        this.filme = filme;
    }

    public LocalDateTime getHoraSessao() {
        return horaSessao;
    }

    public void setHoraSessao(LocalDateTime horaSessao) {
        this.horaSessao = horaSessao;
    }

//    public int getAssentos() {
//        return assentos;
//    }
//
//    public void setAssentos(int assentos) {
//        this.assentos = assentos;
//    }
}
