import java.util.LinkedList;
import java.io.Serializable;

public class Usuario implements Serializable {

    private String username;
    private String senha;
    private LinkedList<Mensagem> listaDeMensagens;

    Usuario(String username, String senha){
        this.username = username;
        this.senha = senha;
        this.listaDeMensagens = new LinkedList<Mensagem>();
    }
    
    @Override
    public String toString(){
        String s = "NOME: " + username + "\n";
        for(Mensagem m : this.listaDeMensagens){
            s += m.toString();
        }
        s += "\n\n";
        return s;
    }

    public String getUsername(){
        return this.username;
    }

    public String getSenha(){
        return this.senha;
    }

    public void adicionaMensagem(Mensagem m){
        this.listaDeMensagens.add(m);
    }
    
    public Mensagem popMensagem(){
        return this.listaDeMensagens.pop();
    }

    public int getQuantidadeMensagens() {
        return this.listaDeMensagens.size();
    }

}