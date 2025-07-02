package com.example.pandemic.domínio.acoes.movimento;

import com.example.pandemic.domínio.acoes.Acao;
import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.cartas.CartaCidade;

/**
 * Ação de movimento Voo Direto:
 * Descarta a carta da cidade de destino para se mover até lá.
 */
public class VooDireto implements Acao {
    private Cidade destino;

    public VooDireto(Cidade destino) {
        this.destino = destino;
    }

    @Override
    public void executar(Jogador jogador) {
        Cidade posicaoAtual = jogador.getPosicao();
        
        if (posicaoAtual == null) {
            throw new IllegalStateException("Jogador deve estar em uma cidade para usar Voo Direto.");
        }
        
        if (posicaoAtual.equals(destino)) {
            throw new IllegalArgumentException("Não é possível usar Voo Direto para a mesma cidade.");
        }
        
        CartaCidade cartaDestino = null;
        for (CartaCidade carta : jogador.getCartas()) {
            if (carta.getNome().equals(destino.getNome())) {
                cartaDestino = carta;
                break;
            }
        }
        
        if (cartaDestino == null) {
            throw new IllegalStateException("Jogador deve ter a carta da cidade de destino para usar Voo Direto.");
        }
        

        jogador.getCartas().remove(cartaDestino);
        
        jogador.setPosicao(destino);
    }

    @Override
    public String getNome() {
        return "Voo Direto";
    }

    @Override
    public String getDescricao() {
        return "Descarta a carta da cidade de destino para se mover até lá.";
    }
}
