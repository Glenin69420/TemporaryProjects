package controladores;
import io.javalin.Javalin;
import servicios.CarritoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ControladorListas extends BaseControlador {

    public ControladorListas(Javalin app) {super(app);}

    @Override
    public void aplicarRutas() {
        app.get("/listaUsuarios", ctx -> {ctx.render("public/listas.html", generarModeloSesion(ctx,"listaUsuarios"));});
        app.get("/listaFacturas", ctx -> {ctx.render("public/listas.html", generarModeloSesion(ctx,"listaFacturas"));});
        app.get("/dashboard", ctx -> {
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("title", "Gráficos");

            // Agregamos los datos iniciales para el dashboard
            double totalVentas = CarritoService.getInstancia().calcularTotalVentas();
            Map<String, Integer> productosVendidos = CarritoService.getInstancia().obtenerCantidadProductosVendidos();
            List<Map<String, Object>> histogramaVentas = CarritoService.getInstancia().obtenerHistogramaVentas();

            modelo.put("totalVentas", totalVentas);
            modelo.put("productos", productosVendidos);
            modelo.put("histogramaVentas", histogramaVentas);

            ctx.render("public/dashboard.html", modelo);
        });

    }
}