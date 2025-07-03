package com.example.pandemic.domínio.acoes.movimento;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.pandemic.domínio.acoes.movimento.PonteAerea;
import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.utils.builders.CidadeBuilder;
import com.example.pandemic.domínio.utils.builders.JogadorBuilder;

class PonteAereaTest {

    private Jogador jogador;
    private Cidade cidadeOrigemComCentro;
    private Cidade cidadeDestinoComCentro;
    private Cidade cidadeSemCentro;
    private PonteAerea ponteAerea;

    @BeforeEach
    void setUp() {
        // Criar cidades
        cidadeOrigemComCentro = CidadeBuilder.umaCidade()
            .comNome("Cidade Origem")
            .comCentroDePesquisa()
            .build();
            
        cidadeDestinoComCentro = CidadeBuilder.umaCidade()
            .comNome("Cidade Destino")
            .comCentroDePesquisa()
            .build();
            
        cidadeSemCentro = CidadeBuilder.umaCidade()
            .comNome("Cidade Sem Centro")
            .build();

        // Criar jogador
        jogador = JogadorBuilder.umJogador()
            .comNome("Jogador Teste")
            .build();
        jogador.setPosicao(cidadeOrigemComCentro);
        jogador.setCartas(new ArrayList<>());

        // Criar ação
        ponteAerea = new PonteAerea(cidadeDestinoComCentro);
    }

    @Test
    void deveExecutarPonteAereaComSucesso() {
        // Act
        ponteAerea.executar(jogador);
        
        // Assert
        assertEquals(cidadeDestinoComCentro, jogador.getPosicao());
    }

    @Test
    void deveFalharSemCentroDePesquisaNaOrigem() {
        // Arrange
        jogador.setPosicao(cidadeSemCentro);
        
        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> ponteAerea.executar(jogador));
        assertEquals("Jogador deve estar em uma cidade com centro de pesquisa para usar Ponte Aérea.", 
            exception.getMessage());
    }

    @Test
    void deveFalharSemCentroDePesquisaNoDestino() {
        // Arrange
        PonteAerea ponteAereaSemCentro = new PonteAerea(cidadeSemCentro);
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> ponteAereaSemCentro.executar(jogador));
        assertEquals("Cidade de destino deve ter um centro de pesquisa para usar Ponte Aérea.", 
            exception.getMessage());
    }

    @Test
    void deveFalharParaMesmaCidade() {
        // Arrange
        PonteAerea ponteAereaMesmaCidade = new PonteAerea(cidadeOrigemComCentro);
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> ponteAereaMesmaCidade.executar(jogador));
        assertEquals("Não é possível usar Ponte Aérea para a mesma cidade.", 
            exception.getMessage());
    }

    @Test
    void deveFalharSemPosicao() {
        // Arrange
        jogador.setPosicao(null);
        
        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> ponteAerea.executar(jogador));
        assertEquals("Jogador deve estar em uma cidade para usar Ponte Aérea.", 
            exception.getMessage());
    }

    @Test
    void deveRetornarNomeCorreto() {
        assertEquals("Ponte Aérea", ponteAerea.getNome());
    }

    @Test
    void deveRetornarDescricaoCorreta() {
        assertEquals("Se o jogador está em um centro de pesquisa, pode ir para outra cidade com centro de pesquisa.", 
            ponteAerea.getDescricao());
    }
}
