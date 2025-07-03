package com.example.pandemic.domínio.acoes.movimento;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.pandemic.domínio.acoes.movimento.VooFretado;
import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.cartas.CartaCidade;
import com.example.pandemic.domínio.enums.Cor;
import com.example.pandemic.domínio.utils.builders.CidadeBuilder;
import com.example.pandemic.domínio.utils.builders.JogadorBuilder;

class VooFretadoTest {

    private Jogador jogador;
    private Cidade cidadeOrigem;
    private Cidade cidadeDestino;
    private CartaCidade cartaCidadeOrigem;
    private VooFretado vooFretado;

    @BeforeEach
    void setUp() {
        // Criar cidades
        cidadeOrigem = CidadeBuilder.umaCidade()
            .comNome("Cidade Origem")
            .build();
            
        cidadeDestino = CidadeBuilder.umaCidade()
            .comNome("Cidade Destino")
            .build();

        // Criar carta da cidade origem
        cartaCidadeOrigem = new CartaCidade("Cidade Origem", Cor.AZUL);

        // Criar jogador
        jogador = JogadorBuilder.umJogador()
            .comNome("Jogador Teste")
            .build();
        jogador.setPosicao(cidadeOrigem);
        
        List<CartaCidade> cartas = new ArrayList<>();
        cartas.add(cartaCidadeOrigem);
        jogador.setCartas(cartas);

        // Criar ação
        vooFretado = new VooFretado(cidadeDestino);
    }

    @Test
    void deveExecutarVooFretadoComSucesso() {
        // Act
        vooFretado.executar(jogador);
        
        // Assert
        assertEquals(cidadeDestino, jogador.getPosicao());
        assertFalse(jogador.getCartas().contains(cartaCidadeOrigem), 
            "Carta da cidade origem deveria ter sido removida");
        assertEquals(0, jogador.getCartas().size(), 
            "Jogador não deveria ter cartas após usar voo fretado");
    }

    @Test
    void deveFalharSemCartaDaCidadeAtual() {
  
        jogador.setCartas(new ArrayList<>()); 

        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> vooFretado.executar(jogador));
        assertEquals("Jogador deve ter a carta da cidade atual para usar Voo Fretado.", 
            exception.getMessage());
    }

    @Test
    void deveFalharComCartaDeCidadeDiferente() {
        CartaCidade cartaOutraCidade = new CartaCidade("Outra Cidade", Cor.VERMELHO);
        List<CartaCidade> cartas = new ArrayList<>();
        cartas.add(cartaOutraCidade);
        jogador.setCartas(cartas);
        

        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> vooFretado.executar(jogador));
        assertEquals("Jogador deve ter a carta da cidade atual para usar Voo Fretado.", 
            exception.getMessage());
    }

    @Test
    void deveFalharParaMesmaCidade() {
        VooFretado vooFretadoMesmaCidade = new VooFretado(cidadeOrigem);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> vooFretadoMesmaCidade.executar(jogador));
        assertEquals("Não é possível usar Voo Fretado para a mesma cidade.", 
            exception.getMessage());
    }

    @Test
    void deveManterOutrasCartasAposUso() {
        CartaCidade outraCarta = new CartaCidade("Outra Carta", Cor.AMARELO);
        List<CartaCidade> cartas = new ArrayList<>();
        cartas.add(cartaCidadeOrigem);
        cartas.add(outraCarta);
        jogador.setCartas(cartas);
        
        vooFretado.executar(jogador);
        
        assertEquals(1, jogador.getCartas().size());
        assertTrue(jogador.getCartas().contains(outraCarta));
        assertFalse(jogador.getCartas().contains(cartaCidadeOrigem));
    }
}
