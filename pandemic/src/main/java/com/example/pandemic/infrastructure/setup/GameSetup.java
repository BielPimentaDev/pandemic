package com.example.pandemic.infrastructure.setup;

import java.util.ArrayList;
import java.util.List;

import com.example.pandemic.aplicação.usecases.IniciarJogo;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.Tabuleiro;
import com.example.pandemic.domínio.entidades.jogo.Jogo;
import com.example.pandemic.domínio.utils.builders.JogadorBuilder;
import com.example.pandemic.domínio.utils.builders.TabuleiroBuilder;
import com.example.pandemic.infrastructure.ui.GameUI;

/**
 * Classe responsável pela configuração inicial do jogo
 */
public class GameSetup {
    
    public static Jogo configurarEIniciarJogo() {
        // Configurar o jogo
        int numeroJogadores = GameUI.obterNumeroJogadores();
        List<Jogador> jogadores = criarJogadores(numeroJogadores);
        
        // Criar tabuleiro
        GameUI.exibirMensagem("info", "Gerando tabuleiro...");
        Tabuleiro tabuleiro = TabuleiroBuilder.umTabuleiro().build();
        
        // Iniciar jogo
        GameUI.exibirMensagem("info", "Iniciando jogo...");
        IniciarJogo iniciarJogo = new IniciarJogo(jogadores, tabuleiro);
        Jogo jogo = iniciarJogo.executar();
        
        GameUI.exibirMensagem("sucesso", "Jogo iniciado com sucesso!");
        GameUI.exibirMensagem("info", "Numero de jogadores: " + numeroJogadores);
        System.out.println();
        
        // Exibir estado inicial do jogo
        System.out.println(jogo);
        
        return jogo;
    }
    
    private static List<Jogador> criarJogadores(int numeroJogadores) {
        List<Jogador> jogadores = new ArrayList<>();
        
        System.out.println();
        System.out.println("[CONFIGURACAO] Configuracao dos jogadores:");
        System.out.println("---------------------------------------");
        
        for (int i = 1; i <= numeroJogadores; i++) {
            String nome = GameUI.obterNomeJogador(i);
            
            // Criar jogador usando o builder
            Jogador jogador = JogadorBuilder.umJogador()
                .comNome(nome)
                .build();
            
            jogadores.add(jogador);
            GameUI.exibirMensagem("sucesso", "Jogador '" + nome + "' adicionado!");
        }
        
        return jogadores;
    }
}
