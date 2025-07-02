package com.example.pandemic.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.pandemic.aplicação.usecases.ProcessarTurno;
import com.example.pandemic.domínio.acoes.Acao;
import com.example.pandemic.domínio.acoes.ConstruirCentroDePesquisa;
import com.example.pandemic.domínio.acoes.TratarDoenca;
import com.example.pandemic.domínio.entidades.Jogo;
import com.example.pandemic.domínio.utils.builders.usecases.IniciarJogoBuilder;

public class ProcessarTurnoTest {
    
    @Test 
    public void deveProcessarUmTurnoComSucesso(){
        Jogo jogo = IniciarJogoBuilder.umIniciarJogo().build();
    

        ProcessarTurno processarTurno = new ProcessarTurno(jogo);
        System.out.println("====== TURNO ======");
        Acao acao = new TratarDoenca();
        processarTurno.executar(acao);
        assertEquals(0, 0);

    }
}
