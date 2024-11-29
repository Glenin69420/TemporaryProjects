package controladores;

import clases.Carrito;
import clases.CarritoItem;
import clases.Producto;
import io.javalin.Javalin;
import servicios.CarritoItemService;
import servicios.CarritoService;
import servicios.ProductoService;

import static org.example.Main.enviarActualizacionDashboard;

public class ControladorCarrito extends BaseControlador{
    public ControladorCarrito(Javalin app) {super(app);}

    @Override
    public void aplicarRutas() {
        app.get("/tienda", ctx -> {ctx.render("public/tienda.html", generarModeloSesion(ctx,"tienda"));});

        app.post("/carrito", ctx -> {
            int id = Integer.parseInt(ctx.formParam("id"));
            String cantidadStr = ctx.formParam("cantidad");
            int cantidad;
            if(cantidadStr.equals(null)){
                cantidad = 0;
            } else {
                cantidad = Integer.parseInt(cantidadStr);
            }

            //Agregar Items al carrito
            Producto producto = ProductoService.getInstancia().find(id);
            Carrito carrito = ctx.sessionAttribute("carrito");
            CarritoItem item = new CarritoItem(producto, cantidad, carrito);
            carrito.agregarItem(item);

            ctx.sessionAttribute("carrito", carrito);

            ctx.redirect("/tienda");
        });

        app.get("/carrito", ctx -> {ctx.render("public/CRUD.html", generarModeloSesion(ctx,"carrito"));});

        app.post("/eliminarDelCarrito", ctx ->{
            int id = Integer.parseInt(ctx.formParam("id"));
            Carrito carrito = ctx.sessionAttribute("carrito");
            carrito.eliminarItem(id);
            ctx.sessionAttribute("carrito", carrito);
            ctx.redirect("/carrito");
        });

        app.post("/pagar", ctx -> {
            Carrito carrito = ctx.sessionAttribute("carrito");
            carrito.setOwner(ctx.sessionAttribute("usuario"));
            carrito.setPagado(true);
            CarritoService.getInstancia().crear(carrito);

            // Envía datos actualizados al dashboard
            enviarActualizacionDashboard();

            ctx.sessionAttribute("carrito", null);
            ctx.redirect("/");
        });

    }
}
