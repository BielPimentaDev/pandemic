package com.example.pandemic.domínio.entidades;

import java.util.List;

import lombok.Data;

@Data
public class Tabuleiro {
    private  List<Cidade> cidades;
    private List<Doenca> doencas;
    private List<Jogador> jogadores;

    public Tabuleiro(List<Cidade> cidades, List<Doenca> doencas){
        this.cidades = cidades;
        this.doencas = doencas;
      
    }

    public void espalharInfeccao() {
        for (Cidade cidade : cidades) {
            if(cidade.getIntensidadeDaDoenca() < 1){
                continue;
            }
            if ( cidade.getIntensidadeDaDoenca() < 3 ) {
                cidade.aumentarIntensidadeDaDoenca();
                continue;
            }
           
            infectarCidadesVizinhas(cidade);
        }
    }

    public void infectarCidadesVizinhas(Cidade cidade){     
        for ( Cidade cidadeVizinha : cidade.getCidadesVizinhas()){
            if(cidadeVizinha.getIntensidadeDaDoenca() < 3){
                cidadeVizinha.aumentarIntensidadeDaDoenca();
            }
            }
        }

    public List<Jogador> getJogadores() {
        return jogadores;
    }   

    public List<Cidade> getCidades() {
        return cidades;
    }
    public List<Doenca> getDoencas() {
        return doencas;
    }
}
