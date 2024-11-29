package controladores;

import Servicios.FormularioService;
import Servicios.UsuarioService;
import entidades.Formulario;
import entidades.Usuario;
import io.javalin.Javalin;

import java.util.Map;

public class ControladorFormulario extends BaseControlador {

    public ControladorFormulario(Javalin app, FormularioService formularioService, UsuarioService usuarioService) {
        super(app, formularioService, usuarioService);
    }

    public void aplicarRutas() {
        app.get("/formularios", ctx -> {
            ctx.attribute("formularios", formularioService.obtenerTodosLosFormularios());
            ctx.render("publico/discarded/listarFormularios.html");
        });

        app.get("/NuevoFormulario", ctx -> {
            Map<String, Object> modelo = generarModeloSesion(ctx, "Nuevo Formulario");
            modelo.put("modo", "Crear");
            ctx.render("publico/interfaces/formulario.html", modelo);
        });

        app.post("/NuevoFormulario", ctx -> {
            String nombre = ctx.formParam("nombre");
            String apellido = ctx.formParam("apellido");
            String telefono = ctx.formParam("telefono");
            String nivelEscolar = ctx.formParam("nivelEscolar");
            String ubicacion = ctx.formParam("ubicacion");

            if (ubicacion == null || ubicacion.trim().isEmpty()) {
                ctx.status(400).result("Ubicacion no valida");
                return;
            }

            String[] coords = ubicacion.split(",");
            double latitud = Double.parseDouble(coords[0].trim());
            double longitud = Double.parseDouble(coords[1].trim());

            String fotoBase64 = ctx.formParam("fotoBase64");
            String mimeType = "";

            if (fotoBase64.startsWith("data:image/jpeg")) {
                mimeType = "image/jpeg;base64,";
            } else if (fotoBase64.startsWith("data:image/png")) {
                mimeType = "image/png;base64,";
            }


            Usuario usuario = ctx.sessionAttribute("usuario");


            formularioService.crearFormulario(nombre, apellido, telefono, nivelEscolar, latitud, longitud, mimeType, fotoBase64, usuario.getUsuario());

            ctx.redirect("/");
        });


        app.post("/EliminarFormulario/{id}", ctx -> {
            String id = ctx.pathParam("id");
            boolean eliminado = formularioService.eliminarFormulario(id);
            if (eliminado) {
                ctx.status(204).result("Formulario eliminado con exito");
            } else {
                ctx.status(404).result("Formulario no encontrado");
            }
            ctx.redirect("/");
        });

        app.post("/UpdateFormulario/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Formulario formulario = formularioService.buscarPorId(id);
            String nombre = ctx.formParam("nombre");
            String apellido = ctx.formParam("apellido");
            String telefono = ctx.formParam("telefono");
            String nivelEscolar = ctx.formParam("nivelEscolar");

            Usuario usuario = ctx.sessionAttribute("usuario");

            Formulario formularioActualizado = new Formulario(nombre, apellido, telefono, nivelEscolar, formulario.getLatitud(), formulario.getLongitud(), formulario.getMimeType(), formulario.getFotoBase64(), usuario.getUsuario());
            Formulario resultado = formularioService.actualizarFormulario(id, formularioActualizado);

            if (resultado != null) {
                ctx.redirect("/formularios/" + id);
            } else {
                ctx.status(404).result("Formulario no encontrado");
            }
        });

        app.post("/EditarFormulario/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Formulario formulario = formularioService.buscarPorId(id);
            if (formulario != null) {
                Map<String, Object> modelo = generarModeloSesion(ctx, formulario.getNombre());
                modelo.put("formulario", formulario);
                modelo.put("modo", "Editar");
                ctx.render("publico/interfaces/formulario.html", modelo);
            } else {
                ctx.status(404).result("Formulario no encontrado.");
                System.out.println("Formulario no encontrado.");
            }
        });

        app.get("/formularios/{id}", ctx -> {
            String id = ctx.pathParam("id");
            Formulario formulario = formularioService.buscarPorId(id);
            if (formulario != null) {
                Map<String, Object> modelo = generarModeloSesion(ctx, formulario.getNombre());
                modelo.put("formulario", formulario);
                modelo.put("modo", "Ver");
                ctx.render("publico/interfaces/formulario.html", modelo);
            } else {
                ctx.status(404).result("Formulario no encontrado.");
                System.out.println("Formulario no encontrado.");
            }
        });
    }
}
