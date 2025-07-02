package com.example.pandemic.domínio.acoes;

import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.cartas.CartaCidade;

public class DescobrirUmaCura implements Acao {
    @Override
    public void executar(Jogador jogador) {
        Cidade cidade = jogador.getPosicao();

        if (!cidade.hasCentroDePesquisa()) {
            throw new IllegalStateException("Não é possível descobrir uma cura sem um centro de pesquisa na cidade atual.");
        }
        
        String corDaDoenca = cidade.getDoenca().getCor().name();
        
        if(verificarQuantidadeDeCartasParaCura(jogador, cidade) >= 5) {
            cidade.getDoenca().setCuraDescoberta(true);
            removeCartasDoJogador(jogador, corDaDoenca);
        } else {
            throw new IllegalStateException("Você precisa de 5 cartas da mesma cor para descobrir uma cura.");
        }
    }

    private void removeCartasDoJogador(Jogador jogador, String corDaDoenca) {
        int quantidadeRemovida = 0;
        for (int i = 0; i < jogador.getCartas().size() && quantidadeRemovida < 5; i++) {
            CartaCidade carta = jogador.getCartas().get(i);
            if (carta.getCor() != null && carta.getCor().name().equals(corDaDoenca)) {
                jogador.getCartas().remove(i);
                quantidadeRemovida++;
                i--; // Ajusta o índice após remoção
            }
        }
    }

    private int verificarQuantidadeDeCartasParaCura(Jogador jogador, Cidade cidade) {
        // As verificações de null já foram feitas no método executar
        if (cidade.getDoenca() == null || cidade.getDoenca().getCor() == null) {
            return 0;
        }
        
        int quantidadeDeCartasDoJogador = 0;
        String corDaDoenca = cidade.getDoenca().getCor().name();
        for (CartaCidade carta : jogador.getCartas()) {
            if (carta.getCor() != null && carta.getCor().name().equals(corDaDoenca)) {
                quantidadeDeCartasDoJogador++;
            }
        }
        return quantidadeDeCartasDoJogador;    
    }

    @Override
    public String getNome() {
        return "Descobrir uma Cura";
    }

    @Override
    public String getDescricao() {
        return "Descobre a cura para uma doença descartando 5 cartas da mesma cor em um centro de pesquisa.";
    }
}
