package com.example.pandemic.domínio.acoes;
import com.example.pandemic.domínio.entidades.Jogador;

public interface Acao {
    void executar(Jogador jogador);
    String getNome();
    String getDescricao();
}
