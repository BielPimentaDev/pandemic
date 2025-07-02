package com.example.pandemic.domínio.acoes;

import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.cartas.CartaCidade;

public class ConstruirCentroDePesquisa implements Acao {
    @Override
    public void executar(Jogador jogador) {
        Cidade cidade = jogador.getPosicao();
        
        
        if (cidade.hasCentroDePesquisa()) {
            throw new IllegalStateException("Já existe um centro de pesquisa nesta cidade.");
        }
        
     
        boolean cartaEncontrada = false;
        CartaCidade cartaParaRemover = null;
        
        for (CartaCidade carta: jogador.getCartas()) {
            if (carta.getNome().equals(cidade.getNome())) {
                cartaEncontrada = true;
                cartaParaRemover = carta;
                break;
            }
        }
        
        if (!cartaEncontrada) {
            throw new IllegalStateException("Você não possui a carta da cidade onde deseja construir o centro de pesquisa.");
        }
        
       
        jogador.getCartas().remove(cartaParaRemover);
        cidade.setCentroDePesquisa();
    }
    
    @Override
    public String getNome() {
        return "Construir Centro de Pesquisa";
    }

    @Override
    public String getDescricao() {
        return "Constrói um centro de pesquisa na cidade atual descartando a carta da cidade.";
    }
}
