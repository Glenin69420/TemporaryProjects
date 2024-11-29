package entidades;

public class Usuario {

    private String usuario;
    private String password;
    private String nombre;
    private boolean admin;

    public Usuario(){}

    public Usuario(String usuario, String password, String nombre, boolean admin) {
        this.usuario = usuario;
        this.password = password;
        this.nombre = nombre;
        this.admin = admin;
    }

    public Usuario(String nombre, boolean admin, String usuario) {
        this.nombre = nombre;
        this.admin = admin;
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
