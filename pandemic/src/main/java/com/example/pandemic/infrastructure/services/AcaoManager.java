package com.example.pandemic.infrastructure.services;

import java.util.ArrayList;
import java.util.List;

import com.example.pandemic.domínio.acoes.Acao;
import com.example.pandemic.domínio.acoes.ConstruirCentroDePesquisa;
import com.example.pandemic.domínio.acoes.DescobrirUmaCura;
import com.example.pandemic.domínio.acoes.TratarDoenca;
import com.example.pandemic.domínio.acoes.movimento.MovimentoAutomovel;
import com.example.pandemic.domínio.acoes.movimento.PonteAerea;
import com.example.pandemic.domínio.acoes.movimento.VooDireto;
import com.example.pandemic.domínio.acoes.movimento.VooFretado;
import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.jogo.Jogo;

/**
 * Serviço responsável por gerenciar as ações disponíveis para um jogador
 */
public class AcaoManager {
    
    /**
     * Retorna a lista de ações disponíveis para o jogador atual
     */
    public static List<Acao> obterAcoesDisponiveis(Jogo jogo) {
        List<Acao> acoes = new ArrayList<>();
        Jogador jogadorAtual = jogo.getJogadorAtual();
        Cidade posicaoAtual = jogadorAtual.getPosicao();
        
        // Ações básicas sempre disponíveis
        acoes.add(new TratarDoenca());
        acoes.add(new ConstruirCentroDePesquisa());
        acoes.add(new DescobrirUmaCura());
        
        // Ações de movimento
        if (posicaoAtual != null) {
            // Movimento automóvel para cidades vizinhas
            for (Cidade cidadeVizinha : posicaoAtual.getCidadesVizinhas()) {
                acoes.add(new MovimentoAutomovel(cidadeVizinha));
            }
            
            // Voo direto - pode ir para qualquer cidade se tiver a carta
            for (Cidade cidade : jogo.getTabuleiro().getCidades()) {
                if (!cidade.equals(posicaoAtual)) {
                    acoes.add(new VooDireto(cidade));
                }
            }
            
            // Voo fretado - pode ir para qualquer cidade se tiver a carta da cidade atual
            for (Cidade cidade : jogo.getTabuleiro().getCidades()) {
                if (!cidade.equals(posicaoAtual)) {
                    acoes.add(new VooFretado(cidade));
                }
            }
            
            // Ponte aérea - pode ir para qualquer cidade com centro de pesquisa
            for (Cidade cidade : jogo.getTabuleiro().getCidades()) {
                if (!cidade.equals(posicaoAtual) && cidade.hasCentroDePesquisa()) {
                    acoes.add(new PonteAerea(cidade));
                }
            }
        }
        
        return acoes;
    }
    
    /**
     * Filtra ações por categoria para melhor organização no menu
     */
    public static List<Acao> obterAcoesBasicas() {
        List<Acao> acoes = new ArrayList<>();
        acoes.add(new TratarDoenca());
        acoes.add(new ConstruirCentroDePesquisa());
        acoes.add(new DescobrirUmaCura());
        return acoes;
    }
    
    /**
     * Retorna todas as ações de movimento para o jogador (algumas podem estar indisponíveis)
     */
    public static List<Acao> obterAcoesMovimento(Jogo jogo) {
        List<Acao> acoesMovimento = new ArrayList<>();
        
        // Criamos ações genéricas de movimento para exibição
        acoesMovimento.add(new MovimentoGenerico("Movimento Automóvel", "Move-se para uma cidade vizinha conectada por estrada"));
        acoesMovimento.add(new MovimentoGenerico("Voo Direto", "Descarta a carta da cidade de destino para se mover até lá"));
        acoesMovimento.add(new MovimentoGenerico("Voo Fretado", "Descarta a carta da cidade atual para se mover para qualquer cidade"));
        acoesMovimento.add(new MovimentoGenerico("Ponte Aérea", "Move-se entre cidades com centros de pesquisa"));
        
        return acoesMovimento;
    }
    
    /**
     * Retorna uma lista de ações específicas de movimento baseada no tipo
     */
    public static List<Acao> obterAcoesMovimentoEspecificas(String tipoMovimento, Jogo jogo) {
        List<Acao> acoes = new ArrayList<>();
        Jogador jogadorAtual = jogo.getJogadorAtual();
        Cidade posicaoAtual = jogadorAtual.getPosicao();
        
        if (posicaoAtual == null) {
            return acoes;
        }
        
        switch (tipoMovimento) {
            case "Movimento Automóvel":
                for (Cidade cidadeVizinha : posicaoAtual.getCidadesVizinhas()) {
                    acoes.add(new MovimentoAutomovel(cidadeVizinha));
                }
                break;
                
            case "Voo Direto":
                for (Cidade cidade : jogo.getTabuleiro().getCidades()) {
                    if (!cidade.equals(posicaoAtual)) {
                        acoes.add(new VooDireto(cidade));
                    }
                }
                break;
                
            case "Voo Fretado":
                for (Cidade cidade : jogo.getTabuleiro().getCidades()) {
                    if (!cidade.equals(posicaoAtual)) {
                        acoes.add(new VooFretado(cidade));
                    }
                }
                break;
                
            case "Ponte Aérea":
                for (Cidade cidade : jogo.getTabuleiro().getCidades()) {
                    if (!cidade.equals(posicaoAtual) && cidade.hasCentroDePesquisa()) {
                        acoes.add(new PonteAerea(cidade));
                    }
                }
                break;
        }
        
        return acoes;
    }
    
    /**
     * Classe interna para representar ações de movimento genéricas no menu
     */
    public static class MovimentoGenerico implements Acao {
        private String nome;
        private String descricao;
        
        public MovimentoGenerico(String nome, String descricao) {
            this.nome = nome;
            this.descricao = descricao;
        }
        
        @Override
        public void executar(Jogador jogador) {
            // Esta classe só é usada para exibição, não para execução
            throw new UnsupportedOperationException("Ação genérica não pode ser executada diretamente");
        }
        
        @Override
        public String getNome() {
            return nome;
        }
        
        @Override
        public String getDescricao() {
            return descricao;
        }
    }
    
    /**
     * Verifica se uma ação específica está realmente disponível para execução
     */
    public static boolean isAcaoDisponivel(Acao acao, Jogo jogo) {
        try {
            // Para ações básicas, elas são sempre mostradas (mas podem falhar na execução)
            if (acao instanceof TratarDoenca || acao instanceof ConstruirCentroDePesquisa || acao instanceof DescobrirUmaCura) {
                return true;
            }
            
            // Para ações genéricas de movimento, verificar por nome
            if (acao instanceof MovimentoGenerico) {
                return isMovimentoDisponivelPorNome(acao.getNome(), jogo);
            }
            
            Jogador jogadorAtual = jogo.getJogadorAtual();
            Cidade posicaoAtual = jogadorAtual.getPosicao();
            
            if (acao instanceof MovimentoAutomovel) {
                // Para movimento automóvel, verificar se há cidades vizinhas
                return posicaoAtual != null && !posicaoAtual.getCidadesVizinhas().isEmpty();
            }
            
            if (acao instanceof VooDireto) {
                // Verificar se o jogador tem pelo menos uma carta de cidade
                return !jogadorAtual.getCartas().isEmpty();
            }
            
            if (acao instanceof VooFretado) {
                // Verificar se o jogador tem a carta da cidade atual
                return posicaoAtual != null && jogadorAtual.getCartas().stream()
                    .anyMatch(carta -> carta.getNome().equals(posicaoAtual.getNome()));
            }
            
            if (acao instanceof PonteAerea) {
                // Verificar se o jogador está em uma cidade com centro de pesquisa
                return posicaoAtual != null && posicaoAtual.hasCentroDePesquisa();
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Verifica disponibilidade de movimento por nome da ação
     */
    public static boolean isMovimentoDisponivelPorNome(String nomeAcao, Jogo jogo) {
        Jogador jogadorAtual = jogo.getJogadorAtual();
        Cidade posicaoAtual = jogadorAtual.getPosicao();
        
        switch (nomeAcao) {
            case "Movimento Automóvel":
                return posicaoAtual != null && !posicaoAtual.getCidadesVizinhas().isEmpty();
            case "Voo Direto":
                return !jogadorAtual.getCartas().isEmpty();
            case "Voo Fretado":
                return posicaoAtual != null && jogadorAtual.getCartas().stream()
                    .anyMatch(carta -> carta.getNome().equals(posicaoAtual.getNome()));
            case "Ponte Aérea":
                return posicaoAtual != null && posicaoAtual.hasCentroDePesquisa();
            default:
                return false;
        }
    }
}
