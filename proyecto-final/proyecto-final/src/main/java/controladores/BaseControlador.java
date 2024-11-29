package controladores;

import Servicios.FormularioService;
import Servicios.UsuarioService;
import entidades.Usuario;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseControlador {

    protected Javalin app;
    protected FormularioService formularioService;
    protected UsuarioService usuarioService;

    public BaseControlador(Javalin app, FormularioService formularioService, UsuarioService usuarioService) {
        this.app = app;
        this.formularioService = formularioService;
        this.usuarioService = usuarioService;
    }

    abstract public void aplicarRutas();

    protected Map<String, Object> generarModeloSesion(Context ctx, String titulo) {
        Map<String, Object> modelo = new HashMap<>();

        if (ctx.sessionAttribute("usuario") == null) {
            ctx.redirect("/login");
        } else {
            Usuario usuario = ctx.sessionAttribute("usuario"); // Obtén el usuario de la sesión
            modelo.put("usuario", usuario);
            modelo.put("title", titulo);
            modelo.put("formularios", formularioService.buscarPorUsuario(usuario.getUsuario())); // Agrega los formularios al modelo
        }

        return modelo;
    }
}