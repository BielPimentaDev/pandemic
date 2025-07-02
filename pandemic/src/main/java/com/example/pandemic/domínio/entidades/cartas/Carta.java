package com.example.pandemic.domínio.entidades.cartas;

import com.example.pandemic.domínio.enums.Cor;

public interface Carta {
    String getNome();
    String getDescricao();
    String getTipo();
    Cor getCor();
}
