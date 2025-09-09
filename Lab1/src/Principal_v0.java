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

/*
 * Cliente.java
 * @author Dimitri Maia Ra: a2419840 
 * @author Thiago Cristalvao Ra: a2368072
 */

public class Principal_v0 {

    public String read() {
        //usa o padrão de divisão com a regex para o '%' seguido de nova linha
        Pattern delimitador = Pattern.compile("^%\\s*$", Pattern.MULTILINE);
        Path caminhoArq = Paths.get("D:\\Faculdade\\NovoBarraco\\Distribuidos\\Lab1\\src\\fortune-br.txt");
        
        try {
            String conteudo = Files.readString(caminhoArq, StandardCharsets.UTF_8);
            String[] listaFortunas = delimitador.split(conteudo);

            //filtra as strings vazias que podem surgir da divisão
            List<String> fortunes = new ArrayList<>();
            for (String fortune : listaFortunas) {
                if (!fortune.trim().isEmpty()) {
                    fortunes.add(fortune.trim());
                }
            }
            
            //escolhe uma fortuna aleatoriamente
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
        Path caminhoArq = Paths.get("D:\\Faculdade\\NovoBarraco\\Distribuidos\\Lab1\\src\\fortune-br.txt");

        //define a mensagem a ser escrita, incluindo o delimitador de nova linha
        String novoConteudo = "\n" + novaFortuna.trim() + "\n";

        try {
            //escreve a nova fortuna no final do arquivo sem apagar o conteúdo existente
            Files.write(caminhoArq, novoConteudo.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            System.out.println("Nova fortuna adicionada com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo: " + e.getMessage());
        }
    }

    //metodo main para executar o programa
    public static void main(String[] args) {
        Principal_v0 app = new Principal_v0();

        //leitura de uma fortuna aleatória
        System.out.println("--- LENDO UMA FORTUNA ALEATÓRIA ---");
        String fortunaLida = app.read();
        System.out.println(fortunaLida);
        System.out.println("-----------------------------------");

        //escrita de uma nova fortuna
        System.out.println("\n--- ESCREVENDO UMA NOVA FORTUNA ---");
        String novaFortuna = "A dor e inevitavel o sofrimento e inevitavel.\n--Pain";
        app.write(novaFortuna);
        System.out.println("-----------------------------------");
        System.out.println("Programa finalizado. visualize o arquivo para ver a nova fortuna adicionada.");
    }
}
