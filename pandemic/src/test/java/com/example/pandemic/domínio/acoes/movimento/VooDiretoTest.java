package com.example.pandemic.domínio.acoes.movimento;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.pandemic.domínio.acoes.movimento.VooDireto;
import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.cartas.CartaCidade;
import com.example.pandemic.domínio.enums.Cor;
import com.example.pandemic.domínio.utils.builders.CidadeBuilder;
import com.example.pandemic.domínio.utils.builders.JogadorBuilder;

class VooDiretoTest {

    private Jogador jogador;
    private Cidade cidadeOrigem;
    private Cidade cidadeDestino;
    private CartaCidade cartaCidadeDestino;
    private VooDireto vooDireto;

    @BeforeEach
    void setUp() {
        // Criar cidades
        cidadeOrigem = CidadeBuilder.umaCidade()
            .comNome("Cidade Origem")
            .build();
            
        cidadeDestino = CidadeBuilder.umaCidade()
            .comNome("Cidade Destino")
            .build();

        // Criar carta da cidade destino
        cartaCidadeDestino = new CartaCidade("Cidade Destino", Cor.VERMELHO);

        // Criar jogador
        jogador = JogadorBuilder.umJogador()
            .comNome("Jogador Teste")
            .build();
        jogador.setPosicao(cidadeOrigem);
        
        List<CartaCidade> cartas = new ArrayList<>();
        cartas.add(cartaCidadeDestino);
        jogador.setCartas(cartas);

        // Criar ação
        vooDireto = new VooDireto(cidadeDestino);
    }

    @Test
    void deveExecutarVooDiretoComSucesso() {
        // Act
        vooDireto.executar(jogador);
        
        // Assert
        assertEquals(cidadeDestino, jogador.getPosicao());
        assertFalse(jogador.getCartas().contains(cartaCidadeDestino), 
            "Carta da cidade destino deveria ter sido removida");
        assertEquals(0, jogador.getCartas().size(), 
            "Jogador não deveria ter cartas após usar voo direto");
    }

    @Test
    void deveFalharSemCartaDaCidadeDestino() {
        // Arrange
        jogador.setCartas(new ArrayList<>()); // Remover todas as cartas
        
        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> vooDireto.executar(jogador));
        assertEquals("Jogador deve ter a carta da cidade de destino para usar Voo Direto.", 
            exception.getMessage());
    }

    @Test
    void deveFalharComCartaDeCidadeDiferente() {
        // Arrange
        CartaCidade cartaOutraCidade = new CartaCidade("Outra Cidade", Cor.AZUL);
        List<CartaCidade> cartas = new ArrayList<>();
        cartas.add(cartaOutraCidade);
        jogador.setCartas(cartas);
        
        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> vooDireto.executar(jogador));
        assertEquals("Jogador deve ter a carta da cidade de destino para usar Voo Direto.", 
            exception.getMessage());
    }

    @Test
    void deveFalharParaMesmaCidade() {
        // Arrange
        CartaCidade cartaCidadeOrigem = new CartaCidade("Cidade Origem", Cor.AZUL);
        List<CartaCidade> cartas = new ArrayList<>();
        cartas.add(cartaCidadeOrigem);
        jogador.setCartas(cartas);
        
        VooDireto vooDiretoMesmaCidade = new VooDireto(cidadeOrigem);
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> vooDiretoMesmaCidade.executar(jogador));
        assertEquals("Não é possível usar Voo Direto para a mesma cidade.", 
            exception.getMessage());
    }

    @Test
    void deveFalharSemPosicao() {
        // Arrange
        jogador.setPosicao(null);
        
        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> vooDireto.executar(jogador));
        assertEquals("Jogador deve estar em uma cidade para usar Voo Direto.", 
            exception.getMessage());
    }

    @Test
    void deveManterOutrasCartasAposUso() {
        // Arrange
        CartaCidade outraCarta = new CartaCidade("Outra Carta", Cor.AMARELO);
        List<CartaCidade> cartas = new ArrayList<>();
        cartas.add(cartaCidadeDestino);
        cartas.add(outraCarta);
        jogador.setCartas(cartas);
        
        // Act
        vooDireto.executar(jogador);
        
        // Assert
        assertEquals(1, jogador.getCartas().size());
        assertTrue(jogador.getCartas().contains(outraCarta));
        assertFalse(jogador.getCartas().contains(cartaCidadeDestino));
    }

    @Test
    void deveRetornarNomeCorreto() {
        assertEquals("Voo Direto", vooDireto.getNome());
    }

    @Test
    void deveRetornarDescricaoCorreta() {
        assertEquals("Descarta a carta da cidade de destino para se mover até lá.", 
            vooDireto.getDescricao());
    }
}
