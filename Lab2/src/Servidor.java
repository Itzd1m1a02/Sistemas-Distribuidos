import java.io.*;
import java.net.*;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/*
 * Cliente.java
 * @author Dimitri Maia Ra: a2419840 
 * @author Thiago Cristalvao Ra: a
 * 
 * Servidor que atende requisicoes de leitura e escrita de fortunas.
 * Utiliza JSON para formatar as mensagens entre cliente e servidor.
 */

public class Servidor {
    private static final int PORTA = 12345;
    private static final String CAMINHO_ARQUIVO = "D:\\Faculdade\\NovoBarraco\\Distribuidos\\Lab2\\src\\fortune-br.txt";

    // metodo que analisa a requisição JSON e decide qual ação tomar
    private static String processarRequisicao(String requisicaoJson) {
        try {
            JSONObject objetoJson = new JSONObject(requisicaoJson);
            String metodo = objetoJson.getString("method");

            if ("read".equals(metodo)){
                String fortuna = lerFortunaDoArquivo();
                // retorna um JSON com o resultado da leitura
                return new JSONObject().put("result", fortuna).toString();
            }else if("write".equals(metodo)) {
                if (objetoJson.has("args")) {
                    String novaFortuna = objetoJson.getJSONArray("args").getString(0);
                    escreverFortunaNoArquivo(novaFortuna);
                    // retorna um JSON com a fortuna que foi escrita
                    return new JSONObject().put("result", novaFortuna).toString();
                }
            }
            return new JSONObject().put("result", "false").toString();
        } catch (Exception e) {
            // trata qualquer erro durante o processamento do JSON
            System.err.println("Erro ao processar a requisição JSON: " + e.getMessage());
            return new JSONObject().put("result", "false").toString();
        }
    }

    //metodo que le uma fortuna aleatoria do arquivo
    private static String lerFortunaDoArquivo() throws IOException{
        //define o delimitador para separar as fortunas no arquivo
        Pattern delimitador = Pattern.compile("^%\\s*$", Pattern.MULTILINE);
        Path caminhoArq = Paths.get(CAMINHO_ARQUIVO);
        
        //le todo o conteúdo do arquivo como uma string
        String conteudo = new String(Files.readAllBytes(caminhoArq), StandardCharsets.UTF_8);
        String[] fortunas = delimitador.split(conteudo);
        
        List<String> listaFortunas = new ArrayList<>();
        for (String fortuna : fortunas) {
            if (!fortuna.trim().isEmpty()) {
                listaFortunas.add(fortuna.trim());
            }
        }
        
        if (!listaFortunas.isEmpty()) {
            Random aleatorio = new Random();
            int index = aleatorio.nextInt(listaFortunas.size());
            return listaFortunas.get(index);
        }
        return "Nenhuma fortuna encontrada";
    }

    //metodo que adiciona uma nova fortuna ao final do arquivo
    private static void escreverFortunaNoArquivo(String novaMensagem) throws IOException{
        Path caminhoArq = Paths.get(CAMINHO_ARQUIVO);
        String conteudoParaEscrever = "\n" + novaMensagem.trim() + "\n%\n";
        
        //escreve os bytes da nova fortuna no final do arquivo
        Files.write(caminhoArq, conteudoParaEscrever.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    }

    public static void main(String[] args) throws IOException{
        //cria um socket de servidor que escuta na porta especificada
        try(ServerSocket socketServidor = new ServerSocket(PORTA)){
            System.out.println("Servidor iniciado na porta " + PORTA);
            while(true){
                //aceita a conexao de um cliente. o programa para aqui ate que um cliente se conecte
                try(Socket socketCliente = socketServidor.accept();
                     //cria os streams de leitura e escrita para se comunicar com o cliente
                     BufferedReader leitor = new BufferedReader(new InputStreamReader(socketCliente.getInputStream(), StandardCharsets.UTF_8));
                     PrintWriter escritor = new PrintWriter(new OutputStreamWriter(socketCliente.getOutputStream(), StandardCharsets.UTF_8), true)) {

                    //le a requisição JSON enviada pelo cliente
                    String requisicaoCliente = leitor.readLine();
                    System.out.println("Requisição recebida: " + requisicaoCliente);
                    
                    //processa a requisição e obtem a resposta
                    String resposta = processarRequisicao(requisicaoCliente);
                    //envia a resposta de volta ao cliente
                    escritor.println(resposta);
                    System.out.println("Resposta enviada: " + resposta);
                }catch(IOException e){
                    //trata erros de I/O em conexões com clientes
                    System.err.println("Erro na conexão com o cliente: " + e.getMessage());
                }
            }
        }
    }
}