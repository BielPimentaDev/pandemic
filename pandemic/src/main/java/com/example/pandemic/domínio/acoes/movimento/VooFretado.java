package com.example.pandemic.domínio.acoes.movimento;

import com.example.pandemic.domínio.acoes.Acao;
import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.cartas.CartaCidade;

/**
 * Ação de movimento Voo Fretado:
 * Se tiver a carta da cidade onde está, pode se mover para qualquer cidade.
 */
public class VooFretado implements Acao {
    private Cidade destino;

    public VooFretado(Cidade destino) {
        this.destino = destino;
    }

    @Override
    public void executar(Jogador jogador) {
        Cidade posicaoAtual = jogador.getPosicao();
        
        if (posicaoAtual == null) {
            throw new IllegalStateException("Jogador não tem posição atual definida.");
        }
        
        if (posicaoAtual.equals(destino)) {
            throw new IllegalArgumentException("Não é possível usar Voo Fretado para a mesma cidade.");
        }
        
        CartaCidade cartaCidadeAtual = null;
        for (CartaCidade carta : jogador.getCartas()) {
            if (carta.getNome().equals(posicaoAtual.getNome())) {
                cartaCidadeAtual = carta;
                break;
            }
        }
        
        if (cartaCidadeAtual == null) {
            throw new IllegalStateException("Jogador deve ter a carta da cidade atual para usar Voo Fretado.");
        }
        

        jogador.getCartas().remove(cartaCidadeAtual);
        jogador.setPosicao(destino);
    }

    @Override
    public String getNome() {
        return "Voo Fretado";
    }

    @Override
    public String getDescricao() {
        return "Se tiver a carta da cidade onde está, pode se mover para qualquer cidade descartando a carta atual.";
    }
}
