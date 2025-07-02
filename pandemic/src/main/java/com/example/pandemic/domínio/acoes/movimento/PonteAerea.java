package com.example.pandemic.domínio.acoes.movimento;

import com.example.pandemic.domínio.acoes.Acao;
import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Jogador;

/**
 * Ação de movimento Ponte Aérea:
 * Se o jogador está em um centro de pesquisa, pode ir para outra cidade com centro de pesquisa.
 */
public class PonteAerea implements Acao {
    private Cidade destino;

    public PonteAerea(Cidade destino) {
        this.destino = destino;
    }

    @Override
    public void executar(Jogador jogador) {
        Cidade posicaoAtual = jogador.getPosicao();
        
        // Verificar se o jogador está em uma cidade
        if (posicaoAtual == null) {
            throw new IllegalStateException("Jogador deve estar em uma cidade para usar Ponte Aérea.");
        }
        
        // Verificar se a cidade atual tem centro de pesquisa
        if (!posicaoAtual.hasCentroDePesquisa()) {
            throw new IllegalStateException("Jogador deve estar em uma cidade com centro de pesquisa para usar Ponte Aérea.");
        }
        
        // Verificar se o destino tem centro de pesquisa
        if (!destino.hasCentroDePesquisa()) {
            throw new IllegalArgumentException("Cidade de destino deve ter um centro de pesquisa para usar Ponte Aérea.");
        }
        
        // Verificar se não está tentando se mover para a mesma cidade
        if (posicaoAtual.equals(destino)) {
            throw new IllegalArgumentException("Não é possível usar Ponte Aérea para a mesma cidade.");
        }
        
        // Executar o movimento
        jogador.setPosicao(destino);
    }

    @Override
    public String getNome() {
        return "Ponte Aérea";
    }

    @Override
    public String getDescricao() {
        return "Se o jogador está em um centro de pesquisa, pode ir para outra cidade com centro de pesquisa.";
    }
}
