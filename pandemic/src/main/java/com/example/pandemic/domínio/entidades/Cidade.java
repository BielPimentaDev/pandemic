package com.example.pandemic.domínio.entidades;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;


@Data
public class Cidade {
    private String nome;
    private List<Cidade> cidadesVizinhas;
    private boolean temCentroDePesquisa;
    private Doenca doenca;
    private int intensidadeDaDoenca;

    public Cidade(String nome, List<Cidade> vizinhas, Doenca doenca) {
        this.nome = nome;
        this.cidadesVizinhas = vizinhas != null ? vizinhas : new ArrayList<>();
        this.temCentroDePesquisa = false;
        this.doenca = doenca;
        this.intensidadeDaDoenca = 0;
    }

    public Cidade(String nome, List<Cidade> vizinhas, Doenca doenca, boolean temCentroDePesquisa) {
        this.nome = nome;
        this.cidadesVizinhas = vizinhas != null ? vizinhas : new ArrayList<>();
        this.temCentroDePesquisa = temCentroDePesquisa;
        this.doenca = doenca;
        this.intensidadeDaDoenca = 0;
    }

    public Cidade(String nome, Doenca doenca) {
        this(nome, new ArrayList<>(), doenca);
    }

    public void zerarIntensidadeDaDoenca(){
        return;
    }

    public void diminuirIntensidadeDaDoenca(){
        if (intensidadeDaDoenca > 0) {
            this.intensidadeDaDoenca--;
        }
    }
    public void setCidadesVizinhas(List<Cidade> cidades){
        this.cidadesVizinhas = cidades;
    }

    public String getNome() {
        return nome;
    }

    public List<Cidade> getCidadesVizinhas() {
        return cidadesVizinhas;
    }

    public boolean hasCentroDePesquisa() {
        return temCentroDePesquisa;
    }

    public void setCentroDePesquisa(){
        this.temCentroDePesquisa = true;
    }

    public Doenca getDoenca(){
        return this.doenca;
    }

    public void aumentarIntensidadeDaDoenca(){
        this.intensidadeDaDoenca ++;
    }

    public int getIntensidadeDaDoenca(){
        return this.intensidadeDaDoenca;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Cidade)) return false;
        Cidade outra = (Cidade) obj;
        return this.nome.equalsIgnoreCase(outra.nome);
    }

    @Override
    public int hashCode() {
        return nome.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        List<String> nomesVizinhas = cidadesVizinhas != null ? 
            cidadesVizinhas.stream().map(Cidade::getNome).toList() : 
            new ArrayList<>();
        
        return "Cidade{" +
                "nome='" + nome + '\'' +
                ", cidadesVizinhas=" + nomesVizinhas +
                ", temCentroDePesquisa=" + temCentroDePesquisa +
                ", doenca=" + doenca +
                ", intensidadeDaDoenca=" + intensidadeDaDoenca +
                '}';
    }
}