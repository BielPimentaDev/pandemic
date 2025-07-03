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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.example.pandemic.domínio.entidades.cartas.Embaralhador;
import com.example.pandemic.domínio.entidades.jogo.Jogo;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JogoEstadosTest {

    @Mock
    private Tabuleiro mockTabuleiro;
    
    @Mock
    private Embaralhador mockEmbaralhador;
    
    @Mock
    private Jogador mockJogador1;
    
    @Mock
    private Jogador mockJogador2;
    
    @Mock
    private Doenca mockDoenca1;
    
    @Mock
    private Doenca mockDoenca2;
    
    @Mock
    private Doenca mockDoenca3;
    
    @Mock
    private Doenca mockDoenca4;
    
    @Mock
    private Cidade mockCidade1;
    
    @Mock
    private Cidade mockCidade2;
    
    private List<Jogador> jogadores;
    private List<Doenca> doencas;
    private List<Cidade> cidades;
    private Jogo jogo;

    @BeforeEach
    void setUp() {
        jogadores = Arrays.asList(mockJogador1, mockJogador2);
        doencas = Arrays.asList(mockDoenca1, mockDoenca2, mockDoenca3, mockDoenca4);
        cidades = Arrays.asList(mockCidade1, mockCidade2);
        
        // Setup básico necessário para funcionamento
        when(mockTabuleiro.getDoencas()).thenReturn(doencas);
        when(mockTabuleiro.getCidades()).thenReturn(cidades);
        when(mockJogador1.getNome()).thenReturn("Jogador 1");
        when(mockJogador2.getNome()).thenReturn("Jogador 2");
        
        jogo = new Jogo(mockTabuleiro, jogadores, mockEmbaralhador);
    }

    @Test
    void deveDetectarVitoriaQuandoTodasCurasSaoDescubertas() {
        // Arrange - apenas stubbings necessários para este teste
        when(mockTabuleiro.getDoencas()).thenReturn(doencas);
        when(mockDoenca1.isCuraDescoberta()).thenReturn(true);
        when(mockDoenca2.isCuraDescoberta()).thenReturn(true);
        when(mockDoenca3.isCuraDescoberta()).thenReturn(true);
        when(mockDoenca4.isCuraDescoberta()).thenReturn(true);
        when(mockEmbaralhador.darCartaAoJogador(any())).thenReturn(true);
        
        // Act
        jogo.proximoTurno();
        
        // Assert
        assertTrue(jogo.isJogoVitoria(), "Jogo deve estar em vitória quando todas as curas são descobertas");
        assertFalse(jogo.isJogoAtivo(), "Jogo deve estar inativo após vitória");
    }

    @Test
    void naoDeveDetectarVitoriaQuandoApenasAlgumasCurasSaoDescubertas() {
        // Arrange - apenas stubbings necessários para este teste
        when(mockTabuleiro.getDoencas()).thenReturn(doencas);
        when(mockTabuleiro.getCidades()).thenReturn(cidades);
        when(mockDoenca1.isCuraDescoberta()).thenReturn(true);
        when(mockDoenca2.isCuraDescoberta()).thenReturn(true);
        when(mockDoenca3.isCuraDescoberta()).thenReturn(false); // Uma cura não descoberta
        when(mockDoenca4.isCuraDescoberta()).thenReturn(true);
        when(mockEmbaralhador.darCartaAoJogador(any())).thenReturn(true);
        when(mockEmbaralhador.temCartas()).thenReturn(true);
        
        // Mock das cidades para não causar derrota por infecção
        when(mockCidade1.getDoenca()).thenReturn(null);
        when(mockCidade2.getDoenca()).thenReturn(null);
        
        // Act
        jogo.proximoTurno();
        
        // Assert
        assertFalse(jogo.isJogoVitoria(), "Jogo não deve estar em vitória quando nem todas as curas são descobertas");
        assertTrue(jogo.isJogoAtivo(), "Jogo deve continuar ativo");
    }

    @Test
    void deveDetectarDerrotaPorFaltaDeCartas() {
        // Arrange - apenas stubbings necessários para este teste
        when(mockTabuleiro.getDoencas()).thenReturn(doencas);
        when(mockTabuleiro.getCidades()).thenReturn(cidades);
        when(mockDoenca1.isCuraDescoberta()).thenReturn(false);
        when(mockDoenca2.isCuraDescoberta()).thenReturn(false);
        when(mockDoenca3.isCuraDescoberta()).thenReturn(false);
        when(mockDoenca4.isCuraDescoberta()).thenReturn(false);
        when(mockEmbaralhador.temCartas()).thenReturn(false); // Sem cartas
        when(mockEmbaralhador.darCartaAoJogador(any())).thenReturn(true);
        
        // Mock das cidades para não causar derrota por infecção
        when(mockCidade1.getDoenca()).thenReturn(null);
        when(mockCidade2.getDoenca()).thenReturn(null);
        
        // Act
        jogo.proximoTurno();
        
        // Assert
        assertFalse(jogo.isJogoVitoria(), "Jogo não deve estar em vitória");
        assertFalse(jogo.isJogoAtivo(), "Jogo deve estar inativo após derrota");
    }

    @Test
    void deveDetectarDerrotaPorInfeccaoGeneralizada() {
        // Arrange - apenas stubbings necessários para este teste
        when(mockTabuleiro.getDoencas()).thenReturn(doencas);
        when(mockTabuleiro.getCidades()).thenReturn(cidades);
        when(mockDoenca1.isCuraDescoberta()).thenReturn(false);
        when(mockDoenca2.isCuraDescoberta()).thenReturn(false);
        when(mockDoenca3.isCuraDescoberta()).thenReturn(false);
        when(mockDoenca4.isCuraDescoberta()).thenReturn(false);
        when(mockEmbaralhador.temCartas()).thenReturn(true); // Tem cartas
        when(mockEmbaralhador.darCartaAoJogador(any())).thenReturn(true);
        
        // Mock das cidades com infecção alta
        when(mockCidade1.getDoenca()).thenReturn(mockDoenca1);
        when(mockCidade2.getDoenca()).thenReturn(mockDoenca2);
        when(mockDoenca1.getIntensidade()).thenReturn(3); // Intensidade crítica
        when(mockDoenca2.getIntensidade()).thenReturn(3); // Intensidade crítica
        
        // Act
        jogo.proximoTurno();
        
        // Assert
        assertFalse(jogo.isJogoVitoria(), "Jogo não deve estar em vitória");
        assertFalse(jogo.isJogoAtivo(), "Jogo deve estar inativo após derrota por infecção");
    }

    @Test
    void deveContinuarJogoQuandoNaoHaCondicoesDeFimDeJogo() {
        // Arrange - apenas stubbings necessários para este teste
        when(mockTabuleiro.getDoencas()).thenReturn(doencas);
        when(mockTabuleiro.getCidades()).thenReturn(cidades);
        when(mockDoenca1.isCuraDescoberta()).thenReturn(false);
        when(mockDoenca2.isCuraDescoberta()).thenReturn(true);
        when(mockDoenca3.isCuraDescoberta()).thenReturn(false);
        when(mockDoenca4.isCuraDescoberta()).thenReturn(false);
        when(mockEmbaralhador.temCartas()).thenReturn(true); // Tem cartas
        when(mockEmbaralhador.darCartaAoJogador(any())).thenReturn(true);
        
        // Mock das cidades com infecção baixa
        when(mockCidade1.getDoenca()).thenReturn(mockDoenca1);
        when(mockCidade2.getDoenca()).thenReturn(null);
        when(mockDoenca1.getIntensidade()).thenReturn(1); // Intensidade baixa
        
        // Act
        jogo.proximoTurno();
        
        // Assert
        assertFalse(jogo.isJogoVitoria(), "Jogo não deve estar em vitória");
        assertTrue(jogo.isJogoAtivo(), "Jogo deve continuar ativo");
    }

    @Test
    void deveTestarCenarioComplexoComMultiplasCondicoes() {
        // Arrange - Cenário com 3 curas descobertas, cartas disponíveis, mas infecção crescente
        when(mockDoenca1.isCuraDescoberta()).thenReturn(true);
        when(mockDoenca2.isCuraDescoberta()).thenReturn(true);
        when(mockDoenca3.isCuraDescoberta()).thenReturn(true);
        when(mockDoenca4.isCuraDescoberta()).thenReturn(false); // Uma ainda não descoberta
        when(mockEmbaralhador.temCartas()).thenReturn(true);
        when(mockEmbaralhador.darCartaAoJogador(any())).thenReturn(true);
        
        // Cidades com infecção moderada
        when(mockCidade1.getDoenca()).thenReturn(mockDoenca4);
        when(mockCidade2.getDoenca()).thenReturn(null);
        when(mockDoenca4.getIntensidade()).thenReturn(2); // Intensidade moderada
        
        // Act
        jogo.proximoTurno();
        
        // Assert
        assertFalse(jogo.isJogoVitoria(), "Jogo não deve estar em vitória com 1 cura faltando");
        assertTrue(jogo.isJogoAtivo(), "Jogo deve continuar ativo com condições não críticas");
    }

    @Test
    void deveTestjarEstadoInicialDoGameStateManager() {
        // Arrange - Estado inicial - apenas stubbings necessários para este teste
        when(mockTabuleiro.getDoencas()).thenReturn(doencas);
        when(mockDoenca1.isCuraDescoberta()).thenReturn(false);
        when(mockDoenca2.isCuraDescoberta()).thenReturn(false);
        when(mockDoenca3.isCuraDescoberta()).thenReturn(false);
        when(mockDoenca4.isCuraDescoberta()).thenReturn(false);
        
        // Act & Assert
        assertFalse(jogo.getGameStateManager().verificarVitoria(), 
            "GameStateManager não deve detectar vitória no estado inicial");
        assertTrue(jogo.isJogoAtivo(), "Jogo deve estar ativo no estado inicial");
        assertFalse(jogo.isJogoVitoria(), "Jogo não deve estar em vitória no estado inicial");
    }
}
