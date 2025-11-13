package br.com.cinema.cinemasystem.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Ingressos ingresso;

    private LocalDateTime dataCompra;

    private double valorTotal;

    public Ticket() {}

    public Ticket(Cliente cliente, Ingressos ingresso, LocalDateTime dataCompra, double valorTotal) {
        this.cliente = cliente;
        this.ingresso = ingresso;
        this.dataCompra = dataCompra;
        this.valorTotal = valorTotal;
    }

    // Getters e setters
    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
  
    public Ingressos getIngresso() { return ingresso; }
    public void setIngresso(Ingressos ingresso) { this.ingresso = ingresso; }
  
    public LocalDateTime getDataCompra() { return dataCompra; }
  
    public void setDataCompra(LocalDateTime dataCompra) { this.dataCompra = dataCompra; }
    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }
}
