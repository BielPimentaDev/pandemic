package com.example.pandemic.domínio.entidades;

import com.example.pandemic.domínio.entidades.cartas.Embaralhador;

public class GameStateManager {
    private final Tabuleiro tabuleiro;
    private final Embaralhador embaralhador;
    
    public GameStateManager(Tabuleiro tabuleiro, Embaralhador embaralhador) {
        this.tabuleiro = tabuleiro;
        this.embaralhador = embaralhador;
    }
    
    public boolean verificarVitoria() {
        return tabuleiro.getDoencas().stream()
            .allMatch(Doenca::isCuraDescoberta);
    }
    
    public boolean verificarDerrota() {
        return verificarDerrotaPorFaltaDeCartas() || verificarDerrotaPorInfeccao();
    }
    
    public boolean verificarDerrotaPorFaltaDeCartas() {
        return embaralhador != null && !embaralhador.temCartas();
    }
    
    public boolean verificarDerrotaPorInfeccao() {
        long cidadesComIntensidadeAlta = tabuleiro.getCidades().stream()
            .filter(cidade -> cidade.getDoenca() != null)
            .filter(cidade -> cidade.getDoenca().getIntensidade() >= 3)
            .count();
            
        return cidadesComIntensidadeAlta == tabuleiro.getCidades().size();
    }
    
    public String getMessagemVitoria() {
        return "Parabéns! Você descobriu a cura para todas as doenças!";
    }
    
    public String getMessagemDerrota() {
        if (verificarDerrotaPorFaltaDeCartas()) {
            return "Fim de jogo! Você não tem mais cartas para jogar.";
        } else if (verificarDerrotaPorInfeccao()) {
            return "Fim de jogo! Todas as cidades estão infectadas com doenças.";
        }
        return "Fim de jogo!";
    }
}
