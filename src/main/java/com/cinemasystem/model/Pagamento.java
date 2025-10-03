package com.cinemasystem.model;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Pagamento {
    private String cartaoNumero;
    private String cartaoTitular;
    private YearMonth cartaoValidade;
    private String cartaoCVV;
    private double cartaoValor;


    // getters
    public String getCartaoNumero() { return this.cartaoNumero; }

    public String getCartaoTitular() { return this.cartaoTitular; }

    public YearMonth getCartaoValidade() { return this.cartaoValidade; }

    public String getCartaoCVV() {return this.cartaoCVV; }

    public double getCartaoValor() {return this.cartaoValor; }


    // setters
    public void setCartaoNumero(String cartaoNumero){
        if (cartaoNumero != null && cartaoNumero.length() == 16){
            this.cartaoNumero = cartaoNumero;
        } else {
            System.out.println("Número do cartão inválido!");
        }
    }

    public void setCartaoTitular(String cartaoTitular){
        this.cartaoTitular = cartaoTitular;
    }

    public void setCartaoCVV(String cartaoCVV) {
        this.cartaoCVV = cartaoCVV;
    }

    public void setCartaoValidade(String validadeStr) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");

        try{
            YearMonth validade = YearMonth.parse(validadeStr, formatter);

            if (validade.isBefore(YearMonth.now())){
                System.out.println("Erro. Cartão de Crédito expirado");
                this.cartaoValidade = null;
            }
            else {
                this.cartaoValidade = validade;
            }
        } catch (DateTimeParseException e){
            System.out.println("Erro: Formato da data de validade inválido. Use MM/yy.");
            this.cartaoValidade = null;
        }
        this.cartaoValidade = cartaoValidade;
    }

    public void setCartaoValor(double cartaoValor) {
        this.cartaoValor = cartaoValor;
    }
}



