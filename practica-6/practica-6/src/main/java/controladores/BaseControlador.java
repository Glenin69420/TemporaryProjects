package controladores;

import clases.Carrito;
import io.javalin.Javalin;
import io.javalin.http.Context;
import servicios.CarritoService;
import servicios.ProductoService;
import servicios.UsuarioService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseControlador {

    protected Javalin app;

    public BaseControlador(Javalin app) {this.app = app;}

    abstract public void aplicarRutas();

    protected Map<String, Object> generarModeloSesion(Context ctx, String titulo){
        Map<String, Object> modelo = new HashMap<>();
        String usuario = ctx.sessionAttribute("usuario");

        Carrito carrito = ctx.sessionAttribute("carrito");
        if(carrito == null){
            carrito = new Carrito();
            carrito.setListaItems(new ArrayList<>());
            if(usuario != null){
            } carrito.setOwner(usuario);
            ctx.sessionAttribute("carrito", carrito);
        }

        modelo.put("usuarios", UsuarioService.getInstancia().findAll());
        modelo.put("facturas", CarritoService.getInstancia().findAllFacturas());
        modelo.put("productos", ProductoService.getInstancia().findAll());
        modelo.put("carrito", carrito);
        modelo.put("bag", (carrito.getListaItems() != null) ? carrito.getCantidad() : 0);
        modelo.put("title", titulo);

        if(usuario != null){
            modelo.put("usuario", usuario);
            modelo.put("rol", ctx.sessionAttribute("rol"));
        }

        return modelo;
    }
}
