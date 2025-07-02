package com.example.pandemic.domínio.entidades;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.pandemic.domínio.acoes.Acao;
import com.example.pandemic.domínio.entidades.cartas.Embaralhador;

@ExtendWith(MockitoExtension.class)
class JogoIntegracaoTest {

    @Mock
    private Tabuleiro mockTabuleiro;
    
    @Mock
    private Embaralhador mockEmbaralhador;
    
    @Mock
    private Jogador mockJogador1;
    
    @Mock
    private Jogador mockJogador2;
    
    @Mock
    private Acao mockAcao;
    
    @Mock
    private Doenca mockDoenca;
    
    private List<Jogador> jogadores;
    private Jogo jogo;

    @BeforeEach
    void setUp() {
        jogadores = Arrays.asList(mockJogador1, mockJogador2);
        when(mockJogador1.getNome()).thenReturn("Jogador 1");
        when(mockJogador2.getNome()).thenReturn("Jogador 2");
        when(mockTabuleiro.getDoencas()).thenReturn(Arrays.asList(mockDoenca));
        when(mockDoenca.isCuraDescoberta()).thenReturn(false);
        
        jogo = new Jogo(mockTabuleiro, jogadores, mockEmbaralhador);
    }

    @Test
    void deveGerenciarTurnManagerCorretamente() {
        // Arrange
        TurnManager turnManager = jogo.getTurnManager();
        
        // Assert estado inicial
        assertEquals(0, turnManager.getTurnoAtual(), "Deve iniciar no turno 0");
        assertEquals(4, turnManager.getAcoesRestantes(), "Deve iniciar com 4 ações");
        assertEquals(mockJogador1, turnManager.getJogadorAtual(), "Primeiro jogador deve ser o atual");
        
        // Act - consumir ações
        turnManager.consumirAcao();
        turnManager.consumirAcao();
        
        // Assert após consumir ações
        assertEquals(2, turnManager.getAcoesRestantes(), "Deve ter 2 ações restantes");
        assertTrue(turnManager.temAcoesRestantes(), "Deve ainda ter ações restantes");
        
        // Act - avançar turno
        when(mockEmbaralhador.darCartaAoJogador(any())).thenReturn(true);
        jogo.proximoTurno();
        
        // Assert após próximo turno
        assertEquals(1, turnManager.getTurnoAtual(), "Turno deve ter avançado");
        assertEquals(4, turnManager.getAcoesRestantes(), "Ações devem ter sido resetadas");
        assertEquals(mockJogador2, turnManager.getJogadorAtual(), "Deve ter mudado para o segundo jogador");
    }

    @Test
    void deveGerenciarGameStateManagerCorretamente() {
        // Arrange
        GameStateManager gameStateManager = jogo.getGameStateManager();
        
        // Test vitória
        when(mockDoenca.isCuraDescoberta()).thenReturn(true);
        assertTrue(gameStateManager.verificarVitoria(), "Deve detectar vitória");
        
        // Test não vitória
        when(mockDoenca.isCuraDescoberta()).thenReturn(false);
        assertFalse(gameStateManager.verificarVitoria(), "Não deve detectar vitória");
        
        // Test derrota por cartas
        when(mockEmbaralhador.temCartas()).thenReturn(false);
        assertTrue(gameStateManager.verificarDerrota(), "Deve detectar derrota por falta de cartas");
        
        // Test não derrota
        when(mockEmbaralhador.temCartas()).thenReturn(true);
        when(mockTabuleiro.getCidades()).thenReturn(Arrays.asList());
        assertFalse(gameStateManager.verificarDerrota(), "Não deve detectar derrota");
    }

    @Test
    void deveRenderizarEstadoDoJogoComGameRenderer() {
        // Act
        String estadoJogo = jogo.toString();
        
        // Assert
        assertNotNull(estadoJogo, "Estado do jogo não deve ser null");
        assertFalse(estadoJogo.isEmpty(), "Estado do jogo não deve estar vazio");
        assertTrue(estadoJogo.contains("PANDEMIC"), "Deve conter o título do jogo");
    }

    @Test
    void deveIntegrarTodosComponentesEmFluxoCompleto() {
        // Arrange
        when(mockEmbaralhador.darCartaAoJogador(any())).thenReturn(true);
        when(mockEmbaralhador.temCartas()).thenReturn(true);
        when(mockTabuleiro.getCidades()).thenReturn(Arrays.asList());
        
        // Estado inicial
        assertTrue(jogo.isJogoAtivo());
        assertFalse(jogo.isJogoVitoria());
        assertEquals(mockJogador1, jogo.getJogadorAtual());
        assertEquals(4, jogo.getTurnManager().getAcoesRestantes());
        
        // Simular algumas ações
        jogo.getTurnManager().consumirAcao();
        jogo.getTurnManager().consumirAcao();
        assertEquals(2, jogo.getTurnManager().getAcoesRestantes());
        
        // Próximo turno
        jogo.proximoTurno();
        
        // Verificar estado após turno
        assertEquals(mockJogador2, jogo.getJogadorAtual());
        assertEquals(4, jogo.getTurnManager().getAcoesRestantes());
        assertTrue(jogo.isJogoAtivo());
        
        // Verificar que tabuleiro foi atualizado
        verify(mockTabuleiro).espalharInfeccao();
        verify(mockEmbaralhador).darCartaAoJogador(mockJogador2);
    }

    @Test
    void deveTestarFluxoDeVitoriaCompleto() {
        // Arrange - Setup para vitória
        when(mockDoenca.isCuraDescoberta()).thenReturn(true);
        when(mockEmbaralhador.darCartaAoJogador(any())).thenReturn(true);
        when(mockEmbaralhador.temCartas()).thenReturn(true);
        
        // Act
        jogo.proximoTurno();
        
        // Assert
        assertTrue(jogo.isJogoVitoria(), "Jogo deve estar em vitória");
        assertFalse(jogo.isJogoAtivo(), "Jogo deve estar inativo");
        
        // Verificar que ainda funciona após vitória
        String estadoFinal = jogo.toString();
        assertNotNull(estadoFinal);
        assertTrue(estadoFinal.contains("PANDEMIC"));
    }

    @Test
    void deveTestarFluxoDeDerrotaCompleto() {
        // Arrange - Setup para derrota
        when(mockDoenca.isCuraDescoberta()).thenReturn(false);
        when(mockEmbaralhador.darCartaAoJogador(any())).thenReturn(true);
        when(mockEmbaralhador.temCartas()).thenReturn(false); // Sem cartas = derrota
        when(mockTabuleiro.getCidades()).thenReturn(Arrays.asList());
        
        // Act
        jogo.proximoTurno();
        
        // Assert
        assertFalse(jogo.isJogoVitoria(), "Jogo não deve estar em vitória");
        assertFalse(jogo.isJogoAtivo(), "Jogo deve estar inativo após derrota");
        
        // Verificar que ainda funciona após derrota
        String estadoFinal = jogo.toString();
        assertNotNull(estadoFinal);
        assertTrue(estadoFinal.contains("PANDEMIC"));
    }

    @Test
    void deveValidarDependenciasNaoNulas() {
        // Assert
        assertNotNull(jogo.getTabuleiro(), "Tabuleiro não deve ser null");
        assertNotNull(jogo.getEmbaralhador(), "Embaralhador não deve ser null");
        assertNotNull(jogo.getTurnManager(), "TurnManager não deve ser null");
        assertNotNull(jogo.getGameStateManager(), "GameStateManager não deve ser null");
        assertNotNull(jogo.getJogadorAtual(), "Jogador atual não deve ser null");
    }
}
