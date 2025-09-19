package com.cinemasystem.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Compra {
    private final long id;
    private final LocalDateTime dataDaCompra;
    private final double totalPago;
    // Adicionar List<Ingresso>

    // Construtor nova compra
    public Compra(long id, LocalDateTime dataDaCompra, double totalPago) {
        this.id = id;
        this.dataDaCompra = dataDaCompra;
        this.totalPago = totalPago;
    }

    // Getters
    public long getId() {
        return id;
    }

    public LocalDateTime getDataDaCompra() {
        return dataDaCompra;
    }

    public double getTotalPago() {
        return totalPago;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Compra [ID=" + id + ", Data=" + dataDaCompra.format(formatter) + ", Total=R$" + String.format("%.2f", totalPago) + "]";
    }
}