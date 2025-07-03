package com.example.pandemic.domínio.utils;

import java.util.List;

import com.example.pandemic.domínio.entidades.Doenca;
import com.example.pandemic.domínio.enums.Cor;

/**
 * Renderizador para exibir tabela de doenças
 */
public class DiseaseTableRenderer {
    
    private static final int NOME_WIDTH = 15;
    private static final int COR_WIDTH = 10;
    private static final int INTENSIDADE_WIDTH = 15;
    private static final int CURA_WIDTH = 8;
    private static final int TOTAL_WIDTH = NOME_WIDTH + COR_WIDTH + INTENSIDADE_WIDTH + CURA_WIDTH + 7; // +7 para separadores
    
    public static String renderDiseaseTable(List<Doenca> doencas) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n").append(Cor.textoEmBranco("=== TABELA DE DOENCAS ===")).append("\n\n");
        
        // Cabeçalho da tabela
        sb.append("+").append("-".repeat(TOTAL_WIDTH)).append("+").append("\n");
        sb.append("|").append(centerText("NOME", NOME_WIDTH))
          .append("|").append(centerText("COR", COR_WIDTH))
          .append("|").append(centerText("INTENSIDADE", INTENSIDADE_WIDTH))
          .append("|").append(centerText("CURA", CURA_WIDTH))
          .append("|").append("\n");
        sb.append("+").append("-".repeat(TOTAL_WIDTH)).append("+").append("\n");
        
        // Linhas das doenças
        if (doencas.isEmpty()) {
            String semDoencasText = centerText("Nenhuma doenca no jogo", TOTAL_WIDTH);
            sb.append("|").append(semDoencasText).append("|").append("\n");
        } else {
            for (Doenca doenca : doencas) {
                renderDiseaseRow(sb, doenca);
            }
        }
        
        // Rodapé da tabela
        sb.append("+").append("-".repeat(TOTAL_WIDTH)).append("+").append("\n");
        
        return sb.toString();
    }
    
    private static void renderDiseaseRow(StringBuilder sb, Doenca doenca) {
        // Nome da doença
        String nome = truncateText(doenca.getNome(), NOME_WIDTH - 2);
        String nomeCell = padText(nome, NOME_WIDTH);
        
        // Cor da doença
        String corText = doenca.getCor() != null ? 
            truncateText(doenca.getCor().getNome(), COR_WIDTH - 2) : "N/A";
        if (doenca.getCor() != null) {
            corText = doenca.getCor().colorir(corText);
        }
        String corCell = padText(corText, COR_WIDTH);
        
        // Intensidade com blocos visuais
        String intensidadeText = getIntensidadeVisual(doenca);
        String intensidadeCell = padText(intensidadeText, INTENSIDADE_WIDTH);
        
        // Status da cura
        String curaText = doenca.isCuraDescoberta() ? 
            Cor.textoEmBranco("[SIM]") : "[NAO]";
        String curaCell = padText(curaText, CURA_WIDTH);
        
        sb.append("|").append(nomeCell)
          .append("|").append(corCell)
          .append("|").append(intensidadeCell)
          .append("|").append(curaCell)
          .append("|").append("\n");
    }
    
    private static String getIntensidadeVisual(Doenca doenca) {
        if (doenca.getIntensidade() == 0) {
            return "0";
        }
        
        StringBuilder blocos = new StringBuilder();
        int intensidade = Math.min(doenca.getIntensidade(), 5); // Máximo 5 blocos para caber na coluna
        
        for (int i = 0; i < intensidade; i++) {
            if (doenca.getCor() != null) {
                blocos.append(doenca.getCor().colorir("#"));
            } else {
                blocos.append("#");
            }
        }
        
        return blocos.toString() + " (" + doenca.getIntensidade() + ")";
    }
    
    private static String truncateText(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
    
    private static String centerText(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        
        int padding = (width - text.length()) / 2;
        int rightPadding = width - text.length() - padding;
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
