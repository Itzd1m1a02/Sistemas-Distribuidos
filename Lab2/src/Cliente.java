import java.io.*;
import java.net.*;
import java.util.Scanner;
import org.json.JSONObject;
/*
 * Cliente.java
 * @author Dimitri Maia Ra: a2419840 
 * @author Thiago Cristalvao Ra: a2368072
 * 
 * Cliente que se conecta a um servidor para ler ou escrever fortunas.
 * Utiliza JSON para formatar as mensagens entre cliente e servidor.
 */


public class Cliente {
    private static final String ENDERECO_SERVIDOR = "localhost";
    private static final int PORTA_SERVIDOR = 12345;

    public static void main(String[] args) {
        //usa o try with resources para garantir que o Scanner feche automaticamente
        try (Scanner leitor = new Scanner(System.in)) {
            while (true) {
                //exibe o menu de opções para o usuario
                System.out.println("\nMenu:");
                System.out.println("1 - Ler uma fortuna");
                System.out.println("2 - Escrever uma fortuna");
                System.out.println("3 - Sair");
                System.out.print("Escolha uma opção: ");

                String opcao = leitor.nextLine();

                if("1".equals(opcao)) {
                    requisicaoResposta("read", "");
                }else if("2".equals(opcao)) {
                    System.out.print("Digite a nova fortuna: ");
                    String novaFortuna = leitor.nextLine();
                    requisicaoResposta("write", novaFortuna);
                }else if("3".equals(opcao)) {
                    System.out.println("Encerrando o cliente.");
                    break;
                }else{
                    System.out.println("Opção inválida. Tente novamente.");
                }
            }
        }
    }
    //metodo para se conectar ao servidor, enviar a requisição e ler a resposta
    private static void requisicaoResposta(String metodo, String argumento) {
        //try with resources e para garantir que o socket e os streams de I/O fechem
        try(Socket socket = new Socket(ENDERECO_SERVIDOR, PORTA_SERVIDOR);
             PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader leitor = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            //cria um objeto JSON para a requisicao
            JSONObject requisicao = new JSONObject();
            requisicao.put("method", metodo);
            
            //adiciona o argumento args a requisicao, dependendo do método
            if("write".equals(metodo)) {
                requisicao.put("args", new String[]{argumento});
            }else{
                requisicao.put("args", new String[]{""});
            }
            escritor.println(requisicao.toString());
            String respostaString = leitor.readLine();
            
            //converte a string de resposta de volta para um objeto JSON
            JSONObject resposta = new JSONObject(respostaString);
            System.out.println("Resposta do servidor: " + resposta.getString("result"));

        }catch(IOException e) {
            //trata erros de conexão caso o servidor não esteja online
            System.err.println("Erro de conexão com o servidor: " + e.getMessage());
        }
    }
}
