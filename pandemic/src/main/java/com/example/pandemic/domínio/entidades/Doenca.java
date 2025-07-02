package com.example.pandemic.domínio.entidades;

import lombok.Data;
import com.example.pandemic.domínio.enums.Cor;

@Data
public class Doenca {
    private String nome;
    private int intensidade;
    private Cor cor;
    boolean curaDescoberta;

    public Doenca(String nome) {
        this.nome = nome;
        this.intensidade = 0;
        this.cor = determinarCorPorNome(nome);
        this.curaDescoberta = false;
    }

    public Doenca(String nome, Cor cor) {
        this.nome = nome;
        this.intensidade = 0;
        this.cor = cor;
    }

    public void aumentarIntensidade() {
        this.intensidade++;
    }

    public String getNome() {
        return nome;
    }

    public int getIntensidade() {
        return intensidade;
    }

    public Cor getCor() {
        return cor;
    }

    public void setIntensidade(int intensidade){
        this.intensidade = intensidade;
    }

    public boolean isCuraDescoberta() {
        return curaDescoberta;
    }

    /**
     * Determina a cor baseada no nome da doença (para compatibilidade)
     */
    private Cor determinarCorPorNome(String nome) {
        int hash = nome.hashCode();
        Cor[] cores = Cor.values();
        return cores[Math.abs(hash) % cores.length];
    }

    @Override
    public String toString() {
        return cor.colorir(cor.getEmoji() + " " + nome + " (Intensidade: " + intensidade + ")");
    }
}
