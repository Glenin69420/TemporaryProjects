package servicios;

import clases.Producto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.query.SelectionQuery;

import java.util.List;

public class ProductoService extends GestionDb<Producto>{

    private static ProductoService instancia;

    private ProductoService(){super(Producto.class);}

    public static ProductoService getInstancia(){
        if(instancia==null){
            instancia = new ProductoService();
        }
        return instancia;
    }

    public List<Producto> findAllByName(String nombre){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select u from Producto u where u.nombre like :nombre");
        query.setParameter("nombre", nombre+"%");
        List<Producto> lista = query.getResultList();
        return lista;
    }

    public List<Producto> findAllById(int producto){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select u from Producto u where u.id like :producto");
        query.setParameter("producto", producto+"%");
        List<Producto> lista = query.getResultList();
        return lista;
    }

    public List<Producto> findAll(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from producto ", Producto.class);
        //query.setParameter("nombre", apellido+"%");
        List<Producto> lista = query.getResultList();
        return lista;
    }

    public List<Producto> findAllByPage(int pageNumber) throws PersistenceException {
        int pageSize = 10;
        List<Producto> currPage;
        EntityManager em = getEntityManager();
        try{
            CriteriaQuery<Producto> selectQuery = em.getCriteriaBuilder().createQuery(Producto.class);
            selectQuery.from(Producto.class);
            SelectionQuery<Producto> query = (SelectionQuery<Producto>) em.createQuery(selectQuery);
            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
            currPage = query.list();

        } finally {
            em.close();
        }
        return currPage;
    }

}
