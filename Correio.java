import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Correio extends Remote {
  
    boolean cadastrarUsuario (Usuario u) throws RemoteException;
    	

    // Recupera a primeira mensagem na lista de mensagens do usuario; a mensagem deve ser removida
    // Exigir autenticação do usuário
    Mensagem getMensagem (String username, String senha) throws RemoteException;
    
    // retorna o número de mensagens na fila de mensagens dos usuário
    // Exigir autenticação do usuário
    int getNMensagens (String username, String senha) throws RemoteException;
	
    // Exigir autenticação do usuário (senha do remetente, incluído como atributo da mensagem)
    boolean sendMensagem (Mensagem m, String senha, String usernameDestinatario) throws RemoteException;
}