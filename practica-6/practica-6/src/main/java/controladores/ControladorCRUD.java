package controladores;
import clases.Comentario;
import clases.Producto;
import clases.Usuario;
import io.javalin.Javalin;
import org.example.Main;
import servicios.ComentarioService;
import servicios.ProductoService;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


public class ControladorCRUD extends BaseControlador {

    public ControladorCRUD(Javalin app) {super(app);}

    @Override
    public void aplicarRutas() {
        app.get("/CRUD", ctx -> {ctx.render("public/CRUD.html", generarModeloSesion(ctx,"CRUD"));});
        app.get("/agregar", ctx -> {ctx.render("public/agregar.html", generarModeloSesion(ctx,"Agregar"));});

        app.post("/agregar", ctx -> {
            String nombre = ctx.formParam("nombre");
            double precio = Double.parseDouble(ctx.formParam("precio"));
            String descripcion = ctx.formParam("descripcion");
            ctx.uploadedFiles("foto").forEach(uploadedFile -> {
                try {
                    byte[] bytes = uploadedFile.content().readAllBytes();
                    String encodedString = Base64.getEncoder().encodeToString(bytes);
                    ProductoService.getInstancia().crear(new Producto(nombre, precio, descripcion, uploadedFile.contentType(), encodedString));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ctx.redirect("/CRUD");
            });
        });


        app.post("/eliminar", ctx -> {
            String idStr = ctx.formParam("id");
            int id = Integer.parseInt(idStr);
            ProductoService.getInstancia().eliminar(id);
            ctx.redirect("/CRUD");
        });

        app.post("/editar", ctx ->{
            int id = Integer.parseInt(ctx.formParam("id"));
            Producto producto = ProductoService.getInstancia().find(id);
            ProductoService.getInstancia().editar(producto);
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("title", "Editar");
            modelo.put("producto", producto);
            ctx.render("public/agregar.html", modelo);
        });

        app.post("/cambiar", ctx -> {
            int id = Integer.parseInt(ctx.formParam("id"));
            String nombre = ctx.formParam("nombre");
            double precio = Double.parseDouble(ctx.formParam("precio"));
            String descripcion = ctx.formParam("descripcion");
            ctx.uploadedFiles("foto").forEach(uploadedFile -> {
                try {
                    byte[] bytes = uploadedFile.content().readAllBytes();
                    String encodedString = Base64.getEncoder().encodeToString(bytes);
                    Producto productoNuevo = new Producto(nombre, precio, descripcion, uploadedFile.contentType(), encodedString);
                    Producto productoCambio = ProductoService.getInstancia().find(id);
                    productoCambio.mezclar(productoNuevo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ctx.redirect("/CRUD");
            });
        });

        app.post("/guardar", ctx -> {
            String nombre = ctx.formParam("nombre");
            double precio = Double.parseDouble(ctx.formParam("precio"));
            String descripcion = ctx.formParam("descripcion");
            if (nombre != null && precio != 0 && descripcion != null) {
                ctx.uploadedFiles("foto").forEach(uploadedFile -> {
                    try {
                        byte[] bytes = uploadedFile.content().readAllBytes();
                        String encodedString = Base64.getEncoder().encodeToString(bytes);
                        ProductoService.getInstancia().crear(new Producto(nombre, precio, descripcion, uploadedFile.contentType(), encodedString));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ctx.redirect("/CRUD");
                });

            } else {
                ctx.redirect("/login?error=Invalid username or password");
            }
        });

        app.post("/producto", ctx -> {
            String idProductoStr = ctx.formParam("idProducto");
            int idProducto = Integer.parseInt(idProductoStr);

            Producto producto = ProductoService.getInstancia().find(idProducto);

            Map<String, Object> modelo = new HashMap<>();
            modelo.put("title", "Detalles");
            modelo.put("producto", producto);
            String usuario = ctx.sessionAttribute("usuario");
            if(usuario != null){
                modelo.put("usuario", usuario);
                modelo.put("rol", ctx.sessionAttribute("rol"));
            }
            ctx.render("public/producto_detalle.html", modelo);
        });

        app.post("/agregarComentario", ctx ->{
           String comentario = ctx.formParam("comentario");
           int idProducto = Integer.parseInt(ctx.formParam("idProducto"));
           Producto producto = ProductoService.getInstancia().find(idProducto);
           Comentario coment = new Comentario(comentario, producto, ctx.sessionAttribute("usuario"));
           producto.getComentarios().add(coment);
           ComentarioService.getInstancia().crear(coment);
           ctx.redirect("/tienda");
        });

//        app.post("/eliminarComentario", ctx -> {
//            int idComentario = Integer.parseInt(ctx.formParam("idComentario"));
//            int idProducto = ComentarioService.getInstancia().find(idComentario).getProducto().getId();
//            Producto producto = ProductoService.getInstancia().find(idProducto);
//
//            producto.getComentarios().remove(ComentarioService.getInstancia().find(idComentario));
//            ComentarioService.getInstancia().eliminar(idComentario);
//
//            ctx.redirect("/tienda");
//        });


    }

}
