package com.example.pandemic.domínio.utils;

import java.util.List;

import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.enums.Cor;

/**
 * Renderizador para exibir cidades de forma visual no terminal
 */
public class CityRenderer {
    
    private static final int CITY_WIDTH = 30;
    private static final int CITIES_PER_ROW = 2;
    
    public static String renderCitiesGrid(List<Cidade> cidades, List<Jogador> jogadores) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n").append(Cor.textoEmBranco("=== MAPA DAS CIDADES ===")).append("\n\n");
        
        // Renderizar cidades em grupos de 3 por linha
        for (int i = 0; i < cidades.size(); i += CITIES_PER_ROW) {
            int endIndex = Math.min(i + CITIES_PER_ROW, cidades.size());
            List<Cidade> cidadesLinha = cidades.subList(i, endIndex);
            renderCityRow(sb, cidadesLinha, jogadores);
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    private static void renderCityRow(StringBuilder sb, List<Cidade> cidades, List<Jogador> jogadores) {
        // Criar cards para cada cidade
        String[][] cards = new String[cidades.size()][];
        int maxHeight = 0;
        
        for (int i = 0; i < cidades.size(); i++) {
            cards[i] = createCityCard(cidades.get(i), jogadores);
            maxHeight = Math.max(maxHeight, cards[i].length);
        }
        
        // Renderizar linha por linha
        for (int line = 0; line < maxHeight; line++) {
            for (int card = 0; card < cards.length; card++) {
                if (line < cards[card].length) {
                    sb.append(cards[card][line]);
                } else {
                    // Preencher com espaços se o card for menor
                    sb.append(" ".repeat(CITY_WIDTH + 2));
                }
                if (card < cards.length - 1) {
                    sb.append("  "); // Espaço entre cards
                }
            }
            sb.append("\n");
        }
    }
    
    private static String[] createCityCard(Cidade cidade, List<Jogador> jogadores) {
        // Calcular quantas linhas precisamos (base + conexões)
        int numConexoes = cidade.getCidadesVizinhas() != null ? cidade.getCidadesVizinhas().size() : 0;
        int cardHeight = Math.max(9, 7 + numConexoes); // Mínimo 9 linhas (incluindo centro de pesquisa)
        
        String[] card = new String[cardHeight];
        int currentLine = 0;
        
        // Linha 0: Borda superior
        card[currentLine++] = "+" + "-".repeat(CITY_WIDTH) + "+";
        
        // Linha 1: Nome da cidade (centralizado)
        String nome = truncateText(cidade.getNome(), CITY_WIDTH - 2);
        card[currentLine++] = "|" + centerText(Cor.textoEmBranco(nome), CITY_WIDTH) + "|";
        
        // Linha 2: Separador
        card[currentLine++] = "|" + "-".repeat(CITY_WIDTH) + "|";
        
        // Linha 3: Doença
        String doencaText = getDoencaText(cidade);
        card[currentLine++] = "|" + padText(doencaText, CITY_WIDTH) + "|";
        
        // Linha 4: Intensidade (blocos coloridos)
        String intensidadeText = getIntensidadeText(cidade);
        card[currentLine++] = "|" + padText(intensidadeText, CITY_WIDTH) + "|";
        
        // Linha 5: Centro de Pesquisa
        String centroPesquisaText = getCentroPesquisaText(cidade);
        card[currentLine++] = "|" + padText(centroPesquisaText, CITY_WIDTH) + "|";
        
        // Linhas das conexões
        if (cidade.getCidadesVizinhas() != null && !cidade.getCidadesVizinhas().isEmpty()) {
            for (Cidade vizinha : cidade.getCidadesVizinhas()) {
                String conexaoText = "-> " + vizinha.getNome();
                card[currentLine++] = "|" + padText(conexaoText, CITY_WIDTH) + "|";
            }
        } else {
            card[currentLine++] = "|" + padText("-> Nenhuma conexao", CITY_WIDTH) + "|";
        }
        
        // Preencher linhas restantes se necessário
        while (currentLine < cardHeight - 1) {
            card[currentLine++] = "|" + " ".repeat(CITY_WIDTH) + "|";
        }
        
        // Última linha: Borda inferior
        card[currentLine] = "+" + "-".repeat(CITY_WIDTH) + "+";
        
        return card;
    }
    
    private static String getDoencaText(Cidade cidade) {
        if (cidade.getDoenca() == null) {
            return "Sem doenca";
        }
        
        String nomeDoenca = truncateText(cidade.getDoenca().getNome(), 15);
        if (cidade.getDoenca().getCor() != null) {
            return cidade.getDoenca().getCor().colorir(nomeDoenca);
        }
        return nomeDoenca;
    }
    
    private static String getIntensidadeText(Cidade cidade) {
        if (cidade.getDoenca() == null || cidade.getIntensidadeDaDoenca() == 0) {
            return "Intensidade: 0";
        }
        
        StringBuilder blocos = new StringBuilder();
        int intensidade = Math.min(cidade.getIntensidadeDaDoenca(), 10); // Máximo 10 blocos
        
        for (int i = 0; i < intensidade; i++) {
            if (i > 0) {
                blocos.append(" "); // Espaço entre blocos
            }
            
            if (cidade.getDoenca().getCor() != null) {
                blocos.append(cidade.getDoenca().getCor().colorir("#"));
            } else {
                blocos.append("#");
            }
        }
        
        return "Int: " + blocos.toString() + " (" + cidade.getIntensidadeDaDoenca() + ")";
    }
    
    private static String getCentroPesquisaText(Cidade cidade) {
        if (cidade.hasCentroDePesquisa()) {
            return Cor.textoEmBranco("Centro: [SIM]");
        } else {
            return "Centro: [NAO]";
        }
    }
    
    private static String truncateText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
    
    private static String centerText(String text, int width) {
        // Remove códigos ANSI para calcular o comprimento real
        String cleanText = text.replaceAll("\u001B\\[[;\\d]*m", "");
        
        if (cleanText.length() >= width) {
            return text.substring(0, width);
        }
        
        int padding = (width - cleanText.length()) / 2;
        int rightPadding = width - cleanText.length() - padding;
        return " ".repeat(padding) + text + " ".repeat(rightPadding);
    }
    
    private static String padText(String text, int width) {
        // Remove códigos ANSI para calcular o comprimento real
        String cleanText = text.replaceAll("\u001B\\[[;\\d]*m", "");
        
        if (cleanText.length() >= width) {
            return text.substring(0, width);
        }
        
        return " " + text + " ".repeat(width - cleanText.length() - 1);
    }
}
