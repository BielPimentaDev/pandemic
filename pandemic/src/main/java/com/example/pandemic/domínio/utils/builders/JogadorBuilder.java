package com.example.pandemic.domínio.utils.builders;

import java.util.ArrayList;
import java.util.List;

import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.cartas.CartaCidade;
import com.example.pandemic.domínio.enums.Personagem;
import com.example.pandemic.domínio.enums.Cor;

public class JogadorBuilder {
    private Personagem personagem;
    private List<CartaCidade> cartas;
    private Cidade posicao;
    private String nome;

    public JogadorBuilder() {
        this.personagem = Personagem.CONSTRUTOR;
        this.nome = "Jhony";
        this.cartas = new ArrayList<>();
    }

    public JogadorBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }   

    public static JogadorBuilder umJogador() {
        return new JogadorBuilder();
    }

    public JogadorBuilder comPersonagem(Personagem personagem) {
        this.personagem = personagem;
        return this;
    }

    public JogadorBuilder comCartas(List<CartaCidade> cartas) {
        this.cartas = cartas;
        return this;
    }

    public JogadorBuilder comCarta(CartaCidade carta) {
        if (this.cartas == null) {
            this.cartas = new ArrayList<>();
        }
        this.cartas.add(carta);
        return this;
    }

    public JogadorBuilder comCartaCidade(String nomeCidade, Cor cor) {
        if (this.cartas == null) {
            this.cartas = new ArrayList<>();
        }
        this.cartas.add(new CartaCidade(nomeCidade, cor));
        return this;
    }

    public JogadorBuilder comCartaCidade(String nomeCidade) {
        if (this.cartas == null) {
            this.cartas = new ArrayList<>();
        }
        this.cartas.add(new CartaCidade(nomeCidade));
        return this;
    }

    public JogadorBuilder comPosicao(Cidade posicao) {
        this.posicao = posicao;
        return this;
    }

    public JogadorBuilder semCartas() {
        this.cartas = new ArrayList<>();
        return this;
    }

    public Jogador build() {
        if (cartas == null) {
            cartas = new ArrayList<>();
        }
        return new Jogador(nome, cartas, posicao, personagem);
    }
}
