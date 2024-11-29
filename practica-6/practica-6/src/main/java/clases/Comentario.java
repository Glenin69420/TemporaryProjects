package clases;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String comentario;
    @ManyToOne
    private Producto producto;
    String usuario;

    public Comentario() {
    }

    public Comentario(String comentario, Producto producto, String usuario) {
        this.comentario = comentario;
        this.producto = producto;
        this.usuario = usuario;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getComentario() {return comentario;}

    public void setComentario(String comentario) {this.comentario = comentario;}

    public Producto getProducto() {return producto;}

    public void setProducto(Producto producto) {this.producto = producto;}

    public String getUsuario() {return usuario;}

    public void setUsuario(String usuario) {this.usuario = usuario;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comentario that = (Comentario) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
