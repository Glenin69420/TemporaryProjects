package Servicios;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import entidades.Usuario;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    private final MongoCollection<Document> usuarioCollection;

    public UsuarioService(MongoDatabase database) {
        this.usuarioCollection = database.getCollection("usuarios");
    }

    // Verificar las credenciales de un usuario
    public Usuario verificarCredenciales(String nombreUsuario, String password) {
        Document userDoc = usuarioCollection.find(
                Filters.and(
                        Filters.eq("_id", nombreUsuario),  // Usar `_id` como el nombre de usuario
                        Filters.eq("password", password)
                )
        ).first();

        if (userDoc != null) {
            return documentToUsuario(userDoc);
        }
        return null;
    }



    // Crear un nuevo usuario
    public Usuario crearUsuario(Usuario usuario) {
        Document doc = new Document("_id", usuario.getUsuario())  // Usamos ObjectId como `_id`
                .append("password", usuario.getPassword())
                .append("nombre", usuario.getNombre())
                .append("admin", usuario.isAdmin());
        usuarioCollection.insertOne(doc);
        return usuario;
    }


    public Usuario buscarPorUsuario(String usuario) {
        Document query = new Document("_id", usuario);
        Document result = usuarioCollection.find(query).first();
        return result != null ? documentToUsuario(result) : null;
    }

    // Actualizar un usuario
    public Usuario actualizarUsuario(String id, Usuario usuarioActualizado) {
        Document query = new Document("_id", id);  // Usar `_id` como String
        Document update = new Document("$set", new Document("password", usuarioActualizado.getPassword())
                .append("nombre", usuarioActualizado.getNombre())
                .append("admin", usuarioActualizado.isAdmin()));

        usuarioCollection.updateOne(query, update);
        return buscarPorUsuario(id);  // Devolver el usuario actualizado
    }

    public boolean eliminarUsuario(String id) {
        Document query = new Document("_id", id);  // Usar `_id` como String
        return usuarioCollection.deleteOne(query).getDeletedCount() > 0;
    }


    // Obtener todos los usuarios
    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        for (Document doc : usuarioCollection.find()) {
            usuarios.add(documentToUsuario(doc));
        }
        return usuarios;
    }

    // Convertir un Document de MongoDB a un objeto Usuario
    private Usuario documentToUsuario(Document doc) {
        return new Usuario(
                doc.getString("_id"),
                doc.getString("password"),
                doc.getString("nombre"),
                doc.getBoolean("admin")
        );
    }
}
