package com.example.projeto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class MercadoDeTransferencia implements Serializable {
    private int id;
    private String jogador;
    private String clubeOrigem;
    private String clubeDestino;
    private double valor;
    private LocalDate dataTransferencia;
    private StatusTransferencia status;

    public enum StatusTransferencia {
        NEGOCIACAO, CONCLUIDA, CANCELADA
    }

    // Construtor
    public MercadoDeTransferencia(int id, String jogador, String origem, String destino, double valor, LocalDate data) {
        this.id = id;
        this.jogador = jogador;
        this.clubeOrigem = origem;
        this.clubeDestino = destino;
        this.valor = valor;
        this.dataTransferencia = data;
        this.status = StatusTransferencia.NEGOCIACAO;
    }

    // Getters
    public String getJogador() { return jogador; }
    public String getClubeOrigem() { return clubeOrigem; }
    public String getClubeDestino() { return clubeDestino; }
    public int getId() { return id; }
    public double getValor() { return valor; }
    public LocalDate getDataTransferencia() { return dataTransferencia; }
    public StatusTransferencia getStatus() { return status; }

    // Setter
    public void setStatus(StatusTransferencia status) { this.status = status; }

    // Formatação do valor
    public String getValorFormatado() {
        return String.format("€%.2fM", valor);
    }

    // Validação da transferência
    public boolean validarTransferencia() {
        return !clubeOrigem.equals(clubeDestino) && valor > 0;
    }

    // Métodos equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MercadoDeTransferencia)) return false;
        MercadoDeTransferencia that = (MercadoDeTransferencia) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
