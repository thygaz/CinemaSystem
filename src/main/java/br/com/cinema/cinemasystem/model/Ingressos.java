import model.Ingressos;

package model;

import jakarta.persistence.*;

/**
 * Entidade JPA de ingresso de cinema.
 */
@Entity
public class Ingressos {

    public enum Status {
        COMPRADO,
        CANCELADO
    }

    @Id
    private String codigo;

    private String data;
    private String tituloFilme;
    private String sala;
    private String horario;
    private int quantidade;

    @Enumerated(EnumType.STRING)
    private Status status;

    public Ingressos() {}

    public Ingressos(String codigo, String data, String tituloFilme, String sala, String horario, int quantidade, Status status) {
        this.codigo = codigo;
        this.data = data;
        this.tituloFilme = tituloFilme;
        this.sala = sala;
        this.horario = horario;
        this.quantidade = quantidade;
        this.status = status;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getTituloFilme() { return tituloFilme; }
    public void setTituloFilme(String tituloFilme) { this.tituloFilme = tituloFilme; }

    public String getSala() { return sala; }
    public void setSala(String sala) { this.sala = sala; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public void cancelar() {
        if (status == Status.COMPRADO) status = Status.CANCELADO;
    }

    public void reembolsar() {
        if (status == Status.COMPRADO) status = Status.CANCELADO;
    }
}