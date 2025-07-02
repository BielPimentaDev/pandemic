package com.example.pandemic.domínio.utils;

import java.util.List;
import com.example.pandemic.domínio.entidades.*;
import com.example.pandemic.domínio.enums.Cor;

public class GameRenderer {
    
    public static String renderGameState(Jogo jogo) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(renderHeader());
        sb.append(renderGameStatus(jogo));
        sb.append(renderPlayers(jogo));
        sb.append(DiseaseTableRenderer.renderDiseaseTable(jogo.getTabuleiro().getDoencas()));
        sb.append(CityRenderer.renderCitiesGrid(jogo.getTabuleiro().getCidades(), jogo.getTurnManager().getJogadores()));
        sb.append(renderFooter());
        
        return sb.toString();
    }
    
    private static String renderHeader() {
        return "\n" + Cor.textoEmBranco("=== ESTADO DO JOGO PANDEMIC ===") + "\n";
    }
    
    private static String renderGameStatus(Jogo jogo) {
        StringBuilder sb = new StringBuilder();
        sb.append("[JOGO] ").append(Cor.textoEmBranco("STATUS DO JOGO:")).append("\n");
        sb.append("   ├─ Jogo Ativo: ").append(jogo.isJogoAtivo() ? "[OK] SIM" : "[X] NÃO").append("\n");
        sb.append("   ├─ Turno: ").append(jogo.getTurnManager().getTurnoAtual()).append("\n");
        sb.append("   ├─ Jogador Atual: ").append(jogo.getJogadorAtual().getNome()).append("\n");
        sb.append("   ├─ Vitória: ").append(jogo.isJogoVitoria() ? "[VITORIA] SIM" : "[X] NÃO").append("\n");
        sb.append("   └─ Ações restantes: ").append(jogo.getTurnManager().getAcoesRestantes()).append("\n");
        return sb.toString();
    }
    
    private static String renderPlayers(Jogo jogo) {
        StringBuilder sb = new StringBuilder();
        TurnManager turnManager = jogo.getTurnManager();
        List<Jogador> jogadores = turnManager.getJogadores();
        
        sb.append("\n[JOGADORES] ").append(Cor.textoEmBranco("JOGADORES (")).append(jogadores.size()).append(Cor.textoEmBranco("):")).append("\n");
        
        for (int i = 0; i < jogadores.size(); i++) {
            Jogador jogador = jogadores.get(i);
            boolean isCurrentPlayer = jogador.equals(jogo.getJogadorAtual());
            String indicator = isCurrentPlayer ? "[ATUAL]" : "      ";
            
            sb.append("   ").append(indicator).append(" Jogador ").append(i + 1).append(":\n");
            sb.append(PlayerRenderer.renderPlayer(jogador));
        }
        
        return sb.toString();
    }
    
    private static String renderFooter() {
        return "\n" + Cor.textoEmBranco("=================================");
    }
}
