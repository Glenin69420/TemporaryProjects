package clases;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int carrito_id;
    private String owner;
    @OneToMany(mappedBy = "carrito", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<CarritoItem> listaItems;
    private LocalDateTime fecha;
    private double totalVenta;
    private boolean pagado;

    public int getCarrito_id() {
        return carrito_id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<CarritoItem> getListaItems() {
        return listaItems;
    }

    public void setListaItems(List<CarritoItem> listaProducto) {
        this.listaItems = listaProducto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
        this.fecha = LocalDateTime.now();


    }

    public boolean getPagado() {
        return pagado;
    }

    public void agregarItem(CarritoItem item) {
        listaItems.add(item);
        totalVenta += item.getCantidad() * item.getPrecio();
    }

    public int getCantidad(){
        int cantidad = 0;
        for(CarritoItem carritoItem : listaItems){
            cantidad += carritoItem.getCantidad();
        }
        return cantidad;
    }

    public Carrito() {
    }

    public void eliminarItem(int productoId) {
        listaItems.removeIf(item -> item.getId() == productoId);
    }
}
