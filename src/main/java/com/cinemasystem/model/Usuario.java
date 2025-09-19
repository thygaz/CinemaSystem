package com.cinemasystem.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nome;
    private String email;
    private String senha; // deveria ser hash
    private List<Compra> historicoDeCompras;

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.historicoDeCompras = new ArrayList<>();
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public List<Compra> getHistoricoDeCompras() {
        return historicoDeCompras;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    // Adicionar esse metodo na hora do pagamento @thiago!
    public void adicionarCompraAoHistorico(Compra compra) {
        this.historicoDeCompras.add(compra);
    }
}