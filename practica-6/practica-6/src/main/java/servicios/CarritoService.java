package servicios;

import clases.Carrito;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarritoService extends GestionDb<Carrito>{

    private static CarritoService instancia;

    private CarritoService(){super(Carrito.class);}

    public static CarritoService getInstancia(){
        if(instancia==null){
            instancia = new CarritoService();
        }
        return instancia;
    }

    public List<Carrito> findAllByOwner(String owner){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select u from Carrito u where u.owner like :owner");
        query.setParameter("owner", owner+"%");
        List<Carrito> lista = query.getResultList();
        return lista;
    }

    public List<Carrito> findAllById(int id){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select u from Carrito u where u.id like :id");
        query.setParameter("id", id+"%");
        List<Carrito> lista = query.getResultList();
        return lista;
    }

    public List<Carrito> findAll(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from carrito ", Carrito.class);
        List<Carrito> lista = query.getResultList();
        return lista;
    }

    public List<Carrito> findAllFacturas() {
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from carrito where pagado = true", Carrito.class);
        List<Carrito> lista = query.getResultList();
        return lista;
    }

    public double calcularTotalVentas() {
        // Filtrar solo los carritos pagados y sumar el totalVenta
        return findAllFacturas().stream()
                .filter(Carrito::getPagado)  // Solo carritos pagados
                .mapToDouble(Carrito::getTotalVenta)
                .sum();
    }

    public Map<String, Integer> obtenerCantidadProductosVendidos() {
        Map<String, Integer> productosVendidos = new HashMap<>();

        // Filtrar solo carritos pagados y luego contar los productos
        findAllFacturas().stream()
                .filter(Carrito::getPagado)  // Solo carritos pagados
                .forEach(carrito -> {
                    carrito.getListaItems().forEach(item -> {
                        productosVendidos.merge(item.getNombre(), item.getCantidad(), Integer::sum);
                    });
                });

        return productosVendidos;
    }

    public List<Map<String, Object>> obtenerHistogramaVentas() {
        List<Map<String, Object>> ventasHistograma = new ArrayList<>();

        for (Carrito carrito : findAllFacturas()) {
            Map<String, Object> venta = new HashMap<>();
            venta.put("fecha", carrito.getFecha().toString()); // Convierte LocalDateTime a String
            venta.put("total", carrito.getTotalVenta());
            ventasHistograma.add(venta);
        }

        return ventasHistograma;
    }
}
