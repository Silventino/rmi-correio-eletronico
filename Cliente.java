
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;



public class Cliente{
    private static Correio correio;
    
    public static boolean cadastrarNovoUsuario() throws RemoteException{
        Scanner input = new Scanner(System.in);
        
        System.out.println("Nome do novo usuário: ");
        String nome = input.nextLine();
        System.out.println("Senha do novo usuário: ");
        String senha = input.nextLine();

        Usuario u = new Usuario(nome, senha);
        return correio.cadastrarUsuario(u);
    }
    
    public static boolean enviarMensagem() throws RemoteException{
        Scanner input = new Scanner(System.in);
        
        System.out.println("Digite o seu nome de usuário(remetente): ");
        String remetente = input.nextLine();
        System.out.println("Digite a sua senha: ");
        String senha = input.nextLine();
        System.out.println("Digite o titulo da mensagem: ");
        String titulo = input.nextLine();
        System.out.println("Digite o texto da mensagem: ");
        String texto = input.nextLine();
        System.out.println("Digite o nome do destinatario: ");
        String destinatario = input.nextLine();
        
        Mensagem m = new Mensagem(remetente, titulo, texto);
        return correio.sendMensagem(m, senha, destinatario);
    }
    
    public static Mensagem getPrimeiraMensagem() throws RemoteException{
        Scanner input = new Scanner(System.in);
        System.out.println("Nome do usuário: ");
        String nome = input.nextLine();
        System.out.println("Senha: ");
        String senha = input.nextLine();
        
        return correio.getMensagem(nome, senha);
    }
    
    public static int getNumeroDeMensagens() throws RemoteException{
        Scanner input = new Scanner(System.in);
        System.out.println("Nome do usuário: ");
        String nome = input.nextLine();
        System.out.println("Senha: ");
        String senha = input.nextLine();
        
        return correio.getNMensagens(nome, senha);
    }
    
    public static void printMenu(){
            System.out.println("----------- MENU -----------");
            System.out.println("1 - Cadastrar um novo usuário");
            System.out.println("2 - Enviar mensagem para um usuário");
            System.out.println("3 - Pegar primeira mensagem de um usuario");
            System.out.println("4 - Recuperar número de mensagens de um usuario");
            System.out.println("0 - Sair");
            System.out.println("----------------------------");
    }
    
    public static void main(String args[]) {
        String host = null;
        try {
            Registry registry = LocateRegistry.getRegistry(host); 
            Correio correio = (Correio) registry.lookup("Correio");
            Cliente.correio = correio;
            Scanner input = new Scanner( System.in );
            printMenu();
            int comando = input.nextInt();
            while (comando != 0){
                ArrayList<String> dados;
                boolean resposta;
                
                switch(comando){
                    // novo usuario
                    case 1:
                        resposta = cadastrarNovoUsuario();
                        if(resposta) {
                            System.out.println("Usuario cadastrado com sucesso");
                        } else {
                            System.out.println("Falha ao cadastrar novo usuario.");
                        }
                        break;
                        
                    // enviar mensagem                  
                    case 2:
                        resposta = enviarMensagem();
                        if (resposta){
                            System.out.println("Mensagem enviada com sucesso");
                        } else {
                            System.out.println("Falha ao enviar mensagem");
                        }
                        break;
                        
                    // pegar primeira mensagem                       
                    case 3:
                        Mensagem msg = getPrimeiraMensagem();
                        if (msg == null){
                            System.out.println("Erro ao recuperar mensagem");
                            break;
                        }
                        System.out.println(msg);
                        break;
                        
                    // recuperar numero de mensagens de um usuario                       
                    case 4:
                        int numeroDeMensagens = getNumeroDeMensagens();
                        if(numeroDeMensagens == -1) {
                            System.out.println("Erro");
                        } else {
                            System.out.println("Número de mensagens: " + numeroDeMensagens);
                        }
                        System.out.println();
                        break;
                        
                    // comando pra sair
                    case 0:
                        System.out.println("Good Bye :)");
                        break;
                        
                    default:
                        System.out.println("Opcao invalida");
                }
                
                printMenu();
                comando = input.nextInt();
            }
        } catch (Exception e) {
            System.err.println("Cliente exception: " + e.toString());
            e.printStackTrace();
        }
    }
}