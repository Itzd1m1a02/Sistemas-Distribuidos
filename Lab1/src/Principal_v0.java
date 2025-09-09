import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.nio.charset.StandardCharsets;

public class Principal_v0 {

    public String read() {
        // Usa o padrão de divisão com a regex para o '%' seguido de nova linha
        Pattern delimitador = Pattern.compile("^%\\s*$", Pattern.MULTILINE);
        Path caminhoArq = Paths.get("D:\\Faculdade\\NovoBarraco\\Distribuidos\\Lab1\\src\\fortune-br.txt");
        
        try {
            // Lê todo o conteúdo do arquivo
            String conteudo = Files.readString(caminhoArq, StandardCharsets.UTF_8);

            // Divide o conteúdo em um array de strings, onde cada string é uma fortuna
            String[] listaFortunas = delimitador.split(conteudo);

            // Filtra as strings vazias que podem surgir da divisão
            List<String> fortunes = new ArrayList<>();
            for (String fortune : listaFortunas) {
                if (!fortune.trim().isEmpty()) {
                    fortunes.add(fortune.trim());
                }
            }
            
            // Escolhe uma fortuna aleatoriamente
            if (!fortunes.isEmpty()) {
                Random random = new Random();
                int index = random.nextInt(fortunes.size());
                return fortunes.get(index);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        return "Nenhuma fortuna encontrada.";
    }

    public void write(String novaFortuna) {
        // Define o caminho para o arquivo
        Path caminhoArq = Paths.get("D:\\Faculdade\\NovoBarraco\\Distribuidos\\Lab1\\src\\fortune-br.txt");

        // Define a mensagem a ser escrita, incluindo o delimitador de nova linha
        String novoConteudo = "\n" + novaFortuna.trim() + "\n%\n";

        try {
            // Escreve a nova fortuna no final do arquivo sem apagar o conteúdo existente
            Files.write(caminhoArq, novoConteudo.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            System.out.println("Nova fortuna adicionada com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    // Método main para executar o programa
    public static void main(String[] args) {
        Principal_v0 app = new Principal_v0();

        // 1. Testando a leitura de uma fortuna aleatória
        System.out.println("--- LENDO UMA FORTUNA ALEATÓRIA ---");
        String fortunaLida = app.read();
        System.out.println(fortunaLida);
        System.out.println("-----------------------------------");

        // 2. Testando a escrita de uma nova fortuna
        System.out.println("\n--- ESCREVENDO UMA NOVA FORTUNA ---");
        String novaFortuna = "A dor e inevitavel o sofrimento e inevitavel.\n--Pain";
        app.write(novaFortuna);
        System.out.println("-----------------------------------");
        System.out.println("Programa finalizado. visualize o arquivo para ver a nova fortuna adicionada.");
    }
}