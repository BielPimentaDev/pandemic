package com.example.pandemic.domínio.acoes;

import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.enums.Personagem;





public class TratarDoenca implements Acao {
    @Override
    public void executar(Jogador jogador) {
       Cidade cidade = jogador.getPosicao();
       if(jogador.getPersonagem() == Personagem.MEDICO){
            cidade.zerarIntensidadeDaDoenca();
            return;
       }

        cidade.diminuirIntensidadeDaDoenca();

    }
    
    @Override
    public String getNome() {
        return "Tratar Doença";
    }

    @Override
    public String getDescricao() {
        return "Remove um cubo de doença da cidade atual.";
    }
}


