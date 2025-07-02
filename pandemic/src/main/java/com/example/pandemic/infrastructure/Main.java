package com.example.pandemic.infrastructure;

import java.util.List;

import com.example.pandemic.aplicação.usecases.IniciarJogo;
import com.example.pandemic.aplicação.usecases.ProcessarTurno;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.Jogo;
import com.example.pandemic.domínio.entidades.Tabuleiro;
import com.example.pandemic.domínio.utils.builders.JogadorBuilder;
import com.example.pandemic.domínio.utils.builders.TabuleiroBuilder;
import com.example.pandemic.domínio.acoes.Acao;
import com.example.pandemic.domínio.acoes.ConstruirCentroDePesquisa;
import com.example.pandemic.domínio.acoes.TratarDoenca;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== PANDEMIC ===");
        
    
        
        System.out.println("\n" + "=".repeat(50));
        
        Tabuleiro tabuleiro = TabuleiroBuilder.umTabuleiro().build();
        
        Jogador jogador1 = JogadorBuilder.umJogador().build();    
        Jogador jogador2 = JogadorBuilder.umJogador().comNome("Lucas").build();    

        IniciarJogo iniciarJogo = new IniciarJogo(List.of(jogador1, jogador2), tabuleiro);

        Jogo jogo = iniciarJogo.executar();
        
        System.out.println(jogo);
        Acao acao = new TratarDoenca();
        Acao acao2 = new ConstruirCentroDePesquisa();
        ProcessarTurno processarTurno = new ProcessarTurno(jogo);
        System.out.println("====== TURNO ======");
        
        
    }
    
}
