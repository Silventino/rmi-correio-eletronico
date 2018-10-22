
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor implements Correio {

    private String endereco;
    private ArrayList<Usuario> usuarios;

    public Servidor() {
        this.endereco = "127.0.0.1";
        this.usuarios = lerDoArquivo();

    }

    private boolean login(String nome, String senha){
        Usuario usuario = this.buscarUsuarioPorNome(nome);
        if(usuario == null){
            return false;
        }
        if(usuario.getSenha().equals(senha) == true){
            return true;
        }
        return false;
    }
    
    public void escreverNoArquivo(){
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream("dados.txt");
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(usuarios);
            objectOut.close();
        } catch (Exception ex) {
            System.out.println("Erro ao escrever no arquivo");
        }

    }
    
    public ArrayList<Usuario> lerDoArquivo(){
        ArrayList<Usuario> usuarios = null;
        try{
            File file = new File("dados.txt");
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
            usuarios = (ArrayList<Usuario>) input.readObject();
        }
        catch(Exception ex){
            System.out.println("Erro ao ler do arquivo");
            usuarios = new ArrayList<Usuario>();
        }
        for(Usuario u : usuarios){
            System.out.println(u);
        }
        return usuarios;
    }
    
    @Override
    public int getNMensagens (String username, String senha) throws RemoteException{
        if(!this.login(username, senha)) {
            return -1;
        }
        Usuario usuario = this.buscarUsuarioPorNome(username);
        return usuario.getQuantidadeMensagens();
    }
    
    @Override
    public Mensagem getMensagem (String username, String senha) throws RemoteException{
        if(!this.login(username, senha)) {
            return null;
        }
        Usuario usuario = this.buscarUsuarioPorNome(username);
        Mensagem retorno = usuario.popMensagem();
        escreverNoArquivo();
        return retorno;
    }
    
    @Override
    public boolean sendMensagem (Mensagem m, String senha, String usernameDestinatario) throws RemoteException{
        String usernameRemetente = m.getUsernameRemetente();
        if(!this.login(usernameRemetente, senha)) {
            return false;
        }
        Usuario usuarioDestino = this.buscarUsuarioPorNome(usernameDestinatario);
        if(usuarioDestino == null){
            return false;
        }
        usuarioDestino.adicionaMensagem(m);
        escreverNoArquivo();
        return true;
    }
    
    @Override
    public boolean cadastrarUsuario (Usuario u) throws RemoteException{
        Usuario aux = this.buscarUsuarioPorNome(u.getUsername());
        if(aux == null){
            this.usuarios.add(u);
            escreverNoArquivo();
            return true;
        }
        else{
            return false;
        }
        
    }

    public Usuario buscarUsuarioPorNome(String nome){
        for( Usuario usuario : this.usuarios){
            if(usuario.getUsername().equals(nome)){
                return usuario;
            }
        }
        return null;
    }

    public static void main(String args[]) {
        try {
            Servidor servidor = new Servidor();
            System.setProperty("java.rmi.server.hostname", servidor.endereco);

            //Create and export a remote object
            Correio stub = (Correio) UnicastRemoteObject.exportObject(servidor,0);
            
            //Register the remote object with a Java RMI registry
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("Correio", stub);

            System.out.println("Servidor pronto");
        } catch (Exception e) {
            System.err.println("Erro Servidor: " + e.toString());
            e.printStackTrace();
        }
    }
}