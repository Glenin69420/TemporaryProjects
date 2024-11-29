package Servicios;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoClientConnection {

    private static MongoClient mongoClient;

    public static MongoDatabase connect() {
        if (mongoClient == null) {
            String connectionString = "mongodb+srv://lenin:1234@cluster0.f6rso.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";

            ServerApi serverApi = ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build();

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(connectionString))
                    .serverApi(serverApi)
                    .build();

            mongoClient = MongoClients.create(settings);
        }

        return mongoClient.getDatabase("PROYECTO-FINAL"); // Cambia "mi_base_de_datos" al nombre de tu base de datos real
    }
}
