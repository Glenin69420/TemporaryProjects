package servicios;

import clases.Comentario;

public class ComentarioService extends GestionDb<Comentario> {

    private static ComentarioService instancia;

    public ComentarioService(){
        super(Comentario.class);
    }

    public static ComentarioService getInstancia(){
        if(instancia==null){
            instancia = new ComentarioService();
        }
        return instancia;
    }

}
