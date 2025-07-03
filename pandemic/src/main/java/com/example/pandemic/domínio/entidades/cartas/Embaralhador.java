package com.example.pandemic.domínio.entidades.cartas;

import java.util.Collections;
import java.util.List;

import com.example.pandemic.domínio.entidades.Jogador;

public class Embaralhador {
    private List<CartaCidade> cartas;

    public Embaralhador(List<CartaCidade> cartas) {
        this.cartas = cartas;
        embaralhar();
    }

    public void embaralhar() {
        Collections.shuffle(cartas);
    }

    public boolean darCartaAoJogador(Jogador jogador) {
        // Verifica se ainda há cartas disponíveis
        if (cartas.isEmpty()) {
            System.out.println("Não há mais cartas para dar ao jogador: " + jogador.getNome());
            return false; // Não há mais cartas para dar
        }
        
        // Remove a primeira carta da lista e dá ao jogador
        CartaCidade carta = cartas.remove(0);
        System.out.println("Dando carta: " + carta.getNome() + " ao jogador: " + jogador.getNome());
        jogador.compraCarta(carta);
        return true; // Carta foi dada com sucesso
    }

    public int getCartasRestantes() {
        return cartas.size();
    }

    public boolean temCartas() {
        return !cartas.isEmpty();
    }
}
