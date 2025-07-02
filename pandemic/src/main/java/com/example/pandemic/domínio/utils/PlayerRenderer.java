package com.example.pandemic.domínio.utils;

import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.cartas.CartaCidade;

public class PlayerRenderer {
    
    public static String renderPlayer(Jogador jogador) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("      ├─ Nome: ").append(jogador.getNome()).append("\n");
        sb.append("      ├─ Personagem: ").append(jogador.getPersonagem()).append("\n");
        sb.append("      ├─ Posição: ").append(jogador.getPosicao() != null ? jogador.getPosicao().getNome() : "Nenhuma").append("\n");
        sb.append("      ├─ Total de Cartas: ").append(jogador.getCartas().size()).append("\n");
        
        sb.append(renderPlayerCards(jogador));
        
        return sb.toString();
    }
    
    private static String renderPlayerCards(Jogador jogador) {
        StringBuilder sb = new StringBuilder();
        
        if (jogador.getCartas().isEmpty()) {
            sb.append("      └─ Cartas: Nenhuma carta\n");
        } else {
            sb.append("      └─ Cartas:\n");
            for (int i = 0; i < jogador.getCartas().size(); i++) {
                CartaCidade carta = jogador.getCartas().get(i);
                String indicator = (i == jogador.getCartas().size() - 1) ? "└─" : "├─";
                String cartaNomeColorida = carta.getCor() != null ? 
                    carta.getCor().colorir(carta.getNome()) + " " + carta.getCor().getVisualIndicator() : 
                    carta.getNome();
                sb.append("         ").append(indicator).append(" ").append(cartaNomeColorida).append("\n");
            }
        }
        
        return sb.toString();
    }
}
