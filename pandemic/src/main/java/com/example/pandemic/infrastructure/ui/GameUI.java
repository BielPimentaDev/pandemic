package com.example.pandemic.infrastructure.ui;

import java.util.List;
import java.util.Scanner;

import com.example.pandemic.domínio.acoes.Acao;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.jogo.Jogo;
import com.example.pandemic.infrastructure.services.AcaoManager;

/**
 * Classe responsável por gerenciar a interface do usuário no terminal
 */
public class GameUI {
    private static final Scanner scanner = new Scanner(System.in);
    
    // Códigos ANSI para cores no terminal
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_GRAY = "\u001B[90m";
    
    // Verificar se o terminal suporta cores ANSI
    private static boolean terminalSuportaCores() {
        String os = System.getProperty("os.name").toLowerCase();
        String term = System.getProperty("terminal.ansi");
        
        // Assumir que a maioria dos terminais modernos suporta cores
        return !os.contains("win") || term != null;
    }
    
    public static void exibirCabecalho() {
        if (terminalSuportaCores()) {
            System.out.println(ANSI_CYAN + "=================================================================" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "                    " + ANSI_YELLOW + "BEM-VINDO AO PANDEMIC" + ANSI_CYAN + "                       " + ANSI_RESET);
            System.out.println(ANSI_CYAN + "=================================================================" + ANSI_RESET);
        } else {
            System.out.println("=================================================================");
            System.out.println("                    BEM-VINDO AO PANDEMIC                       ");
            System.out.println("=================================================================");
        }
        System.out.println();
    }
    
    public static int obterNumeroJogadores() {
        int numeroJogadores = 0;
        boolean entradaValida = false;
        
        while (!entradaValida) {
            System.out.print("[JOGADORES] Quantos jogadores irao participar? (2-4): ");
            try {
                numeroJogadores = Integer.parseInt(scanner.nextLine().trim());
                if (numeroJogadores >= 2 && numeroJogadores <= 4) {
                    entradaValida = true;
                } else {
                    System.out.println("[AVISO] Numero invalido! Digite um numero entre 2 e 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[AVISO] Por favor, digite um numero valido!");
            }
        }
        
        return numeroJogadores;
    }
    
    public static String obterNomeJogador(int numeroJogador) {
        System.out.print("Digite o nome do jogador " + numeroJogador + ": ");
        String nome = scanner.nextLine().trim();
        
        while (nome.isEmpty()) {
            System.out.print("[AVISO] Nome nao pode estar vazio. Digite novamente: ");
            nome = scanner.nextLine().trim();
        }
        
        return nome;
    }
    
    public static void exibirMensagem(String tipo, String mensagem) {
        if (terminalSuportaCores()) {
            switch (tipo.toLowerCase()) {
                case "sucesso":
                    System.out.println(ANSI_GREEN + "[OK] " + mensagem + ANSI_RESET);
                    break;
                case "erro":
                    System.out.println(ANSI_RED + "[ERRO] " + mensagem + ANSI_RESET);
                    break;
                case "aviso":
                    System.out.println(ANSI_YELLOW + "[AVISO] " + mensagem + ANSI_RESET);
                    break;
                case "info":
                    System.out.println(ANSI_CYAN + "[INFO] " + mensagem + ANSI_RESET);
                    break;
                default:
                    System.out.println(mensagem);
            }
        } else {
            // Fallback para terminais sem suporte a cores
            switch (tipo.toLowerCase()) {
                case "sucesso":
                    System.out.println("[OK] " + mensagem);
                    break;
                case "erro":
                    System.out.println("[ERRO] " + mensagem);
                    break;
                case "aviso":
                    System.out.println("[AVISO] " + mensagem);
                    break;
                case "info":
                    System.out.println("[INFO] " + mensagem);
                    break;
                default:
                    System.out.println(mensagem);
            }
        }
    }
    
    public static void exibirEstadoCompleto(Jogo jogo) {
        System.out.println("=================================================================");
        System.out.println("                       ESTADO DO JOGO                           ");
        System.out.println("=================================================================");
        System.out.println(jogo);
        System.out.println("=================================================================");
    }
    
    public static void exibirInformacoesJogadorAtual(Jogo jogo) {
        Jogador jogadorAtual = jogo.getJogadorAtual();
        
        System.out.println();
        System.out.println("[TURNO] Turno " + (jogo.getTurnoAtual() + 1));
        System.out.println("[JOGADOR] Jogador da vez: " + jogadorAtual.getNome());
        System.out.println("[PERSONAGEM] " + jogadorAtual.getPersonagem());
        System.out.println("[POSICAO] Posicao atual: " + (jogadorAtual.getPosicao() != null ? 
            jogadorAtual.getPosicao().getNome() : "Sem posicao"));
        System.out.println("[ACOES] Acoes restantes: " + jogo.getAcoesRestantes());
        
        // Exibir cartas do jogador
        System.out.println();
        System.out.println("[CARTAS] Cartas na mao:");
        if (jogadorAtual.getCartas().isEmpty()) {
            System.out.println("   (Nenhuma carta)");
        } else {
            for (int i = 0; i < jogadorAtual.getCartas().size(); i++) {
                System.out.println("   " + (i + 1) + ". " + jogadorAtual.getCartas().get(i).getNome());
            }
        }
    }
    
    public static void exibirMenuAcoesDisponiveis(Jogo jogo) {
        System.out.println();
        System.out.println("[MENU] ACOES DISPONIVEIS (Acoes restantes: " + jogo.getAcoesRestantes() + ")");
        System.out.println("---------------------------------------------------------------------");
        
        // Agrupar ações por tipo
        List<Acao> acoesBasicas = AcaoManager.obterAcoesBasicas();
        List<Acao> acoesMovimento = AcaoManager.obterAcoesMovimento(jogo);
        
        System.out.println();
        System.out.println("=== ACOES BASICAS ===");
        int contador = 1;
        for (Acao acao : acoesBasicas) {
            boolean disponivel = AcaoManager.isAcaoDisponivel(acao, jogo);
            if (disponivel) {
                System.out.println(contador + ". " + acao.getNome());
                System.out.println("   > " + acao.getDescricao());
            } else {
                // Exibir em formato "desabilitado" (sem cor especial por limitações do terminal)
                System.out.println(contador + ". [INDISPONIVEL] " + acao.getNome());
                System.out.println("   > " + acao.getDescricao());
            }
            System.out.println();
            contador++;
        }
        
        if (!acoesMovimento.isEmpty()) {
            System.out.println("=== ACOES DE MOVIMENTO ===");
            for (Acao acao : acoesMovimento) {
                boolean disponivel = AcaoManager.isAcaoDisponivel(acao, jogo);
                if (disponivel) {
                    System.out.println(contador + ". " + acao.getNome());
                    System.out.println("   > " + acao.getDescricao());
                } else {
                    // Exibir em cinza se o terminal suporta cores, senão marcar como indisponível
                    if (terminalSuportaCores()) {
                        System.out.println(ANSI_GRAY + contador + ". " + acao.getNome() + ANSI_RESET);
                        System.out.println(ANSI_GRAY + "   > " + acao.getDescricao() + ANSI_RESET);
                        System.out.println(ANSI_GRAY + "   ! Esta acao nao esta disponivel no momento" + ANSI_RESET);
                    } else {
                        System.out.println(contador + ". [INDISPONIVEL] " + acao.getNome());
                        System.out.println("   > " + acao.getDescricao());
                        System.out.println("   ! Esta acao nao esta disponivel no momento");
                    }
                }
                System.out.println();
                contador++;
            }
        }
        
        // Opções especiais
        System.out.println("=== OPCOES ESPECIAIS ===");
        System.out.println("s. Ver status do jogo novamente");
        System.out.println("x. Sair do jogo");
    }
    
    public static String obterEscolhaJogador() {
        System.out.print("[INPUT] Escolha uma acao (numero ou 'sair'): ");
        return scanner.nextLine().trim().toLowerCase();
    }
    
    public static String obterEscolhaJogador(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    public static boolean confirmarPassarTurno(int acoesRestantes) {
        if (acoesRestantes > 0) {
            System.out.println("[AVISO] Voce ainda tem " + acoesRestantes + " acoes restantes!");
            System.out.print("Tem certeza que deseja passar o turno? (s/n): ");
            String confirmacao = scanner.nextLine().trim().toLowerCase();
            return confirmacao.equals("s") || confirmacao.equals("sim");
        }
        return true;
    }
    
    public static void aguardarContinuacao() {
        System.out.print("Pressione Enter para continuar...");
        scanner.nextLine();
    }
    
    public static void limparTela() {
        // Simula limpeza de tela com quebras de linha
        for (int i = 0; i < 3; i++) {
            System.out.println();
        }
    }
    
    public static void fecharScanner() {
        scanner.close();
    }
}
