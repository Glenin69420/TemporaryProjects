package clases;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Producto {

    @Id
    @GeneratedValue
    private int id;
    private String nombre;
    private double precio;
    private String descripcion;
    private String mimeType;
    @Lob
    private String fotoBase64;
    @OneToMany(mappedBy = "producto", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<Comentario> comentarios;


    public Producto(){
    }

    public Producto(String nombre, double precio, String descripcion, String mimeType, String fotoBase64) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.mimeType = mimeType;
        this.fotoBase64 = fotoBase64;
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
    }

    public void mezclar(Producto e){
        id = e.getId();
        nombre = e.getNombre();
        precio = e.getPrecio();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }

    public Set<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Set<Comentario> comentarios) {
        this.comentarios = comentarios;
    }
}
