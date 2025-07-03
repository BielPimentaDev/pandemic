package com.example.pandemic.usecases;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.pandemic.aplicação.usecases.IniciarJogo;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.Tabuleiro;
import com.example.pandemic.domínio.entidades.jogo.Jogo;
import com.example.pandemic.domínio.enums.Personagem;
import com.example.pandemic.domínio.utils.builders.JogadorBuilder;
import com.example.pandemic.domínio.utils.builders.TabuleiroBuilder;

import static org.junit.jupiter.api.Assertions.*;

class IniciarUmJogoTest {
    
    private Tabuleiro tabuleiro;
    private List<Jogador> jogadores;
    
    @BeforeEach
    void setUp() {
     
        tabuleiro = TabuleiroBuilder.umTabuleiro().build();
        jogadores = new ArrayList<>();
    }
    
    @Test
    void deveIniciarUmJogoComSucesso() {
        // Arrange
        Jogador jogador1 = JogadorBuilder.umJogador().build();    
        Jogador jogador2 = JogadorBuilder.umJogador().comNome("Lucas").build();
        jogadores.add(jogador1);
        jogadores.add(jogador2);

        IniciarJogo iniciarJogo = new IniciarJogo(jogadores, tabuleiro);

        // Act
        Jogo jogo = iniciarJogo.executar();

        // Assert
        assertNotNull(jogo, "O jogo deve ser criado");
        assertTrue(jogo.isJogoAtivo(), "O jogo deve estar ativo após inicialização");
        assertEquals(2, jogo.getJogadores().size(), "O jogo deve ter 2 jogadores");
        assertNotNull(jogo.getTabuleiro(), "O jogo deve ter um tabuleiro");
        assertNotNull(jogo.getJogadorAtual(), "Deve haver um jogador atual");
        assertEquals(0, jogo.getTurnoAtual(), "O turno inicial deve ser 0");
        assertTrue(jogo.getAcoesRestantes() > 0, "Deve haver ações restantes no início");
    }
    
    @Test
    void deveIniciarJogoComDoisJogadores() {
        // Arrange
        jogadores.add(JogadorBuilder.umJogador().comNome("João").build());
        jogadores.add(JogadorBuilder.umJogador().comNome("Maria").build());
        
        IniciarJogo iniciarJogo = new IniciarJogo(jogadores, tabuleiro);
        
        // Act
        Jogo jogo = iniciarJogo.executar();
        
        // Assert
        assertEquals(2, jogo.getJogadores().size());
        assertEquals("João", jogo.getJogadores().get(0).getNome());
        assertEquals("Maria", jogo.getJogadores().get(1).getNome());
    }
    
    @Test
    void deveLancarExcecaoParaMaisDeCincoJogadores() {
        // Arrange
        for (int i = 1; i <= 5; i++) {
            jogadores.add(JogadorBuilder.umJogador().comNome("Jogador" + i).build());
        }
        
        IniciarJogo iniciarJogo = new IniciarJogo(jogadores, tabuleiro);
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> iniciarJogo.executar(),
            "Deve lançar exceção para mais de 4 jogadores"
        );
        
        assertEquals("O jogo suporta no máximo 4 jogadores.", exception.getMessage());
    }
    
    @Test
    void devePosicionarTodosJogadoresNaMesmaCidadeInicial() {
        // Arrange
        jogadores.add(JogadorBuilder.umJogador().comNome("João").build());
        jogadores.add(JogadorBuilder.umJogador().comNome("Maria").build());
        jogadores.add(JogadorBuilder.umJogador().comNome("Pedro").build());
        
        IniciarJogo iniciarJogo = new IniciarJogo(jogadores, tabuleiro);
        
        // Act
        Jogo jogo = iniciarJogo.executar();
        
        // Assert
        assertNotNull(jogo.getJogadores().get(0).getPosicao(), "Jogador 1 deve ter posição");
        assertNotNull(jogo.getJogadores().get(1).getPosicao(), "Jogador 2 deve ter posição");
        assertNotNull(jogo.getJogadores().get(2).getPosicao(), "Jogador 3 deve ter posição");
        
        // Todos devem estar na mesma cidade inicial
        assertEquals(
            jogo.getJogadores().get(0).getPosicao(), 
            jogo.getJogadores().get(1).getPosicao(),
            "Jogadores 1 e 2 devem estar na mesma posição inicial"
        );
        assertEquals(
            jogo.getJogadores().get(1).getPosicao(), 
            jogo.getJogadores().get(2).getPosicao(),
            "Jogadores 2 e 3 devem estar na mesma posição inicial"
        );
    }

}
