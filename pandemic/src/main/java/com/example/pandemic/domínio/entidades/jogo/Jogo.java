package com.example.pandemic.domínio.entidades.jogo;

import java.util.List;

import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.Tabuleiro;
import com.example.pandemic.domínio.entidades.cartas.Embaralhador;
import com.example.pandemic.domínio.utils.GameRenderer;

import lombok.Data;

@Data
public class Jogo {
    private final Tabuleiro tabuleiro;
    private final Embaralhador embaralhador;
    private final GerenciadorDeTurno turnManager;
    private final GerenciadorDoEstadoDoJogo gameStateManager;
    
    private boolean jogoAtivo;
    private boolean jogoVitoria;
    private String nome;

    public Jogo(Tabuleiro tabuleiro, List<Jogador> jogadores, Embaralhador embaralhador) {
        this.tabuleiro = tabuleiro;
        this.embaralhador = embaralhador;
        this.turnManager = new GerenciadorDeTurno(jogadores);
        this.gameStateManager = new GerenciadorDoEstadoDoJogo(tabuleiro, embaralhador);
        
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

    public int getTurnoAtual() {
        return turnManager.getTurnoAtual();
    }
    
    public int getAcoesRestantes() {
        return turnManager.getAcoesRestantes();
    }
    
    public boolean temAcoesRestantes() {
        return turnManager.temAcoesRestantes();
    }
    
    public void consumirAcao() {
        turnManager.consumirAcao();
    }
    
    public void resetarAcoes() {
        turnManager.resetarAcoes();
    }
    
    public List<Jogador> getJogadores() {
        return turnManager.getJogadores();
    }
    
    // Métodos encapsulados do GerenciadorDoEstadoDoJogo
    public boolean verificarVitoria() {
        return gameStateManager.verificarVitoria();
    }
    
    public boolean verificarDerrota() {
        return gameStateManager.verificarDerrota();
    }
    
    public boolean verificarDerrotaPorFaltaDeCartas() {
        return gameStateManager.verificarDerrotaPorFaltaDeCartas();
    }
    
    public boolean verificarDerrotaPorInfeccao() {
        return gameStateManager.verificarDerrotaPorInfeccao();
    }
    
    public String getMessagemVitoria() {
        return gameStateManager.getMessagemVitoria();
    }
    
    public String getMessagemDerrota() {
        return gameStateManager.getMessagemDerrota();
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
