package controladores;

import Servicios.FormularioService;
import Servicios.UsuarioService;
import io.javalin.Javalin;

import java.util.Map;

public class ControladorIndex extends BaseControlador {

    public ControladorIndex(Javalin app, FormularioService formularioService, UsuarioService usuarioService) {
        super(app, formularioService, usuarioService);
    }

    @Override
    public void aplicarRutas() {
        app.get("/", ctx -> {
            Map<String, Object> modelo = generarModeloSesion(ctx, "Inicio");
            modelo.put("todosLosFormularios", formularioService.obtenerTodosLosFormularios());
            modelo.put("usuarios", usuarioService.obtenerTodosLosUsuarios());
            ctx.render("publico/interfaces/index.html", modelo);
        });
    }
}