package com.example.pandemic.domain.acoes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.pandemic.domínio.acoes.DescobrirUmaCura;
import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Doenca;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.cartas.CartaCidade;
import com.example.pandemic.domínio.enums.Cor;
import com.example.pandemic.domínio.utils.builders.CidadeBuilder;
import com.example.pandemic.domínio.utils.builders.DoencaBuilder;
import com.example.pandemic.domínio.utils.builders.JogadorBuilder;

public class DescobrirUmaCuraTest {
    
    private DescobrirUmaCura descobrirUmaCura;
    private Jogador jogador;
    private Cidade cidade;
    private Doenca doenca;
    
    @BeforeEach
    public void setUp() {
        descobrirUmaCura = new DescobrirUmaCura();
        
        // Criar doença azul
        doenca = DoencaBuilder.umaDoenca()
            .comNome("Variola")
            .azul()
            .build();
        
        // Criar cidade com centro de pesquisa e doença
        cidade = CidadeBuilder.umaCidade()
            .comNome("Rio de Janeiro")
            .comDoenca(doenca)
            .comCentroDePesquisa()
            .build();
        
        // Criar jogador na cidade
        jogador = JogadorBuilder.umJogador()
            .comNome("Dr. Silva")
            .build();
        jogador.setPosicao(cidade);
    }
    
    @Test
    public void deveDescobrirCuraComSucesso() {
        // Arrange - Adicionar 5 cartas azuis ao jogador
        List<CartaCidade> cartas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CartaCidade carta = new CartaCidade("Carta Azul " + i, Cor.AZUL);
            cartas.add(carta);
        }
        jogador.setCartas(cartas);

        assertEquals(true, cidade.hasCentroDePesquisa());
        
        // Act
        descobrirUmaCura.executar(jogador);
        
        // Assert
        assertTrue(doenca.isCuraDescoberta(), "A cura deveria ter sido descoberta");
        assertEquals(0, jogador.getCartas().size(), "Todas as 5 cartas deveriam ter sido removidas");
    }
    
    @Test
    public void deveDescobrirCuraComMaisDe5Cartas() {
        // Arrange - Adicionar 7 cartas azuis (5 serão removidas)
        List<CartaCidade> cartas = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            CartaCidade carta = new CartaCidade("Carta Azul " + i, Cor.AZUL);
            cartas.add(carta);
        }
        jogador.setCartas(cartas);
        
        // Act
        descobrirUmaCura.executar(jogador);
        
        // Assert
        assertTrue(doenca.isCuraDescoberta(), "A cura deveria ter sido descoberta");
        assertEquals(2, jogador.getCartas().size(), "Deveriam restar 2 cartas após remover 5");
    }
    
    @Test
    public void deveFalharSemCentroDePesquisa() {
        // Arrange - Cidade sem centro de pesquisa
        Cidade cidadeSemCentro = CidadeBuilder.umaCidade()
            .comNome("São Paulo")
            .comDoenca(doenca)
            .build(); // Não chama comCentroDePesquisa()
        
        jogador.setPosicao(cidadeSemCentro);
        
        // Adicionar 5 cartas azuis
        List<CartaCidade> cartas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CartaCidade carta = new CartaCidade("Carta Azul " + i, Cor.AZUL);
            cartas.add(carta);
        }
        jogador.setCartas(cartas);
        
        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> descobrirUmaCura.executar(jogador),
            "Deveria falhar sem centro de pesquisa"
        );
        
        assertEquals(
            "Não é possível descobrir uma cura sem um centro de pesquisa na cidade atual.",
            exception.getMessage()
        );
        assertFalse(doenca.isCuraDescoberta(), "A cura não deveria ter sido descoberta");
    }
    
    @Test
    public void deveFalharComMenosDe5Cartas() {
        // Arrange - Adicionar apenas 3 cartas azuis
        List<CartaCidade> cartas = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CartaCidade carta = new CartaCidade("Carta Azul " + i, Cor.AZUL);
            cartas.add(carta);
        }
        jogador.setCartas(cartas);
        
        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> descobrirUmaCura.executar(jogador),
            "Deveria falhar com menos de 5 cartas"
        );
        
        assertEquals(
            "Você precisa de 5 cartas da mesma cor para descobrir uma cura.",
            exception.getMessage()
        );
        assertFalse(doenca.isCuraDescoberta(), "A cura não deveria ter sido descoberta");
        assertEquals(3, jogador.getCartas().size(), "As cartas não deveriam ter sido removidas");
    }
    
    @Test
    public void deveFalharComCartasDeCorDiferente() {
        // Arrange - Adicionar 5 cartas, mas de cores diferentes
        List<CartaCidade> cartas = new ArrayList<>();
        cartas.add(new CartaCidade("Carta Azul 1", Cor.AZUL));
        cartas.add(new CartaCidade("Carta Azul 2", Cor.AZUL));
        cartas.add(new CartaCidade("Carta Vermelha 1", Cor.VERMELHO));
        cartas.add(new CartaCidade("Carta Amarela 1", Cor.AMARELO));
        cartas.add(new CartaCidade("Carta Preta 1", Cor.PRETO));
        jogador.setCartas(cartas);
        
        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> descobrirUmaCura.executar(jogador),
            "Deveria falhar com cartas de cores diferentes"
        );
        
        assertEquals(
            "Você precisa de 5 cartas da mesma cor para descobrir uma cura.",
            exception.getMessage()
        );
        assertFalse(doenca.isCuraDescoberta(), "A cura não deveria ter sido descoberta");
        assertEquals(5, jogador.getCartas().size(), "As cartas não deveriam ter sido removidas");
    }
    
    @Test
    public void deveFalharSemCartas() {
        // Arrange - Jogador sem cartas
        jogador.setCartas(new ArrayList<>());
        
        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> descobrirUmaCura.executar(jogador),
            "Deveria falhar sem cartas"
        );
        
        assertEquals(
            "Você precisa de 5 cartas da mesma cor para descobrir uma cura.",
            exception.getMessage()
        );
        assertFalse(doenca.isCuraDescoberta(), "A cura não deveria ter sido descoberta");
    }
    
    @Test
    public void deveDescobrirCuraComCartasMistas() {
        // Arrange - 6 cartas azuis e 2 de outras cores
        List<CartaCidade> cartas = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            cartas.add(new CartaCidade("Carta Azul " + i, Cor.AZUL));
        }
        cartas.add(new CartaCidade("Carta Vermelha", Cor.VERMELHO));
        cartas.add(new CartaCidade("Carta Amarela", Cor.AMARELO));
        jogador.setCartas(cartas);
        
        // Act
        descobrirUmaCura.executar(jogador);
        
        // Assert
        assertTrue(doenca.isCuraDescoberta(), "A cura deveria ter sido descoberta");
        assertEquals(3, jogador.getCartas().size(), "Deveriam restar 3 cartas (1 azul + 2 de outras cores)");
        
        // Verificar se as cartas restantes são corretas
        long cartasAzuis = jogador.getCartas().stream()
            .filter(carta -> carta.getCor() == Cor.AZUL)
            .count();
        assertEquals(1, cartasAzuis, "Deveria restar 1 carta azul");
    }
}
