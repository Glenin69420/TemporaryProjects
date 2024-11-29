package controladores;

import Servicios.FormularioService;
import Servicios.UsuarioService;
import entidades.Usuario;
import io.javalin.Javalin;

import java.util.Map;

public class ControladorUsuario extends BaseControlador {

    public ControladorUsuario(Javalin app, FormularioService formularioService, UsuarioService usuarioService) {
        super(app, formularioService, usuarioService);
    }

    public void aplicarRutas() {
        // Ruta para mostrar la lista de usuarios
        app.get("/usuarios", ctx -> {
            ctx.attribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
            ctx.render("publico/discarded/listarUsuarios.html");
        });

        app.get("/CrearUsuario", ctx -> {
            Map<String, Object> modelo = generarModeloSesion(ctx, "Nuevo Usuario");
            modelo.put("modo", "Crear");
            ctx.render("publico/interfaces/usuario.html", modelo);
        });

        app.post("/CrearUsuario", ctx -> {
            String usuario = ctx.formParam("usuario");
            String password = ctx.formParam("password");
            String nombre = ctx.formParam("nombre");
            boolean admin = ctx.formParam("admin") != null;

                Usuario nuevoUsuario = new Usuario(usuario, password, nombre, admin);
            usuarioService.crearUsuario(nuevoUsuario);

            ctx.status(201).result("Usuario creado con éxito");
            ctx.redirect("/");
        });

        app.post("/EliminarUsuario/{usuario}", ctx -> {
            String id = ctx.pathParam("usuario");
            boolean eliminado = usuarioService.eliminarUsuario(id);
            if (eliminado) {
                ctx.status(204).result("Usuario eliminado con éxito");
            } else {
                ctx.status(404).result("Usuario no encontrado");
            }
            ctx.redirect("/");
        });

        // Ruta para ver usuario
        app.get("/usuarios/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Usuario usuario = usuarioService.buscarPorUsuario(id);
            if (usuario != null) {
                Map<String, Object> modelo = generarModeloSesion(ctx, usuario.getNombre());
                modelo.put("usuario", usuario);
                modelo.put("modo", "Ver");
                ctx.render("publico/interfaces/usuario.html", modelo);
            } else {
                ctx.status(404).result("Usuario no encontrado");
            }
        });

        // Ruta para editar usuario
        app.post("/EditarUsuario/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Usuario usuario = usuarioService.buscarPorUsuario(id);
            if (usuario != null) {
                Map<String, Object> modelo = generarModeloSesion(ctx, usuario.getUsuario());
                modelo.put("usuario", usuario);
                modelo.put("modo", "Editar");
                ctx.render("publico/interfaces/usuario.html", modelo);
            } else {
                ctx.status(404).result("Usuario no encontrado");
            }
        });

        app.post("/UpdateUsuario/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Usuario user = usuarioService.buscarPorUsuario(id);
            String usuario = ctx.formParam("usuario");
            String password = ctx.formParam("password");
            String nombre = ctx.formParam("nombre");
            boolean admin = ctx.formParam("admin") != null;

            Usuario usuarioActualizado = new Usuario(usuario, password, nombre, admin);
            Usuario resultado = usuarioService.actualizarUsuario(id, usuarioActualizado);

            if (resultado != null) {
                ctx.redirect("/");
            } else {
                ctx.status(404).result("Usuario no encontrado");
            }
        });
    }
}
