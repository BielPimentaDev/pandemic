package com.example.pandemic.domínio.acoes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.pandemic.domínio.acoes.ConstruirCentroDePesquisa;
import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Doenca;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.cartas.CartaCidade;
import com.example.pandemic.domínio.enums.Cor;
import com.example.pandemic.domínio.utils.builders.CidadeBuilder;
import com.example.pandemic.domínio.utils.builders.DoencaBuilder;
import com.example.pandemic.domínio.utils.builders.JogadorBuilder;

public class ConstruirCentroDePesquisaTest {
    
    private ConstruirCentroDePesquisa construirCentroDePesquisa;
    private Jogador jogador;
    private Cidade cidade;
    private Doenca doenca;
    
    @BeforeEach
    public void setUp() {
        construirCentroDePesquisa = new ConstruirCentroDePesquisa();
        
        // Criar doença
        doenca = DoencaBuilder.umaDoenca()
            .comNome("Covid")
            .amarela()
            .build();
        
        // Criar cidade sem centro de pesquisa
        cidade = CidadeBuilder.umaCidade()
            .comNome("São Paulo")
            .comDoenca(doenca)
            .build(); // Não chama comCentroDePesquisa()
        
        // Criar jogador na cidade
        jogador = JogadorBuilder.umJogador()
            .comNome("Dr. Santos")
            .build();
        jogador.setPosicao(cidade);
    }
    
    @Test
    public void deveConstruirCentroDePesquisaComSucesso() {
        // Arrange - Adicionar carta da cidade atual
        List<CartaCidade> cartas = new ArrayList<>();
        CartaCidade cartaDaCidade = new CartaCidade("São Paulo", Cor.AMARELO);
        cartas.add(cartaDaCidade);
        cartas.add(new CartaCidade("Rio de Janeiro", Cor.AZUL)); // Carta extra
        jogador.setCartas(cartas);
        
        // Verificar estado inicial
        assertFalse(cidade.hasCentroDePesquisa(), "A cidade não deveria ter centro de pesquisa inicialmente");
        
        // Act
        construirCentroDePesquisa.executar(jogador);
        
        // Assert
        assertTrue(cidade.hasCentroDePesquisa(), "A cidade deveria ter centro de pesquisa após a construção");
        assertEquals(1, jogador.getCartas().size(), "Uma carta deveria ter sido removida");
        assertFalse(
            jogador.getCartas().stream().anyMatch(carta -> carta.getNome().equals("São Paulo")),
            "A carta da cidade deveria ter sido removida"
        );
    }
    
    @Test
    public void deveConstruirCentroDePesquisaComApenasACartaNecessaria() {
        // Arrange - Apenas a carta da cidade atual
        List<CartaCidade> cartas = new ArrayList<>();
        CartaCidade cartaDaCidade = new CartaCidade("São Paulo", Cor.AMARELO);
        cartas.add(cartaDaCidade);
        jogador.setCartas(cartas);
        
        // Act
        construirCentroDePesquisa.executar(jogador);
        
        // Assert
        assertTrue(cidade.hasCentroDePesquisa(), "A cidade deveria ter centro de pesquisa após a construção");
        assertEquals(0, jogador.getCartas().size(), "Todas as cartas deveriam ter sido removidas");
    }
    
    @Test
    public void deveFalharJaExisteCentroDePesquisa() {
        // Arrange - Cidade já tem centro de pesquisa
        cidade.setCentroDePesquisa();
        
        List<CartaCidade> cartas = new ArrayList<>();
        CartaCidade cartaDaCidade = new CartaCidade("São Paulo", Cor.AMARELO);
        cartas.add(cartaDaCidade);
        jogador.setCartas(cartas);
        
        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> construirCentroDePesquisa.executar(jogador),
            "Deveria falhar quando já existe centro de pesquisa"
        );
        
        assertEquals(
            "Já existe um centro de pesquisa nesta cidade.",
            exception.getMessage()
        );
        assertEquals(1, jogador.getCartas().size(), "As cartas não deveriam ter sido removidas");
    }
    
    @Test
    public void deveFalharSemCartaDaCidade() {
        // Arrange - Cartas de outras cidades
        List<CartaCidade> cartas = new ArrayList<>();
        cartas.add(new CartaCidade("Rio de Janeiro", Cor.AZUL));
        cartas.add(new CartaCidade("Brasília", Cor.VERMELHO));
        jogador.setCartas(cartas);
        
        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> construirCentroDePesquisa.executar(jogador),
            "Deveria falhar sem carta da cidade atual"
        );
        
        assertEquals(
            "Você não possui a carta da cidade onde deseja construir o centro de pesquisa.",
            exception.getMessage()
        );
        assertFalse(cidade.hasCentroDePesquisa(), "Não deveria ter construído centro de pesquisa");
        assertEquals(2, jogador.getCartas().size(), "As cartas não deveriam ter sido removidas");
    }
    
    @Test
    public void deveFalharSemCartas() {
        // Arrange - Jogador sem cartas
        jogador.setCartas(new ArrayList<>());
        
        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> construirCentroDePesquisa.executar(jogador),
            "Deveria falhar sem cartas"
        );
        
        assertEquals(
            "Você não possui a carta da cidade onde deseja construir o centro de pesquisa.",
            exception.getMessage()
        );
        assertFalse(cidade.hasCentroDePesquisa(), "Não deveria ter construído centro de pesquisa");
    }
    
    @Test
    public void deveConstruirComMultiplasCartasDaMesmaCidade() {
        // Arrange - Múltiplas cartas da mesma cidade (deveria remover apenas uma)
        List<CartaCidade> cartas = new ArrayList<>();
        cartas.add(new CartaCidade("São Paulo", Cor.AMARELO));
        cartas.add(new CartaCidade("São Paulo", Cor.AMARELO));
        cartas.add(new CartaCidade("Rio de Janeiro", Cor.AZUL));
        jogador.setCartas(cartas);
        
        // Act
        construirCentroDePesquisa.executar(jogador);
        
        // Assert
        assertTrue(cidade.hasCentroDePesquisa(), "A cidade deveria ter centro de pesquisa após a construção");
        assertEquals(2, jogador.getCartas().size(), "Duas cartas deveriam restar");
        
        // Verificar se ainda há uma carta de São Paulo
        long cartasSaoPaulo = jogador.getCartas().stream()
            .filter(carta -> carta.getNome().equals("São Paulo"))
            .count();
        assertEquals(1, cartasSaoPaulo, "Deveria restar uma carta de São Paulo");
    }
    
    @Test
    public void deveConstruirEmCidadeComNomeComEspacos() {
        // Arrange - Cidade com nome composto
        Cidade cidadeComEspacos = CidadeBuilder.umaCidade()
            .comNome("Rio de Janeiro")
            .comDoenca(doenca)
            .build();
        jogador.setPosicao(cidadeComEspacos);
        
        List<CartaCidade> cartas = new ArrayList<>();
        cartas.add(new CartaCidade("Rio de Janeiro", Cor.AZUL));
        jogador.setCartas(cartas);
        
        // Act
        construirCentroDePesquisa.executar(jogador);
        
        // Assert
        assertTrue(cidadeComEspacos.hasCentroDePesquisa(), "A cidade deveria ter centro de pesquisa");
        assertEquals(0, jogador.getCartas().size(), "A carta deveria ter sido removida");
    }
    
    @Test
    public void deveFalharComCartaDaCidadeComNomeSimilar() {
        // Arrange - Carta com nome similar mas não exato
        List<CartaCidade> cartas = new ArrayList<>();
        cartas.add(new CartaCidade("são paulo", Cor.AMARELO)); // minúscula
        cartas.add(new CartaCidade("Sao Paulo", Cor.AMARELO)); // sem acentos
        jogador.setCartas(cartas);
        
        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> construirCentroDePesquisa.executar(jogador),
            "Deveria falhar com nome não exatamente igual"
        );
        
        assertEquals(
            "Você não possui a carta da cidade onde deseja construir o centro de pesquisa.",
            exception.getMessage()
        );
        assertFalse(cidade.hasCentroDePesquisa(), "Não deveria ter construído centro de pesquisa");
    }
}
