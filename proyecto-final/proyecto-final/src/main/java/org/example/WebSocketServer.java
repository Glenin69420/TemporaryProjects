package org.example;

import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebSocketServer {

    private final MongoDatabase database;

    public WebSocketServer(MongoDatabase database) {
        this.database = database;
    }

    public void configurarWebSocket(Javalin app) {
        app.ws("/sync", ws -> {
            ws.onConnect(ctx -> System.out.println("Cliente conectado: " + ctx.sessionId()));

            ws.onMessage(ctx -> {
                String datosSincronizados = ctx.message();
                System.out.println("Datos recibidos para sincronización: " + datosSincronizados);

                // Guardar los datos recibidos en MongoDB
                guardarDatosEnMongo(datosSincronizados);

                // Enviar confirmación al cliente
                ctx.send("Sincronización exitosa");
            });

            ws.onClose(ctx -> System.out.println("Conexión cerrada: " + ctx.sessionId()));
        });
    }

    private void guardarDatosEnMongo(String datosJson) {
        try {
            // Convertir JSON en lista de documentos
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> listaDatos = mapper.readValue(datosJson, List.class);

            List<Document> documentos = new ArrayList<>();
            for (Map<String, Object> dato : listaDatos) {
                documentos.add(new Document(dato));
            }

            database.getCollection("formularios").insertMany(documentos);
        } catch (Exception e) {
            System.err.println("Error al guardar los datos en MongoDB: " + e.getMessage());
        }
    }
}
