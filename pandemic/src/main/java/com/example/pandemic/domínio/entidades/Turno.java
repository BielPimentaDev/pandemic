package com.example.pandemic.domínio.entidades;

public class Turno {
    private Jogador jogador;
    private Jogo jogo;
    public Turno(Jogador jogador, Jogo jogo){
        this.jogador = jogador;
        this.jogo = jogo;
        iniciarTurno();
    }

    public void iniciarTurno(){
        verificarStatusJogo();
        acoesDoJogador();
        espalharInfeccao();
        jogadorCompraCartas();
        verificarNumeroDeCartasDoJogador();
    }

    public void verificarStatusJogo(){
        System.out.println("Verificando o status do jogo...");
    }

  

    public void acoesDoJogador(){
        System.out.println("Iniciando ações do jogador...");
        // Aqui você pode implementar a lógica para as ações do jogador, como mover, tratar doenças, etc.
        // Por exemplo:
        // jogador.executarAcao(new MoverAcao());
        // jogador.executarAcao(new TratarDoencaAcao());

    }
    public void espalharInfeccao(){
        System.out.println("Espalhando infecção...");
        // Aqui você pode implementar a lógica para espalhar a infecção no tabuleiro.
        // Por exemplo, chamar o método de espalhar infecção do tabuleiro:
        // jogo.getTabuleiro().espalharInfeccao();

    }
    public void jogadorCompraCartas(){  
        System.out.println("Jogador comprando cartas...");
        // Aqui você pode implementar a lógica para o jogador comprar cartas.
        // Por exemplo, chamar o método de compra de cartas do jogador:
        // jogador.comprarCarta(carta);


    }

    public void verificarNumeroDeCartasDoJogador(){
        System.out.println("Verificando o número de cartas do jogador...");
        // Aqui você pode implementar a lógica para verificar se o jogador tem mais de 7 cartas.
        // Se sim, ele deve descartar uma carta.
        // Por exemplo:
        // if (jogador.getCartas().size() > 7) {
        //     jogador.descartarUmaCarta(carta);
        // }

    }
}
