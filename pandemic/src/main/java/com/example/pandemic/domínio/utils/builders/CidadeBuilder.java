package com.example.pandemic.domínio.utils.builders;

import java.util.List;

import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Doenca;

public class CidadeBuilder {
    private String nome;
    private List<Cidade> cidadesVizinhas;
    private boolean temCentroDePesquisa;
    private Doenca doenca;
    private int intensidadeDaDoenca;
    
    public CidadeBuilder() {
        this.nome = "Cidade Teste";
        this.temCentroDePesquisa = false;
        this.intensidadeDaDoenca = 0;

    }

    public static CidadeBuilder umaCidade() {
        return new CidadeBuilder();
    }

    public CidadeBuilder comNome(String nome) {
        this.nome = nome;
        return this;
    }

    public CidadeBuilder comDoenca(Doenca doenca) {
        this.doenca = doenca;
        return this;
    }

    public CidadeBuilder comCentroDePesquisa() {
        this.temCentroDePesquisa = true;
        return this;
    }

    public CidadeBuilder comIntensidadeDaDoenca(int intensidade) {
        this.intensidadeDaDoenca = intensidade;
        return this;
    }

    public Cidade build() {
        return new Cidade(nome, null, doenca, temCentroDePesquisa); 
    }
    
}
