package com.example.pandemic.aplicação.usecases;

import java.util.ArrayList;
import java.util.List;

import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.Jogo;
import com.example.pandemic.domínio.entidades.Tabuleiro;
import com.example.pandemic.domínio.entidades.cartas.CartaCidade;
import com.example.pandemic.domínio.entidades.cartas.Embaralhador;

public class IniciarJogo {
    private Tabuleiro tabuleiro;
    private List<Jogador> jogadores;
    public IniciarJogo(List<Jogador> jogadores, Tabuleiro tabuleiro) { 
        this.jogadores = jogadores;
        this.tabuleiro = tabuleiro; 
    }

   public Jogo executar() {

       if (jogadores.size() > 4) {
            throw new IllegalArgumentException("O jogo suporta no máximo 4 jogadores.");
        }
        inicializarPosicoesJogadores();    
        List<CartaCidade> cartas = criarCartasDasCidades(tabuleiro);
        Embaralhador embaralhador = new Embaralhador(cartas);
        distribuirCartasIniciais(embaralhador);

        return new Jogo(tabuleiro, jogadores, embaralhador);
    }

    private void inicializarPosicoesJogadores() {
        Cidade cidadeInicial = tabuleiro.getCidades().get(0);
        for (Jogador jogador : jogadores) {
            jogador.setPosicao(cidadeInicial);
        }
    }

     private List<CartaCidade> criarCartasDasCidades(Tabuleiro tabuleiro) {
        List<CartaCidade> cartas = new ArrayList<CartaCidade>();
        for (Cidade cidade : tabuleiro.getCidades()) {
            cartas.add(new CartaCidade(cidade.getNome()));
        }
        return cartas;
    }
    
    private void distribuirCartasIniciais(Embaralhador embaralhador) {
       
        int cartasPorJogador = 2;
        
        for (Jogador jogador : jogadores) {
            for (int i = 0; i < cartasPorJogador; i++) {
                embaralhador.darCartaAoJogador(jogador);
            }
        }
    }
    
    // private int calcularCartasIniciais() {
    //     switch (jogadores.size()) {
    //         case 2: return 4;
    //         case 3: return 3;
    //         case 4: return 2;
    //         default: return 2;
    //     }
    // }
}
