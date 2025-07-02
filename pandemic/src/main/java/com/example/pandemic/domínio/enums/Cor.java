package com.example.pandemic.domínio.enums;

public enum Cor {
    AZUL("Azul", "\u001B[34m"),      // Azul
    AMARELO("Amarelo", "\u001B[33m"), // Amarelo
    VERMELHO("Vermelho", "\u001B[31m"), // Vermelho
    PRETO("Preto", "\u001B[30m");     // Preto (cinza escuro no terminal)
    
    private final String nome;
    private final String codigoAnsi;
    public static final String RESET = "\u001B[0m"; // Reset cor
    public static final String BRANCO = "\u001B[37m"; // Texto branco
    
    Cor(String nome, String codigoAnsi) {
        this.nome = nome;
        this.codigoAnsi = codigoAnsi;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getCodigoAnsi() {
        return codigoAnsi;
    }
    
    /**
     * Formata um texto com a cor
     */
    public String colorir(String texto) {
        return codigoAnsi + texto + RESET;
    }
    
    /**
     * Formata um texto em branco
     */
    public static String textoEmBranco(String texto) {
        return BRANCO + texto + RESET;
    }
    
    /**
     * Emoji para representar a cor visualmente
     */
    public String getEmoji() {
        switch (this) {
            case AZUL: return "🔵";
            case AMARELO: return "🟡";
            case VERMELHO: return "🔴";
            case PRETO: return "⚫";
            default: return "⚪";
        }
    }
    
    /**
     * Símbolo ASCII para representar a cor (compatível com terminais Windows)
     */
    public String getSymbol() {
        switch (this) {
            case AZUL: return "[B]";
            case AMARELO: return "[A]";
            case VERMELHO: return "[V]";
            case PRETO: return "[P]";
            default: return "[?]";
        }
    }
    
    /**
     * Representação visual da cor (tenta emoji, fallback para ASCII)
     */
    public String getVisualIndicator() {
        // Para Windows, usar símbolos ASCII por padrão
        return getSymbol();
    }
    
    @Override
    public String toString() {
        return colorir(nome);
    }
}
