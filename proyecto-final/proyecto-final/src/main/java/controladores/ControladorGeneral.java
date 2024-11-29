package controladores;

import Servicios.UsuarioService;
import entidades.Usuario;
import io.javalin.Javalin;

import java.util.Map;

public class ControladorGeneral {
    private final UsuarioService usuarioService;

    public ControladorGeneral(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void aplicarRutas(Javalin app) {
        app.get("/login", ctx -> {
            ctx.render("/publico/interfaces/login.html", Map.of("title", "Iniciar Sesión"));
        });

        app.post("/login", ctx -> {
            String usuario = ctx.formParam("usuario");
            String password = ctx.formParam("password");

            Usuario usuarioExiste = usuarioService.verificarCredenciales(usuario, password);

            if (usuarioExiste != null) {
                System.out.println("Usuario encontrado");
                ctx.sessionAttribute("usuario", usuarioExiste);

                ctx.redirect("/");
            } else {
                System.out.println("Usuario no encontrado o credenciales incorrectas");
                ctx.redirect("/login?error=Usuario no encontrado o credenciales incorrectas");
            }
        });
    }
}
