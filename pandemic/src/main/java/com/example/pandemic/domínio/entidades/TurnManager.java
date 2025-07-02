package com.example.pandemic.domínio.entidades;

import java.util.List;

public class TurnManager {
    private final List<Jogador> jogadores;
    private int turnoAtual;
    private int acoesRestantes;
    
    public TurnManager(List<Jogador> jogadores) {
        this.jogadores = jogadores;
        this.turnoAtual = 0;
        this.acoesRestantes = 4; // Padrão do Pandemic
    }
    
    public Jogador getJogadorAtual() {
        return jogadores.get(turnoAtual % jogadores.size());
    }
    
    public void proximoTurno() {
        turnoAtual++;
        acoesRestantes = 4;
    }
    
    public void consumirAcao() {
        if (acoesRestantes > 0) {
            acoesRestantes--;
        }
    }
    
    public boolean temAcoesRestantes() {
        return acoesRestantes > 0;
    }
    
    public void resetarAcoes() {
        acoesRestantes = 4;
    }
    
    // Getters
    public int getTurnoAtual() { 
        return turnoAtual; 
    }
    
    public int getAcoesRestantes() { 
        return acoesRestantes; 
    }
    
    public List<Jogador> getJogadores() {
        return jogadores;
    }
}
