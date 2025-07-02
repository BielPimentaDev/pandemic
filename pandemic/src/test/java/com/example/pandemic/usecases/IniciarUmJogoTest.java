package com.example.pandemic.usecases;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.pandemic.aplicação.usecases.IniciarJogo;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.Jogo;
import com.example.pandemic.domínio.entidades.Tabuleiro;
import com.example.pandemic.domínio.utils.builders.JogadorBuilder;
import com.example.pandemic.domínio.utils.builders.TabuleiroBuilder;

import static org.junit.jupiter.api.Assertions.*;

class IniciarUmJogoTest {
    @Test
    void deveIniciarUmJogoComSucesso() {
        Tabuleiro tabuleiro = TabuleiroBuilder.umTabuleiro().build();
        
        Jogador jogador1 = JogadorBuilder.umJogador().build();    
        Jogador jogador2 = JogadorBuilder.umJogador().comNome("Lucas").build();    

        IniciarJogo iniciarJogo = new IniciarJogo(List.of(jogador1, jogador2), tabuleiro);

        Jogo jogo = iniciarJogo.executar();
        System.out.println(jogo);
        assertTrue(jogo.isJogoAtivo());
    }
}
