package servicios;

import clases.CarritoItem;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.query.SelectionQuery;

import java.util.List;

public class CarritoItemService extends GestionDb<CarritoItem>{

    private static CarritoItemService instancia;

    private CarritoItemService(){super(CarritoItem.class);}

    public static CarritoItemService getInstancia(){
        if(instancia==null){
            instancia = new CarritoItemService();
        }
        return instancia;
    }

    public List<CarritoItem> findAllByName(String nombre){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select u from CarritoItem u where u.nombre like :nombre");
        query.setParameter("nombre", nombre+"%");
        List<CarritoItem> lista = query.getResultList();
        return lista;
    }

    public List<CarritoItem> findAllById(int CarritoItem){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select u from CarritoItem u where u.id like :carritoItem");
        query.setParameter("carritoItem", CarritoItem+"%");
        List<CarritoItem> lista = query.getResultList();
        return lista;
    }

    public List<CarritoItem> findAll(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from carritoItem ", CarritoItem.class);
        //query.setParameter("nombre", apellido+"%");
        List<CarritoItem> lista = query.getResultList();
        return lista;
    }

    public List<CarritoItem> findAllByPage(int pageNumber) throws PersistenceException {
        int pageSize = 10;
        List<CarritoItem> currPage;
        EntityManager em = getEntityManager();
        try{
            CriteriaQuery<CarritoItem> selectQuery = em.getCriteriaBuilder().createQuery(CarritoItem.class);
            selectQuery.from(CarritoItem.class);
            SelectionQuery<CarritoItem> query = (SelectionQuery<CarritoItem>) em.createQuery(selectQuery);
            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
            currPage = query.list();

        } finally {
            em.close();
        }
        return currPage;
    }

}
