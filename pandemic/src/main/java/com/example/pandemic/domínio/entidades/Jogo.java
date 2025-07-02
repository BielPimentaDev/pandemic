package com.example.pandemic.domínio.entidades;

import java.util.List;

import com.example.pandemic.domínio.entidades.cartas.Embaralhador;
import com.example.pandemic.domínio.utils.GameRenderer;

import lombok.Data;

@Data
public class Jogo {
    private final Tabuleiro tabuleiro;
    private final Embaralhador embaralhador;
    private final TurnManager turnManager;
    private final GameStateManager gameStateManager;
    
    private boolean jogoAtivo;
    private boolean jogoVitoria;
    private String nome;

    public Jogo(Tabuleiro tabuleiro, List<Jogador> jogadores, Embaralhador embaralhador) {
        this.tabuleiro = tabuleiro;
        this.embaralhador = embaralhador;
        this.turnManager = new TurnManager(jogadores);
        this.gameStateManager = new GameStateManager(tabuleiro, embaralhador);
        
        this.jogoAtivo = true;
        this.jogoVitoria = false;
    }
    
    public boolean darCartaAoJogador(Jogador jogador) {
        if (embaralhador != null) {
            return embaralhador.darCartaAoJogador(jogador);
        }
        return false;
    }

    public Jogador getJogadorAtual() {
        return turnManager.getJogadorAtual();
    }

    public void proximoTurno() {
        if (jogoAtivo) {
            tabuleiro.espalharInfeccao();
            turnManager.resetarAcoes();
            darCartaAoJogador(getJogadorAtual());
            turnManager.proximoTurno();
            verificarEstadoDoJogo();
        }
    }

    private void verificarEstadoDoJogo() {
        if (gameStateManager.verificarVitoria()) {
            jogoVitoria = true;
            jogoAtivo = false;
            System.out.println(gameStateManager.getMessagemVitoria());
        } else if (gameStateManager.verificarDerrota()) {
            jogoAtivo = false;
            System.out.println(gameStateManager.getMessagemDerrota());
        }
    }

    @Override
    public String toString() {
        return GameRenderer.renderGameState(this);
    }
}
