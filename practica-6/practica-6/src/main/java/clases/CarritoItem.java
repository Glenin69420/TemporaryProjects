package clases;

import jakarta.persistence.*;
import servicios.CarritoService;

@Entity
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private double precio;
    private int cantidad;
    @ManyToOne
    private Carrito carrito;

    public CarritoItem(Producto producto, int cantidad, Carrito carrito) {
        this.nombre = producto.getNombre();
        this.precio = producto.getPrecio();
        this.cantidad = cantidad;
        this.carrito = carrito;
    }

    public CarritoItem() {
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

}
