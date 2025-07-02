package com.example.pandemic.domínio.utils;

import java.util.*;
import java.util.stream.Collectors;

import com.example.pandemic.domínio.entidades.Cidade;
import com.example.pandemic.domínio.entidades.Jogador;
import com.example.pandemic.domínio.entidades.Tabuleiro;
import com.example.pandemic.domínio.enums.Cor;

/**
 * Simple utility class for rendering game state information
 */
public class GameStateRenderer {
    
    /**
     * Renders a visual network map of cities and their connections
     */
    public static String renderCityNetwork(Tabuleiro tabuleiro, List<Jogador> jogadores) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("[MAPA] ").append(Cor.textoEmBranco("REDE DE CIDADES")).append("\n");
        sb.append("=".repeat(80)).append("\n\n");
        
        List<Cidade> cidades = tabuleiro.getCidades();
        
        // Group cities by disease color for better organization
        Map<String, List<Cidade>> cidadesPorCor = groupCitiesByDiseaseColor(cidades);
        
        // Render each color group
        for (Map.Entry<String, List<Cidade>> entry : cidadesPorCor.entrySet()) {
            renderColorGroup(sb, entry.getKey(), entry.getValue(), jogadores);
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Groups cities by their disease color
     */
    private static Map<String, List<Cidade>> groupCitiesByDiseaseColor(List<Cidade> cidades) {
        Map<String, List<Cidade>> groups = new LinkedHashMap<>();
        
        for (Cidade cidade : cidades) {
            String colorKey;
            if (cidade.getDoenca() != null && cidade.getDoenca().getCor() != null) {
                colorKey = cidade.getDoenca().getCor().getNome();
            } else {
                colorKey = "Sem Doença";
            }
            
            groups.computeIfAbsent(colorKey, k -> new ArrayList<>()).add(cidade);
        }
        
        return groups;
    }
    
    /**
     * Renders a group of cities with the same disease color
     */
    private static void renderColorGroup(StringBuilder sb, String colorName, List<Cidade> cidades, List<Jogador> jogadores) {
        // Get color for header
        String colorHeader = getColorHeader(colorName, cidades);
        
        sb.append("[GRUPO] ").append(colorHeader).append(" (").append(cidades.size()).append(" cidades)").append("\n");
        sb.append("-".repeat(60)).append("\n");
        
        // Render cities in a grid layout
        renderCitiesInGrid(sb, cidades, jogadores);
    }
    
    /**
     * Gets colored header for city group
     */
    private static String getColorHeader(String colorName, List<Cidade> cidades) {
        if (cidades.isEmpty()) {
            return colorName;
        }
        
        Cidade firstCity = cidades.get(0);
        if (firstCity.getDoenca() != null && firstCity.getDoenca().getCor() != null) {
            return firstCity.getDoenca().getCor().colorir(colorName) + " " + firstCity.getDoenca().getCor().getVisualIndicator();
        }
        
        return colorName;
    }
    
    /**
     * Renders cities in a 2-column grid with connections
     */
    private static void renderCitiesInGrid(StringBuilder sb, List<Cidade> cidades, List<Jogador> jogadores) {
        for (int i = 0; i < cidades.size(); i += 2) {
            Cidade cidade1 = cidades.get(i);
            Cidade cidade2 = (i + 1 < cidades.size()) ? cidades.get(i + 1) : null;
            
            // Render city pair
            renderCityPair(sb, cidade1, cidade2, jogadores);
            
            if (i + 2 < cidades.size()) {
                sb.append("\n");
            }
        }
    }
    
    /**
     * Renders a pair of cities side by side
     */
    private static void renderCityPair(StringBuilder sb, Cidade cidade1, Cidade cidade2, List<Jogador> jogadores) {
        String city1Info = formatCityInfo(cidade1, jogadores);
        String city2Info = cidade2 != null ? formatCityInfo(cidade2, jogadores) : "";
        
        sb.append(city1Info);
        if (!city2Info.isEmpty()) {
            sb.append("     ").append(city2Info);
        }
        sb.append("\n");
        
        // Show connections
        renderCityConnections(sb, cidade1, cidade2);
    }
    
    /**
     * Formats city information in a compact way
     */
    private static String formatCityInfo(Cidade cidade, List<Jogador> jogadores) {
        StringBuilder info = new StringBuilder();
        
        // City name with status indicators
        info.append("🏙️  ").append(cidade.getNome());
        
        // Research center
        if (cidade.hasCentroDePesquisa()) {
            info.append(" 🏥");
        }
        
        // Players in city
        List<Jogador> playersInCity = jogadores.stream()
            .filter(j -> j.getPosicao() != null && j.getPosicao().equals(cidade))
            .collect(Collectors.toList());
        
        if (!playersInCity.isEmpty()) {
            info.append(" [P]x").append(playersInCity.size());
        }
        
        // Disease info
        if (cidade.getDoenca() != null && cidade.getIntensidadeDaDoenca() > 0) {
            String diseaseInfo = cidade.getDoenca().getCor() != null ? 
                cidade.getDoenca().getCor().getVisualIndicator() : "[!]";
            info.append(" ").append(diseaseInfo).append("x").append(cidade.getIntensidadeDaDoenca());
        }
        
        return info.toString();
    }
    
    /**
     * Renders connections for a city pair
     */
    private static void renderCityConnections(StringBuilder sb, Cidade cidade1, Cidade cidade2) {
        StringBuilder connections1 = new StringBuilder();
        StringBuilder connections2 = new StringBuilder();
        
        // Format connections for city 1
        if (!cidade1.getCidadesVizinhas().isEmpty()) {
            connections1.append("   └─ Conecta: ");
            String vizinhas = cidade1.getCidadesVizinhas().stream()
                .map(Cidade::getNome)
                .limit(3) // Limit to avoid too long lines
                .collect(Collectors.joining(", "));
            connections1.append(vizinhas);
            if (cidade1.getCidadesVizinhas().size() > 3) {
                connections1.append("... (+").append(cidade1.getCidadesVizinhas().size() - 3).append(")");
            }
        }
        
        // Format connections for city 2
        if (cidade2 != null && !cidade2.getCidadesVizinhas().isEmpty()) {
            connections2.append("   └─ Conecta: ");
            String vizinhas = cidade2.getCidadesVizinhas().stream()
                .map(Cidade::getNome)
                .limit(3)
                .collect(Collectors.joining(", "));
            connections2.append(vizinhas);
            if (cidade2.getCidadesVizinhas().size() > 3) {
                connections2.append("... (+").append(cidade2.getCidadesVizinhas().size() - 3).append(")");
            }
        }
        
        // Render connections
        if (connections1.length() > 0) {
            sb.append(connections1);
            if (connections2.length() > 0) {
                // Calculate padding to align second city connections
                int padding = Math.max(0, 40 - connections1.length());
                sb.append(" ".repeat(padding)).append(connections2);
            }
            sb.append("\n");
        } else if (connections2.length() > 0) {
            sb.append(" ".repeat(40)).append(connections2).append("\n");
        }
    }
    
    /**
     * Renders a simple summary of the board state
     */
    public static String renderBoardSummary(Tabuleiro tabuleiro, List<Jogador> jogadores) {
        StringBuilder sb = new StringBuilder();
        
        // Disease summary
        sb.append("[DOENCAS] ").append(Cor.textoEmBranco("RESUMO DAS DOENÇAS:")).append("\n");
        Map<String, Integer> diseaseSummary = new HashMap<>();
        Map<String, String> diseaseColors = new HashMap<>();
        
        for (Cidade cidade : tabuleiro.getCidades()) {
            if (cidade.getDoenca() != null && cidade.getIntensidadeDaDoenca() > 0) {
                String diseaseName = cidade.getDoenca().getNome();
                diseaseSummary.merge(diseaseName, cidade.getIntensidadeDaDoenca(), Integer::sum);
                
                if (cidade.getDoenca().getCor() != null) {
                    diseaseColors.put(diseaseName, 
                        cidade.getDoenca().getCor().colorir(diseaseName) + " " + cidade.getDoenca().getCor().getEmoji());
                }
            }
        }
        
        if (diseaseSummary.isEmpty()) {
            sb.append("   [OK] Nenhuma doença ativa no tabuleiro\n");
        } else {
            for (Map.Entry<String, Integer> entry : diseaseSummary.entrySet()) {
                String displayName = diseaseColors.getOrDefault(entry.getKey(), entry.getKey());
                sb.append("   ├─ ").append(displayName).append(": ").append(entry.getValue()).append(" cubos\n");
            }
        }
        
        // Research centers summary
        long researchCenters = tabuleiro.getCidades().stream()
            .mapToLong(c -> c.hasCentroDePesquisa() ? 1 : 0)
            .sum();
        
        sb.append("\n🏥 ").append(Cor.textoEmBranco("CENTROS DE PESQUISA:")).append(" ").append(researchCenters).append("/").append(tabuleiro.getCidades().size()).append("\n");
        
        // Player positions summary
        sb.append("\n[POSICOES] ").append(Cor.textoEmBranco("POSIÇÕES DOS JOGADORES:")).append("\n");
        for (Jogador jogador : jogadores) {
            String position = jogador.getPosicao() != null ? jogador.getPosicao().getNome() : "Não posicionado";
            sb.append("   ├─ ").append(jogador.getNome()).append(": ").append(position).append("\n");
        }
        
        return sb.toString();
    }
}
