package servicios;

import clases.Usuario;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;

public class UsuarioService extends GestionDb<Usuario>{

    private static UsuarioService instancia;

    private UsuarioService(){super(Usuario.class);}

    public static UsuarioService getInstancia(){
        if(instancia==null){
            instancia = new UsuarioService();
        }
        return instancia;
    }

    public List<Usuario> findAllByName(String nombre){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select u from Usuario u where u.nombre like :nombre");
        query.setParameter("nombre", nombre+"%");
        List<Usuario> lista = query.getResultList();
        return lista;
    }

    public List<Usuario> findAllByUser(String usuario){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select u from Usuario u where u.nombre like :usuario");
        query.setParameter("usuario", usuario+"%");
        List<Usuario> lista = query.getResultList();
        return lista;
    }

    public List<Usuario> findAll(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from usuario ", Usuario.class);
        //query.setParameter("nombre", apellido+"%");
        List<Usuario> lista = query.getResultList();
        return lista;
    }

}
