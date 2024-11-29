package Servicios;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entidades.Formulario;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class FormularioService {

    private final MongoCollection<Document> formularioCollection;

    public FormularioService(MongoDatabase database) {
        this.formularioCollection = database.getCollection("formularios");
    }

    public Formulario crearFormulario(String nombre, String apellido, String telefono, String nivelEscolar, double latitud, double longitud, String mimeType, String fotoBase64, String usuario) {
        Document doc = new Document("nombre", nombre)
                .append("apellido", apellido)
                .append("telefono", telefono)
                .append("nivelEscolar", nivelEscolar)
                .append("latitud", latitud)
                .append("longitud", longitud)
                .append("usuario", usuario)
                .append("mimeType", mimeType)
                .append("fotoBase64", fotoBase64);
        formularioCollection.insertOne(doc);

        Formulario nuevoFormulario = new Formulario(nombre, apellido, telefono, nivelEscolar, latitud, longitud, mimeType, fotoBase64, usuario);
        nuevoFormulario.setId(doc.getObjectId("_id"));
        return nuevoFormulario;
    }

    public Formulario buscarPorId(String id) {
        Document query = new Document("_id", new ObjectId(id));
        Document result = formularioCollection.find(query).first();
        if (result != null) {
            return documentToFormulario(result);
        }
        return null;
    }

    public List<Formulario> buscarPorUsuario(String usuario) {
        Document query = new Document("usuario", usuario);
        List<Formulario> formularios = new ArrayList<>();
        for (Document doc : formularioCollection.find(query)) {
            formularios.add(documentToFormulario(doc));
        }
        return formularios;
    }

    public Formulario actualizarFormulario(String id, Formulario formularioActualizado) {
        Document query = new Document("_id", new ObjectId(id));
        Document update = new Document("$set", new Document("nombre", formularioActualizado.getNombre())
                .append("apellido", formularioActualizado.getApellido())
                .append("telefono", formularioActualizado.getTelefono())
                .append("nivelEscolar", formularioActualizado.getNivelEscolar())
                .append("usuario", formularioActualizado.getUsuario())
                .append("latitud", formularioActualizado.getLatitud())
                .append("longitud", formularioActualizado.getLongitud()));

        formularioCollection.updateOne(query, update);
        return buscarPorId(id); // Devolver el formulario actualizado
    }

    public boolean eliminarFormulario(String id) {
        Document query = new Document("_id", new ObjectId(id));
        return formularioCollection.deleteOne(query).getDeletedCount() > 0;
    }

    public List<Formulario> obtenerTodosLosFormularios() {
        List<Formulario> formularios = new ArrayList<>();
        for (Document doc : formularioCollection.find()) {
            formularios.add(documentToFormulario(doc));
        }
        return formularios;
    }


    private Formulario documentToFormulario(Document doc) {
        Formulario formulario = new Formulario(
                doc.getString("nombre"),
                doc.getString("apellido"),
                doc.getString("telefono"),
                doc.getString("nivelEscolar"),
                doc.getDouble("latitud"),
                doc.getDouble("longitud"),
                doc.getString("mimeType"),
                doc.getString("fotoBase64"),
                doc.getString("usuario")
        );
        formulario.setId(doc.getObjectId("_id"));
        return formulario;
    }
}
