package com.example.pandemic.domínio.utils;

import java.util.*;
import java.util.stream.Collectors;

import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.Tabuleiro;
import com.example.pandemic.domínio.enums.Cor;

/**
 * Utility class for rendering the game board in a visual format
 */
public class BoardRenderer {
    
    private static final int CITY_BOX_WIDTH = 15;
    private static final int MAX_CITIES_PER_ROW = 4;
    private static final String HORIZONTAL_LINE = "─";
    private static final String CONNECTION_LINE = "══";
    
    /**
     * Renders the board in a visual layout showing cities and connections
     */
    public static String renderBoard(Tabuleiro tabuleiro, List<Jogador> jogadores) {
        StringBuilder sb = new StringBuilder();
        
        List<Cidade> cidades = tabuleiro.getCidades();
        if (cidades.isEmpty()) {
            return "🏙️ Nenhuma cidade no tabuleiro";
        }
        
        // Group cities by rows for better visualization
        List<List<Cidade>> cityRows = groupCitiesInRows(cidades);
        
        sb.append("🌍 ").append(Cor.textoEmBranco("MAPA DO MUNDO")).append("\n");
        sb.append("═".repeat(80)).append("\n\n");
        
        // Render each row of cities
        for (int rowIndex = 0; rowIndex < cityRows.size(); rowIndex++) {
            List<Cidade> row = cityRows.get(rowIndex);
            renderCityRow(sb, row, jogadores);
            
            // Add connections between rows if not the last row
            if (rowIndex < cityRows.size() - 1) {
                renderVerticalConnections(sb, row, cityRows.get(rowIndex + 1));
            }
        }
        
        sb.append("\n═".repeat(80));
        return sb.toString();
    }
    
    /**
     * Groups cities into rows for better visual layout
     */
    private static List<List<Cidade>> groupCitiesInRows(List<Cidade> cidades) {
        List<List<Cidade>> rows = new ArrayList<>();
        
        for (int i = 0; i < cidades.size(); i += MAX_CITIES_PER_ROW) {
            int end = Math.min(i + MAX_CITIES_PER_ROW, cidades.size());
            rows.add(new ArrayList<>(cidades.subList(i, end)));
        }
        
        return rows;
    }
    
    /**
     * Renders a row of cities with their details
     */
    private static void renderCityRow(StringBuilder sb, List<Cidade> cities, List<Jogador> jogadores) {
        // Render city boxes
        renderCityBoxes(sb, cities, jogadores);
        
        // Render horizontal connections
        renderHorizontalConnections(sb, cities);
        
        sb.append("\n");
    }
    
    /**
     * Renders the city boxes with information
     */
    private static void renderCityBoxes(StringBuilder sb, List<Cidade> cities, List<Jogador> jogadores) {
        // Top border
        for (int i = 0; i < cities.size(); i++) {
            sb.append("┌").append(HORIZONTAL_LINE.repeat(CITY_BOX_WIDTH - 2)).append("┐");
            if (i < cities.size() - 1) sb.append("  ");
        }
        sb.append("\n");
        
        // City name line
        for (int i = 0; i < cities.size(); i++) {
            Cidade cidade = cities.get(i);
            String cityName = truncate(cidade.getNome(), CITY_BOX_WIDTH - 4);
            sb.append("│ ").append(centerText(cityName, CITY_BOX_WIDTH - 4)).append(" │");
            if (i < cities.size() - 1) sb.append("  ");
        }
        sb.append("\n");
        
        // Disease info line
        for (int i = 0; i < cities.size(); i++) {
            Cidade cidade = cities.get(i);
            String diseaseInfo = getDiseaseInfo(cidade);
            sb.append("│ ").append(centerText(diseaseInfo, CITY_BOX_WIDTH - 4)).append(" │");
            if (i < cities.size() - 1) sb.append("  ");
        }
        sb.append("\n");
        
        // Players and research center line
        for (int i = 0; i < cities.size(); i++) {
            Cidade cidade = cities.get(i);
            String statusInfo = getStatusInfo(cidade, jogadores);
            sb.append("│ ").append(centerText(statusInfo, CITY_BOX_WIDTH - 4)).append(" │");
            if (i < cities.size() - 1) sb.append("  ");
        }
        sb.append("\n");
        
        // Bottom border
        for (int i = 0; i < cities.size(); i++) {
            sb.append("└").append(HORIZONTAL_LINE.repeat(CITY_BOX_WIDTH - 2)).append("┘");
            if (i < cities.size() - 1) sb.append("  ");
        }
        sb.append("\n");
    }
    
    /**
     * Renders horizontal connections between adjacent cities
     */
    private static void renderHorizontalConnections(StringBuilder sb, List<Cidade> cities) {
        for (int i = 0; i < cities.size() - 1; i++) {
            Cidade current = cities.get(i);
            Cidade next = cities.get(i + 1);
            
            // Check if cities are connected
            boolean connected = current.getCidadesVizinhas().contains(next);
            
            // Position for connection line (middle of the gap between boxes)
            sb.append(" ".repeat(CITY_BOX_WIDTH));
            
            if (connected) {
                sb.append(CONNECTION_LINE);
            } else {
                sb.append("  ");
            }
        }
        if (!cities.isEmpty()) {
            sb.append("\n");
        }
    }
    
    /**
     * Renders vertical connections between rows
     */
    private static void renderVerticalConnections(StringBuilder sb, List<Cidade> upperRow, List<Cidade> lowerRow) {
        // This is a simplified version - in a real implementation you might want more sophisticated positioning
        for (int i = 0; i < Math.max(upperRow.size(), lowerRow.size()); i++) {
            if (i < upperRow.size() && i < lowerRow.size()) {
                Cidade upper = upperRow.get(i);
                Cidade lower = lowerRow.get(i);
                
                boolean connected = upper.getCidadesVizinhas().contains(lower);
                
                sb.append(" ".repeat(CITY_BOX_WIDTH / 2));
                if (connected) {
                    sb.append("║");
                } else {
                    sb.append(" ");
                }
                sb.append(" ".repeat(CITY_BOX_WIDTH / 2 + 1));
            } else {
                sb.append(" ".repeat(CITY_BOX_WIDTH + 2));
            }
        }
        sb.append("\n");
    }
    
    /**
     * Gets disease information for a city
     */
    private static String getDiseaseInfo(Cidade cidade) {
        if (cidade.getDoenca() == null) {
            return "Saudável";
        }
        
        String diseaseEmoji = cidade.getDoenca().getCor() != null ? 
            cidade.getDoenca().getCor().getEmoji() : "🦠";
        
        return diseaseEmoji + " " + cidade.getIntensidadeDaDoenca();
    }
    
    /**
     * Gets status information (players, research center) for a city
     */
    private static String getStatusInfo(Cidade cidade, List<Jogador> jogadores) {
        StringBuilder status = new StringBuilder();
        
        // Check for research center
        if (cidade.hasCentroDePesquisa()) {
            status.append("🏥 ");
        }
        
        // Check for players
        List<Jogador> playersInCity = jogadores.stream()
            .filter(j -> j.getPosicao() != null && j.getPosicao().equals(cidade))
            .collect(Collectors.toList());
        
        if (!playersInCity.isEmpty()) {
            status.append("👤").append(playersInCity.size());
        }
        
        return status.toString();
    }
    
    /**
     * Utility method to truncate text to fit in box
     */
    private static String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * Utility method to center text in a given width
     */
    private static String centerText(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        
        int leftPadding = (width - text.length()) / 2;
        int rightPadding = width - text.length() - leftPadding;
        
        return " ".repeat(leftPadding) + text + " ".repeat(rightPadding);
    }
    
    /**
     * Renders a detailed connection map showing all city connections
     */
    public static String renderConnectionMap(Tabuleiro tabuleiro) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("🗺️  ").append(Cor.textoEmBranco("MAPA DE CONEXÕES")).append("\n");
        sb.append("═".repeat(60)).append("\n");
        
        List<Cidade> cidades = tabuleiro.getCidades();
        for (Cidade cidade : cidades) {
            sb.append("🏙️  ").append(Cor.textoEmBranco(cidade.getNome())).append("\n");
            
            if (cidade.getCidadesVizinhas().isEmpty()) {
                sb.append("   └─ Nenhuma conexão\n");
            } else {
                for (int i = 0; i < cidade.getCidadesVizinhas().size(); i++) {
                    Cidade vizinha = cidade.getCidadesVizinhas().get(i);
                    String connector = (i == cidade.getCidadesVizinhas().size() - 1) ? "└─" : "├─";
                    sb.append("   ").append(connector).append(" ").append(vizinha.getNome()).append("\n");
                }
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
}
