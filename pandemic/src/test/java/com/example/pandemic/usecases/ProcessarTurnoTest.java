package com.example.pandemic.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.pandemic.aplicação.usecases.ProcessarTurno;
import com.example.pandemic.domínio.acoes.Acao;
import com.example.pandemic.domínio.acoes.TratarDoenca;
import com.example.pandemic.domínio.entidades.jogo.Jogo;
import com.example.pandemic.domínio.utils.builders.usecases.IniciarJogoBuilder;

public class ProcessarTurnoTest {
    
    private Jogo jogo;
    private ProcessarTurno processarTurno;
    
    @BeforeEach
    public void setUp() {
        jogo = IniciarJogoBuilder.umIniciarJogo().build();
        processarTurno = new ProcessarTurno(jogo);
    }
    
    @Test 
    public void deveProcessarUmTurnoComSucesso(){
        // Arrange
        int acoesIniciais = jogo.getAcoesRestantes();
        
        // Act
        System.out.println("====== TURNO ======");
        Acao acao = new TratarDoenca();
        processarTurno.executar(acao);
        
        // Assert
        assertEquals(acoesIniciais - 1, jogo.getAcoesRestantes());
    }
    
    @Test
    public void deveConsumirAcaoCorretamente() {
        // Arrange
        assertEquals(4, jogo.getAcoesRestantes(), "Deve iniciar com 4 ações");
        
        // Act
        Acao acao = new TratarDoenca();
        processarTurno.executar(acao);
        
        // Assert
        assertEquals(3, jogo.getAcoesRestantes(), "Deve ter 3 ações restantes após executar uma ação");
        assertTrue(jogo.temAcoesRestantes(), "Ainda deve ter ações restantes");
    }
    
    @Test
    public void devePassarTurnoQuandoNaoTemMaisAcoes() {
        // Arrange
        String jogadorInicialNome = jogo.getJogadorAtual().getNome();
        
        // Act - Executar todas as 4 ações
        processarTurno.executar(new TratarDoenca());
        processarTurno.executar(new TratarDoenca());
        processarTurno.executar(new TratarDoenca());
        
        // Ainda deve ser o mesmo jogador
        assertEquals(jogadorInicialNome, jogo.getJogadorAtual().getNome());
        assertEquals(1, jogo.getAcoesRestantes());
        
        // Última ação deve passar o turno
        processarTurno.executar(new TratarDoenca());
        
        // Assert
        assertEquals(4, jogo.getAcoesRestantes(), "Ações devem ser resetadas para 4");
        // O jogador atual deve ter mudado (assumindo que há mais de um jogador)
        if (jogo.getJogadores().size() > 1) {
            assertFalse(jogadorInicialNome.equals(jogo.getJogadorAtual().getNome()), 
                "Jogador atual deve ter mudado após esgotar todas as ações");
        }
    }
    
    @Test
    public void deveLancarExcecaoQuandoNaoTemAcoesRestantes() {
        // Arrange - Consumir todas as ações sem passar turno
        jogo.consumirAcao();
        jogo.consumirAcao();
        jogo.consumirAcao();
        jogo.consumirAcao();
        
        // Assert
        assertFalse(jogo.temAcoesRestantes(), "Não deve ter ações restantes");
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> processarTurno.executar(new TratarDoenca()),
            "Deve lançar exceção quando não há ações restantes"
        );
        
        assertEquals("Número máximo de ações por turno excedido", exception.getMessage());
    }
    
    
    @Test
    public void deveManterJogadorAtualAteEsgotarAcoes() {
        // Arrange
        String jogadorInicialNome = jogo.getJogadorAtual().getNome();
        
        // Act - Executar ações sem esgotar todas
        processarTurno.executar(new TratarDoenca());
        processarTurno.executar(new TratarDoenca());
        
        // Assert
        assertEquals(jogadorInicialNome, jogo.getJogadorAtual().getNome(), 
            "Jogador atual deve permanecer o mesmo enquanto ainda há ações");
        assertEquals(2, jogo.getAcoesRestantes(), "Deve ter 2 ações restantes");
    }
    
    @Test
    public void deveVerificarEstadoDoJogoAposProcessarTurno() {
        // Arrange
        assertTrue(jogo.isJogoAtivo(), "Jogo deve estar ativo inicialmente");
        
        // Act - Executar uma ação
        processarTurno.executar(new TratarDoenca());
        
        // Assert
        assertTrue(jogo.isJogoAtivo(), "Jogo deve continuar ativo após executar uma ação");
        assertEquals(3, jogo.getAcoesRestantes());
    }
}
