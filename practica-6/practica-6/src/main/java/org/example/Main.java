package org.example;

import clases.Comentario;
import clases.Producto;
import com.google.gson.Gson;
import controladores.*;
import io.javalin.Javalin;
import io.javalin.http.sse.SseClient;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsContext;
import org.eclipse.jetty.websocket.api.Session;
import servicios.BootStrapServices;
import servicios.CarritoService;
import servicios.ComentarioService;
import servicios.ProductoService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import util.LocalDateTimeAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static j2html.TagCreator.p;

public class Main {

    //indica el modo de operacion para la base de datos.
    private static String modoConexion = "";

    public static List<Session> usuariosConectados = new ArrayList<>();
    public static List<SseClient> listaSseUsuario = new ArrayList<>();


    public static void main(String[] args) {
        String mensaje = "Hola Mundo en Javalin :-D";
        System.out.println(mensaje);

        if(args.length >= 1){
            modoConexion = args[0];
            System.out.println("Modo de Operacion: "+modoConexion);
        }

        //Iniciando la base de datos.
        if(modoConexion.isEmpty()) {
            BootStrapServices.getInstancia().init();
        }else{
            if(modoConexion.equalsIgnoreCase("update")){
                BootStrapServices.getInstancia().update();
            }
        }

        //Agregando productos de ejemplo.
        generarEjemplos();

        var app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "public";
                staticFiles.location = Location.CLASSPATH;
                staticFiles.precompress=false;
                staticFiles.aliasCheck=null;
            });

            config.fileRenderer(new JavalinThymeleaf());

        }).start(7000);

        app.ws("/mensajeServidor", ws -> {
            ws.onConnect(ctx -> {
                System.out.println("Conexión Iniciada - " + ctx.sessionId());
                usuariosConectados.add(ctx.session);
                enviarCantidadUsuariosConectados();
            });

            ws.onMessage(ctx -> {
                String idComentario = ctx.message(); // Recibe el ID del comentario como un mensaje
                System.out.println("Recibido el ID del comentario: " + idComentario);
                eliminarComentario(idComentario, ctx.session);
            });

            ws.onClose(ctx -> {
                System.out.println("Conexión Cerrada - " + ctx.sessionId());
                usuariosConectados.remove(ctx.session);
                enviarCantidadUsuariosConectados();
            });
        });

        app.ws("/dashboard/ws", ws -> {
            ws.onConnect(ctx -> {
                System.out.println("Dashboard conectado - " + ctx.sessionId());
                usuariosConectados.add(ctx.session);
            });

            ws.onClose(ctx -> {
                System.out.println("Dashboard desconectado - " + ctx.sessionId());
                usuariosConectados.remove(ctx.session);
            });
        });

        new ControladorIndex(app).aplicarRutas();
        new ControladorLogin(app).aplicarRutas();
        new ControladorCarrito(app).aplicarRutas();
        new ControladorCRUD(app).aplicarRutas();
        new ControladorListas(app).aplicarRutas();

        app.post("/volver", ctx ->{ctx.redirect("/");});
    }

    public static void generarEjemplos(){
        if(ProductoService.getInstancia().findAll().isEmpty()){
            byte[] imageBytes1 = null;
            try {
                imageBytes1 = Files.readAllBytes(Paths.get("src/main/resources/public/img/vegetable-item-6.jpg"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String base64Image1 = Base64.getEncoder().encodeToString(imageBytes1);
            ProductoService.getInstancia().crear(new Producto("Cilantro", 100.0, "Esto es un cilantro", "image/jpeg", base64Image1));

            byte[] imageBytes2 = null;
            try {
                imageBytes2 = Files.readAllBytes(Paths.get("src/main/resources/public/img/vegetable-item-5.jpg"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String base64Image2 = Base64.getEncoder().encodeToString(imageBytes2);
            ProductoService.getInstancia().crear(new Producto("Papa", 50.0, "Esto es una papa", "image/jpeg", base64Image2));
        }
    }

    private static void eliminarComentario(String idComentario, Session session) throws IOException {

        boolean eliminado = ComentarioService.getInstancia().eliminar(idComentario);
        System.out.println("Intentando eliminar comentario con ID: " + idComentario);

        if (eliminado) {

            for (Session sesionConectada : usuariosConectados) {
                try {
                    sesionConectada.getRemote().sendString("Comentario con ID " + idComentario + " ha sido eliminado.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            session.getRemote().sendString("Error: No se pudo eliminar el comentario con ID " + idComentario);
        }
    }

    public static void enviarCantidadUsuariosConectados() {
        String mensaje = "Usuarios conectados: " + usuariosConectados.size();
        for (Session sesionConectada : usuariosConectados) {
            try {
                sesionConectada.getRemote().sendString(mensaje);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void enviarActualizacionDashboard() {
        // Configuración de Gson con el adaptador personalizado para LocalDateTime
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        // Obtener datos para enviar al dashboard
        double totalVentas = CarritoService.getInstancia().calcularTotalVentas();
        Map<String, Integer> productosVendidos = CarritoService.getInstancia().obtenerCantidadProductosVendidos();
        List<Map<String, Object>> histogramaVentas = CarritoService.getInstancia().obtenerHistogramaVentas();

        Map<String, Object> data = new HashMap<>();
        data.put("totalVentas", totalVentas);
        data.put("productos", productosVendidos);
        data.put("histogramaVentas", histogramaVentas);

        // Convertir el mapa de datos a JSON y enviarlo por WebSocket
        String jsonData = gson.toJson(data);

        for (Session sesionConectada : usuariosConectados) {
            try {
                sesionConectada.getRemote().sendString(jsonData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}





