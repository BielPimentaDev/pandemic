package com.example.pandemic.infrastructure.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.example.pandemic.aplicação.usecases.ProcessarTurno;
import com.example.pandemic.domínio.acoes.Acao;
import com.example.pandemic.domínio.acoes.movimento.MovimentoAutomovel;
import com.example.pandemic.domínio.acoes.movimento.PonteAerea;
import com.example.pandemic.domínio.acoes.movimento.VooDireto;
import com.example.pandemic.domínio.acoes.movimento.VooFretado;
import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.jogo.Jogo;
import com.example.pandemic.infrastructure.services.AcaoManager;
import com.example.pandemic.infrastructure.ui.GameUI;

/**
 * Controlador principal do fluxo do jogo
 */
public class GameController {
    
    public static void executarLoopPrincipal(Jogo jogo) {
        GameUI.exibirMensagem("info", "Iniciando loop principal do jogo...");
        GameUI.exibirMensagem("info", "Digite 'sair' a qualquer momento para encerrar o jogo.");
        System.out.println("=================================================================");
        
        while (jogo.isJogoAtivo()) {
            // Limpar tela (simulado com quebras de linha)
            GameUI.limparTela();
            
            // Mostrar estado completo do jogo
            GameUI.exibirEstadoCompleto(jogo);
            
            // Mostrar informações do jogador atual
            GameUI.exibirInformacoesJogadorAtual(jogo);
            
            // Menu de ações disponíveis
            GameUI.exibirMenuAcoesDisponiveis(jogo);
            
            String entrada = GameUI.obterEscolhaJogador();
            
            if (entrada.equals("sair") || entrada.equals("x")) {
                GameUI.exibirMensagem("info", "Encerrando jogo...");
                break;
            }
            
            processarEscolhaJogador(entrada, jogo);
            
            // Verificar condições de fim de jogo
            if (jogo.isJogoVitoria()) {
                System.out.println();
                System.out.println("=== PARABENS! VOCES VENCERAM! ===");
                System.out.println("Todas as curas foram descobertas!");
                break;
            }
            
            if (!jogo.isJogoAtivo()) {
                System.out.println();
                System.out.println("=== GAME OVER! VOCES PERDERAM! ===");
                System.out.println("O mundo foi dominado pela pandemia...");
                break;
            }
            
            // Pausa para o jogador ler as informações
            if (!entrada.equals("s") && !entrada.equals("status")) {
                GameUI.aguardarContinuacao();
            }
        }
        
        System.out.println();
        System.out.println("[RESULTADO] Estado final do jogo:");
        System.out.println(jogo);
    }
    
    private static void processarEscolhaJogador(String entrada, Jogo jogo) {
        try {
            if (entrada.equals("s") || entrada.equals("status")) {
                // Não faz nada, o loop mostrará o status novamente
                return;
            }
            
            // Tentar converter para número da ação
            try {
                int numeroAcao = Integer.parseInt(entrada);
                
                // Construir lista completa de ações para corresponder aos números
                List<Acao> todasAcoes = new ArrayList<>();
                todasAcoes.addAll(AcaoManager.obterAcoesBasicas());
                todasAcoes.addAll(AcaoManager.obterAcoesMovimento(jogo));
                
                if (numeroAcao >= 1 && numeroAcao <= todasAcoes.size()) {
                    Acao acaoSelecionada = todasAcoes.get(numeroAcao - 1);
                    
                    // Verificar se é uma ação genérica de movimento
                    if (acaoSelecionada instanceof AcaoManager.MovimentoGenerico) {
                        processarAcaoMovimentoGenerica(acaoSelecionada.getNome(), jogo);
                    } else {
                        executarAcao(acaoSelecionada, jogo);
                    }
                } else {
                    GameUI.exibirMensagem("aviso", "Numero de acao invalido!");
                }
            } catch (NumberFormatException e) {
                GameUI.exibirMensagem("aviso", "Entrada invalida! Digite um numero, 's' para status ou 'x' para sair.");
            }
        } catch (Exception e) {
            GameUI.exibirMensagem("erro", "Erro ao processar escolha: " + e.getMessage());
        }
    }
    
    private static void processarAcaoMovimentoGenerica(String tipoMovimento, Jogo jogo) {
        // Verificar se o movimento está disponível
        if (!AcaoManager.isMovimentoDisponivelPorNome(tipoMovimento, jogo)) {
            GameUI.exibirMensagem("aviso", "Acao " + tipoMovimento + " nao esta disponivel no momento!");
            exibirMotivoIndisponibilidade(tipoMovimento, jogo);
            return;
        }
        
        // Obter ações específicas para o tipo de movimento
        List<Acao> acoesEspecificas = AcaoManager.obterAcoesMovimentoEspecificas(tipoMovimento, jogo);
        
        if (acoesEspecificas.isEmpty()) {
            GameUI.exibirMensagem("aviso", "Nenhuma opcao disponivel para " + tipoMovimento + "!");
            return;
        }
        
        // Se há apenas uma opção, executar diretamente
        if (acoesEspecificas.size() == 1) {
            executarAcao(acoesEspecificas.get(0), jogo);
            return;
        }
        
        // Se há múltiplas opções, permitir que o jogador escolha
        Acao acaoEscolhida = selecionarDestino(tipoMovimento, acoesEspecificas, jogo);
        if (acaoEscolhida != null) {
            executarAcao(acaoEscolhida, jogo);
        }
    }
    
    private static Acao selecionarDestino(String tipoMovimento, List<Acao> acoesDisponiveis, Jogo jogo) {
        System.out.println();
        GameUI.exibirMensagem("info", "Escolha o destino para " + tipoMovimento + ":");
        System.out.println("=================================================================");
        
        // Exibir opções disponíveis
        for (int i = 0; i < acoesDisponiveis.size(); i++) {
            Acao acao = acoesDisponiveis.get(i);
            String destino = obterNomeDestino(acao);
            String informacaoAdicional = obterInformacaoAdicional(acao, jogo);
            
            System.out.println((i + 1) + ". " + destino + informacaoAdicional);
        }
        System.out.println("0. Cancelar");
        System.out.println("=================================================================");
        
        // Obter escolha do jogador usando o GameUI
        while (true) {
            String entrada = GameUI.obterEscolhaJogador("Digite o numero da opcao (0 para cancelar): ");
            try {
                int escolha = Integer.parseInt(entrada);
                
                if (escolha == 0) {
                    GameUI.exibirMensagem("info", "Acao cancelada.");
                    return null;
                }
                
                if (escolha >= 1 && escolha <= acoesDisponiveis.size()) {
                    return acoesDisponiveis.get(escolha - 1);
                } else {
                    GameUI.exibirMensagem("aviso", "Opcao invalida! Digite um numero entre 0 e " + acoesDisponiveis.size());
                }
            } catch (NumberFormatException e) {
                GameUI.exibirMensagem("aviso", "Entrada invalida! Digite apenas numeros.");
            }
        }
    }
    
    private static String obterNomeDestino(Acao acao) {
        if (acao instanceof MovimentoAutomovel) {
            // Usar reflexão ou método para obter o destino
            try {
                java.lang.reflect.Field field = acao.getClass().getDeclaredField("destino");
                field.setAccessible(true);
                Cidade destino = (Cidade) field.get(acao);
                return destino.getNome();
            } catch (Exception e) {
                return "Cidade vizinha";
            }
        }
        
        if (acao instanceof VooDireto) {
            try {
                java.lang.reflect.Field field = acao.getClass().getDeclaredField("destino");
                field.setAccessible(true);
                Cidade destino = (Cidade) field.get(acao);
                return destino.getNome();
            } catch (Exception e) {
                return "Cidade (Voo Direto)";
            }
        }
        
        if (acao instanceof VooFretado) {
            try {
                java.lang.reflect.Field field = acao.getClass().getDeclaredField("destino");
                field.setAccessible(true);
                Cidade destino = (Cidade) field.get(acao);
                return destino.getNome();
            } catch (Exception e) {
                return "Cidade (Voo Fretado)";
            }
        }
        
        if (acao instanceof PonteAerea) {
            try {
                java.lang.reflect.Field field = acao.getClass().getDeclaredField("destino");
                field.setAccessible(true);
                Cidade destino = (Cidade) field.get(acao);
                return destino.getNome();
            } catch (Exception e) {
                return "Cidade com Centro de Pesquisa";
            }
        }
        
        return acao.getNome();
    }
    
    private static String obterInformacaoAdicional(Acao acao, Jogo jogo) {
        Jogador jogadorAtual = jogo.getJogadorAtual();
        
        if (acao instanceof VooDireto) {
            try {
                java.lang.reflect.Field field = acao.getClass().getDeclaredField("destino");
                field.setAccessible(true);
                Cidade destino = (Cidade) field.get(acao);
                
                // Verificar se o jogador tem a carta necessária
                boolean temCarta = jogadorAtual.getCartas().stream()
                    .anyMatch(carta -> carta.getNome().equals(destino.getNome()));
                
                return temCarta ? " (Tem carta)" : " (NAO tem carta - acao falhara)";
            } catch (Exception e) {
                return "";
            }
        }
        
        if (acao instanceof VooFretado) {
            try {
                // Verificar se o jogador tem a carta da cidade atual
                Cidade posicaoAtual = jogadorAtual.getPosicao();
                boolean temCarta = posicaoAtual != null && jogadorAtual.getCartas().stream()
                    .anyMatch(carta -> carta.getNome().equals(posicaoAtual.getNome()));
                
                return temCarta ? " (Tem carta da cidade atual)" : " (NAO tem carta da cidade atual - acao falhara)";
            } catch (Exception e) {
                return "";
            }
        }
        
        if (acao instanceof PonteAerea) {
            try {
                java.lang.reflect.Field field = acao.getClass().getDeclaredField("destino");
                field.setAccessible(true);
                Cidade destino = (Cidade) field.get(acao);
                Cidade posicaoAtual = jogadorAtual.getPosicao();
                
                boolean podeUsar = posicaoAtual != null && posicaoAtual.hasCentroDePesquisa() && 
                                 destino.hasCentroDePesquisa();
                
                return podeUsar ? " (Ambas tem centro de pesquisa)" : " (Falta centro de pesquisa - acao falhara)";
            } catch (Exception e) {
                return "";
            }
        }
        
        return "";
    }
    
    private static void exibirMotivoIndisponibilidade(String tipoMovimento, Jogo jogo) {
        switch (tipoMovimento) {
            case "Movimento Automóvel":
                GameUI.exibirMensagem("info", "Motivo: Nao ha cidades vizinhas disponiveis.");
                break;
            case "Voo Direto":
                GameUI.exibirMensagem("info", "Motivo: Voce nao possui cartas de cidade na mao.");
                break;
            case "Voo Fretado":
                GameUI.exibirMensagem("info", "Motivo: Voce nao possui a carta da cidade atual.");
                break;
            case "Ponte Aérea":
                GameUI.exibirMensagem("info", "Motivo: Voce nao esta em uma cidade com centro de pesquisa.");
                break;
        }
    }
    
    private static void executarAcao(Acao acao, Jogo jogo) {
        if (jogo.getAcoesRestantes() <= 0) {
            GameUI.exibirMensagem("aviso", "Voce nao tem acoes restantes neste turno!");
            return;
        }
        
        try {
            GameUI.exibirMensagem("info", "Executando: " + acao.getNome());
            ProcessarTurno processarTurno = new ProcessarTurno(jogo);
            processarTurno.executar(acao);
            GameUI.exibirMensagem("sucesso", acao.getNome() + " executada com sucesso!");
        } catch (Exception e) {
            GameUI.exibirMensagem("erro", "Erro ao executar " + acao.getNome() + ": " + e.getMessage());
        }
    }
}
