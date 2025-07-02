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

import com.example.pandemic.domínio.entidades.cartas.Embaralhador;

@ExtendWith(MockitoExtension.class)
class JogoTest {

    @Mock
    private Tabuleiro mockTabuleiro;
    
    @Mock
    private Embaralhador mockEmbaralhador;
    
    @Mock
    private Jogador mockJogador1;
    
    @Mock
    private Jogador mockJogador2;
    
    private List<Jogador> jogadores;
    private Jogo jogo;

    @BeforeEach
    void setUp() {
        jogadores = Arrays.asList(mockJogador1, mockJogador2);
        jogo = new Jogo(mockTabuleiro, jogadores, mockEmbaralhador);
        
        // Setup básico dos mocks
        when(mockJogador1.getNome()).thenReturn("Jogador 1");
        when(mockJogador2.getNome()).thenReturn("Jogador 2");
    }

    @Test
    void deveIniciarJogoComEstadoCorreto() {
        // Assert
        assertTrue(jogo.isJogoAtivo(), "Jogo deve estar ativo ao iniciar");
        assertFalse(jogo.isJogoVitoria(), "Jogo não deve estar em vitória ao iniciar");
        assertEquals(mockJogador1, jogo.getJogadorAtual(), "Primeiro jogador deve ser o atual");
        assertEquals(4, jogo.getTurnManager().getAcoesRestantes(), "Deve iniciar com 4 ações");
    }

    @Test
    void deveAlternarJogadoresApósProximoTurno() {
        // Arrange
        when(mockTabuleiro.getDoencas()).thenReturn(Arrays.asList());
        when(mockEmbaralhador.darCartaAoJogador(any())).thenReturn(true);
        
        // Act
        Jogador jogadorInicial = jogo.getJogadorAtual();
        jogo.proximoTurno();
        
        // Assert
        assertNotEquals(jogadorInicial, jogo.getJogadorAtual(), 
            "Jogador atual deve ter mudado após próximo turno");
    }

    @Test
    void deveDarCartaAoJogadorComSucesso() {
        // Arrange
        when(mockEmbaralhador.darCartaAoJogador(mockJogador1)).thenReturn(true);
        
        // Act
        boolean resultado = jogo.darCartaAoJogador(mockJogador1);
        
        // Assert
        assertTrue(resultado, "Deve retornar true quando carta é dada com sucesso");
        verify(mockEmbaralhador).darCartaAoJogador(mockJogador1);
    }

    @Test
    void deveFalharAoDarCartaQuandoEmbaralhadorEhNull() {
        // Arrange
        Jogo jogoSemEmbaralhador = new Jogo(mockTabuleiro, jogadores, null);
        
        // Act
        boolean resultado = jogoSemEmbaralhador.darCartaAoJogador(mockJogador1);
        
        // Assert
        assertFalse(resultado, "Deve retornar false quando embaralhador é null");
    }

    @Test
    void deveFalharAoDarCartaQuandoEmbaralhadorFalha() {
        // Arrange
        when(mockEmbaralhador.darCartaAoJogador(mockJogador1)).thenReturn(false);
        
        // Act
        boolean resultado = jogo.darCartaAoJogador(mockJogador1);
        
        // Assert
        assertFalse(resultado, "Deve retornar false quando embaralhador falha");
        verify(mockEmbaralhador).darCartaAoJogador(mockJogador1);
    }

    @Test
    void naoDeveExecutarProximoTurnoQuandoJogoInativo() {
        // Arrange
        jogo.setJogoAtivo(false);
        int turnoAnterior = jogo.getTurnManager().getTurnoAtual();
        
        // Act
        jogo.proximoTurno();
        
        // Assert
        assertEquals(turnoAnterior, jogo.getTurnManager().getTurnoAtual(), 
            "Turno não deve avançar quando jogo está inativo");
        verify(mockTabuleiro, never()).espalharInfeccao();
    }

    @Test
    void deveEspalharInfeccaoAoProximoTurno() {
        // Arrange
        when(mockTabuleiro.getDoencas()).thenReturn(Arrays.asList());
        when(mockEmbaralhador.darCartaAoJogador(any())).thenReturn(true);
        
        // Act
        jogo.proximoTurno();
        
        // Assert
        verify(mockTabuleiro).espalharInfeccao();
    }

    @Test
    void deveResetarAcoesAoProximoTurno() {
        // Arrange
        when(mockTabuleiro.getDoencas()).thenReturn(Arrays.asList());
        when(mockEmbaralhador.darCartaAoJogador(any())).thenReturn(true);
        
        // Consumir algumas ações primeiro
        jogo.getTurnManager().consumirAcao();
        jogo.getTurnManager().consumirAcao();
        assertEquals(2, jogo.getTurnManager().getAcoesRestantes());
        
        // Act
        jogo.proximoTurno();
        
        // Assert
        assertEquals(4, jogo.getTurnManager().getAcoesRestantes(), 
            "Ações devem ser resetadas para 4 no próximo turno");
    }
}
