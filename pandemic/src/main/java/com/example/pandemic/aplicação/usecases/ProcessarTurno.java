package com.example.pandemic.aplicação.usecases;

import com.example.pandemic.domínio.acoes.Acao;
import com.example.pandemic.domínio.entidades.jogo.Jogo;

public class ProcessarTurno {
    private Jogo jogo;

    public ProcessarTurno(Jogo jogo) {
        this.jogo = jogo;
    }

    public void executar(Acao acao) {
        if (!jogo.temAcoesRestantes()) {
            throw new IllegalArgumentException("Número máximo de ações por turno excedido");
        }

        System.out.println("Jogador: " + jogo.getJogadorAtual().getNome() + " esta executando a ação");
        jogo.getJogadorAtual().executarAcao(acao);
        jogo.consumirAcao();
        
        if (!jogo.temAcoesRestantes()) {
            jogo.proximoTurno();
        }
    }
}
