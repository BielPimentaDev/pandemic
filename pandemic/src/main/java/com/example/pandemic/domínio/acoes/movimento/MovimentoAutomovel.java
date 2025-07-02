package com.example.pandemic.domínio.acoes.movimento;

import java.util.List;

import com.example.pandemic.domínio.acoes.Acao;
import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Jogador;



public class MovimentoAutomovel implements Acao {
    private Cidade destino;

    public MovimentoAutomovel(Cidade destino) {
        this.destino = destino;
    }

    @Override
    public void executar(Jogador jogador) {
        Cidade posicaoAtual = jogador.getPosicao();
        List<Cidade> cidadesVizinhas = posicaoAtual.getCidadesVizinhas();
        if (!cidadesVizinhas.contains(destino)) {
            throw new IllegalArgumentException("Não é possível viajar para uma cidade que não é vizinha.");
        }

        jogador.setPosicao(destino); 
    }

    @Override
    public String getNome() {
        return "Movimento Automóvel";
    }

    @Override
    public String getDescricao() {
        return "Move-se para uma cidade vizinha conectada por estrada.";
    }
}