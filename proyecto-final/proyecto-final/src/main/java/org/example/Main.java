package org.example;

import Servicios.MongoClientConnection;
import Servicios.FormularioService;
import Servicios.UsuarioService;
import controladores.ControladorFormulario;
import controladores.ControladorGeneral;
import controladores.ControladorIndex;
import controladores.ControladorUsuario;
import entidades.Formulario;
import io.javalin.Javalin;
import com.mongodb.client.MongoDatabase;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Conectar a MongoDB
        MongoDatabase database = new MongoClientConnection().connect();

        // Crear servicios
        FormularioService formularioService = new FormularioService(database);
        UsuarioService usuarioService = new UsuarioService(database);

        // Iniciar Javalin
        var app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "publico";
                staticFiles.location = Location.CLASSPATH;
                staticFiles.precompress = false;
                staticFiles.aliasCheck = null;
            });

            config.fileRenderer(new JavalinThymeleaf());

        }).start(7070);

        app.before("/", ctx -> {
            if (ctx.sessionAttribute("usuario") == null) {
                ctx.redirect("/login");
            }
        });

        app.post("/volver", ctx -> { ctx.redirect("/"); });

        new ControladorIndex(app, formularioService, usuarioService).aplicarRutas();
        new ControladorGeneral(usuarioService).aplicarRutas(app);
        new ControladorUsuario(app, formularioService, usuarioService).aplicarRutas();
        new ControladorFormulario(app, formularioService, usuarioService).aplicarRutas();
    }
}
