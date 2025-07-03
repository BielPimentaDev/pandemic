package com.example.pandemic.infrastructure;

import com.example.pandemic.domínio.entidades.jogo.Jogo;
import com.example.pandemic.infrastructure.controllers.GameController;
import com.example.pandemic.infrastructure.setup.GameSetup;
import com.example.pandemic.infrastructure.ui.GameUI;

/**
 * Classe principal do jogo Pandemic
 * Ponto de entrada da aplicação
 */
public class Main {
    
    public static void main(String[] args) {
        try {
            // Exibir cabeçalho do jogo
            GameUI.exibirCabecalho();
            
            // Configurar e iniciar o jogo
            Jogo jogo = GameSetup.configurarEIniciarJogo();
            
            // Executar loop principal do jogo
            GameController.executarLoopPrincipal(jogo);
            
        } catch (Exception e) {
            GameUI.exibirMensagem("erro", "Erro durante a execucao do jogo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            GameUI.fecharScanner();
        }


       
    }
}
