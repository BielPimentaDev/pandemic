package com.example.pandemic;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.cartas.CartaCidade;
import com.example.pandemic.domínio.entidades.cartas.Embaralhador;
import com.example.pandemic.domínio.enums.Personagem;
import com.example.pandemic.domínio.utils.builders.JogadorBuilder;

import static org.junit.jupiter.api.Assertions.*;

class EmbaralhadorTest {
    
    @Test
    void deveEmbaralharEDistribuirCartas() {
        // Arrange - Preparar cartas
        List<CartaCidade> cartas = new ArrayList<>();
        cartas.add(new CartaCidade("São Paulo"));
        cartas.add(new CartaCidade("Rio de Janeiro"));
        cartas.add(new CartaCidade("Brasília"));
        cartas.add(new CartaCidade("Salvador"));
        
        Embaralhador embaralhador = new Embaralhador(cartas);
        
        // Criar jogadores
        Jogador jogador1 = JogadorBuilder.umJogador()
            .comNome("Dr. Silva")
            .comPersonagem(Personagem.MEDICO)
            .build();
            
        Jogador jogador2 = JogadorBuilder.umJogador()
            .comNome("Dr. Santos")
            .comPersonagem(Personagem.MEDICO)
            .build();
        
        // Act - Distribuir cartas
        assertTrue(embaralhador.darCartaAoJogador(jogador1));
        assertTrue(embaralhador.darCartaAoJogador(jogador2));
        assertTrue(embaralhador.darCartaAoJogador(jogador1));
        assertTrue(embaralhador.darCartaAoJogador(jogador2));
        
        // Assert - Verificar resultados
        assertEquals(2, jogador1.getCartas().size(), "Jogador 1 deve ter 2 cartas");
        assertEquals(2, jogador2.getCartas().size(), "Jogador 2 deve ter 2 cartas");
        assertEquals(0, embaralhador.getCartasRestantes(), "Não deve restar cartas no embaralhador");
        assertFalse(embaralhador.temCartas(), "Embaralhador não deve ter mais cartas");
        
        // Tentar dar mais uma carta quando não há mais
        assertFalse(embaralhador.darCartaAoJogador(jogador1), "Não deve conseguir dar carta quando não há mais");
        
        System.out.println("\n=== TESTE DO EMBARALHADOR ===");
        System.out.println("Jogador 1 (" + jogador1.getPersonagem() + "):");
        for (CartaCidade carta : jogador1.getCartas()) {
            System.out.println("  - " + carta.getNome());
        }
        
        System.out.println("\nJogador 2 (" + jogador2.getPersonagem() + "):");
        for (CartaCidade carta : jogador2.getCartas()) {
            System.out.println("  - " + carta.getNome());
        }
        
        System.out.println("\nCartas restantes no embaralhador: " + embaralhador.getCartasRestantes());
    }
}
