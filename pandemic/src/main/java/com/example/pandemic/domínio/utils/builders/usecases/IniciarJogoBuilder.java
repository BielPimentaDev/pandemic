package com.example.pandemic.domínio.utils.builders.usecases;

import java.util.List;

import com.example.pandemic.aplicação.usecases.IniciarJogo;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.Jogo;
import com.example.pandemic.domínio.entidades.Tabuleiro;
import com.example.pandemic.domínio.utils.builders.JogadorBuilder;
import com.example.pandemic.domínio.utils.builders.TabuleiroBuilder;

public class IniciarJogoBuilder {

    Tabuleiro tabuleiro;
    List<Jogador> jogadores;
    public IniciarJogoBuilder() {    
       
        this.tabuleiro = TabuleiroBuilder.umTabuleiro().build();
        
        this.jogadores = List.of(
            JogadorBuilder.umJogador().build(),
            JogadorBuilder.umJogador().comNome("Lucas").build()
        );
    }

    public static IniciarJogoBuilder umIniciarJogo() {
        return new IniciarJogoBuilder();
   
    }

    public Jogo build() {
        IniciarJogo iniciarJogo = new IniciarJogo(this.jogadores, this.tabuleiro);
        return iniciarJogo.executar();
    }   
}
