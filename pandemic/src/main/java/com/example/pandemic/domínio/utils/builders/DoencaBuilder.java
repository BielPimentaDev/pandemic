package com.example.pandemic.domínio.utils.builders;

import com.example.pandemic.domínio.entidades.Doenca;
import com.example.pandemic.domínio.enums.Cor;

public class DoencaBuilder {
    private String nome;
    private int intensidade;
    private Cor cor;

    public DoencaBuilder() {
        this.nome = "Doença Teste";
        this.intensidade = 0;
        this.cor = Cor.AZUL; // Cor padrão
    }

    public static DoencaBuilder umaDoenca() {
        return new DoencaBuilder();
    }

    public DoencaBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public DoencaBuilder comIntensidade(int intensidade) {
        this.intensidade = intensidade;
        return this;
    }

    public DoencaBuilder comCor(Cor cor) {
        this.cor = cor;
        return this;
    }

    // Métodos de conveniência para cores específicas
    public DoencaBuilder azul() {
        this.cor = Cor.AZUL;
        return this;
    }

    public DoencaBuilder amarela() {
        this.cor = Cor.AMARELO;
        return this;
    }

    public DoencaBuilder vermelha() {
        this.cor = Cor.VERMELHO;
        return this;
    }

    public DoencaBuilder preta() {
        this.cor = Cor.PRETO;
        return this;
    }

    public Doenca build() {
        Doenca doenca = new Doenca(nome, cor);
        doenca.setIntensidade(intensidade);
        return doenca;
    }
}
