package com.example.pandemic.domínio.entidades.cartas;

import lombok.Data;
import com.example.pandemic.domínio.enums.Cor;

@Data
public class CartaCidade implements Carta {
    private String nome;
    private Cor cor;

    public CartaCidade(String nome) {
        this.nome = nome;
        this.cor = determinarCorPorNome(nome);
    }

    public CartaCidade(String nome, Cor cor) {
        this.nome = nome;
        this.cor = cor;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String getDescricao() {
        return "Carta de cidade";
    }

    @Override
    public String getTipo() {
        return "Cidade";
    }

    @Override
    public Cor getCor() {
        return cor;
    }

    /**
     * Determina a cor baseada no nome da cidade (para compatibilidade)
     */
    private Cor determinarCorPorNome(String nome) {
        int hash = nome.hashCode();
        Cor[] cores = Cor.values();
        return cores[Math.abs(hash) % cores.length];
    }

    @Override
    public String toString() {
        return cor.colorir(cor.getEmoji() + " " + nome + " (" + getTipo() + ")");
    }
}

