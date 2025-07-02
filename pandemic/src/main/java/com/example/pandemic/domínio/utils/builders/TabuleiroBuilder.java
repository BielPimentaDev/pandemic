package com.example.pandemic.domínio.utils.builders;

import java.util.List;

import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Doenca;
import com.example.pandemic.domínio.entidades.Tabuleiro;

public class TabuleiroBuilder {
    // Implementação do TabuleiroBuilder
    // Este builder pode ser usado para criar instâncias de Tabuleiro com configurações específicas
    // como cidades, doenças, jogadores, etc.

    Cidade cidade1 = CidadeBuilder.umaCidade().comNome("Rio de Janeiro").build();
    Cidade cidade2 = CidadeBuilder.umaCidade().comNome("Sao Paulo").build();
    Cidade cidade3 = CidadeBuilder.umaCidade().comNome("Minas Gerais").build();
    Cidade cidade4 = CidadeBuilder.umaCidade().comNome("Espirito Santo").build();
    Cidade cidade5 = CidadeBuilder.umaCidade().comNome("Maranhão").build();
    Cidade cidade6 = CidadeBuilder.umaCidade().comNome("Goias").build();
    Cidade cidade7 = CidadeBuilder.umaCidade().comNome("Salvador").build();
    Cidade cidade8 = CidadeBuilder.umaCidade().comNome("Rio Grande do Sul").build();
    

    // Doenças com cores específicas do Pandemic
    Doenca doenca1 = DoencaBuilder.umaDoenca().comNome("Variola").azul().comIntensidade(1).build();
    Doenca doenca2 = DoencaBuilder.umaDoenca().comNome("Covid").amarela().comIntensidade(1).build();
    Doenca doenca3 = DoencaBuilder.umaDoenca().comNome("Ebola").vermelha().comIntensidade(1).build();
    Doenca doenca4 = DoencaBuilder.umaDoenca().comNome("Peste").preta().comIntensidade(1).build();

    public static TabuleiroBuilder umTabuleiro() {
        return new TabuleiroBuilder();
    }

    public List<Cidade> gerarCidades() {
        cidade1.setDoenca(doenca1);
        cidade2.setDoenca(doenca2);
        cidade3.setDoenca(doenca3);
        cidade4.setDoenca(doenca4);
        cidade5.setDoenca(doenca1);
        cidade6.setDoenca(doenca2);
        cidade1.aumentarIntensidadeDaDoenca();
        cidade2.aumentarIntensidadeDaDoenca();
        cidade3.aumentarIntensidadeDaDoenca();
        cidade3.aumentarIntensidadeDaDoenca();
        cidade1.aumentarIntensidadeDaDoenca();
        cidade2.aumentarIntensidadeDaDoenca();
        cidade3.aumentarIntensidadeDaDoenca();
        cidade3.aumentarIntensidadeDaDoenca();
        

        cidade1.setCidadesVizinhas(List.of(cidade2, cidade3));
        cidade2.setCidadesVizinhas(List.of(cidade1, cidade3));
        cidade3.setCidadesVizinhas(List.of(cidade1, cidade2, cidade4));
        cidade4.setCidadesVizinhas(List.of(cidade3));

        return List.of(cidade1, cidade2, cidade3, cidade4, cidade5, cidade6, cidade7, cidade8);
      
    }


    
    
    public TabuleiroBuilder comCidades( /* parâmetros */) {
        // Lógica para adicionar cidades
        return this;
    }

    public TabuleiroBuilder comDoencas(/* parâmetros */) {
        // Lógica para adicionar doenças
        return this;
    }

    public TabuleiroBuilder comJogadores(/* parâmetros */) {
        // Lógica para adicionar jogadores
        return this;
    }

    public Tabuleiro build() {
        // Lógica para construir e retornar a instância de Tabuleiro
        return new Tabuleiro(gerarCidades(),List.of(doenca1, doenca2, doenca3) );
    }
}
