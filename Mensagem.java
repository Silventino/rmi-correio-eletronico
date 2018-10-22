import java.util.Date;
import java.io.Serializable;


public class Mensagem implements Serializable {

    private String usernameRemetente;
    private String titulo;
    private String texto;
    private Date data;

    Mensagem(String usernameRemetente, String titulo, String texto){
        this.usernameRemetente = usernameRemetente;
        this.titulo = titulo;
        this.texto = texto;
        this.data = new Date();
    }
    
    public String getUsernameRemetente(){
        return this.usernameRemetente;
    }

    @Override
    public String toString(){
        String resposta = 
        "Titulo do email: " + this.titulo + "\n" +
        "De: " + this.usernameRemetente + " \n" +
        "Data de envio: " + this.data + "\n" +
        "Texto: " + this.texto + "\n";
        return resposta;        
    }

}