package com.example.pandemic.domínio.entidades;


import java.util.List;

import com.example.pandemic.domínio.acoes.Acao;
import com.example.pandemic.domínio.entidades.cartas.CartaCidade;
import com.example.pandemic.domínio.enums.Personagem;

import lombok.Data;


@Data
public class Jogador {
    private Personagem personagem;
    private List<CartaCidade> cartas;
    private Cidade posicao;
    private String nome;

    public Jogador(String nome, List<CartaCidade> cartas, Cidade posicao, Personagem personagem) {
        this.cartas = cartas;
        this.posicao = posicao;
        this.personagem = personagem;
        this.nome = nome;
    }
    public void executarAcao(Acao acao){
        acao.executar(this);
    }

    public void comprarCarta(CartaCidade carta) {
       
        cartas.add(carta);
    
    }

    public String getNome() {
            return nome;
        }   

    public List<CartaCidade> getCartas() {
        return cartas;
    }

    public void compraCarta(CartaCidade carta) {
        if (carta != null) {
            cartas.add(carta);
        }
    }

    public Cidade getPosicao() {
        return posicao;
    }

    public void setPosicao(Cidade posicao) {
        this.posicao = posicao;
    }

    public Personagem getPersonagem(){
        return this.personagem;
    }

    public void descartarUmaCarta(CartaCidade carta){
        
    }
}
