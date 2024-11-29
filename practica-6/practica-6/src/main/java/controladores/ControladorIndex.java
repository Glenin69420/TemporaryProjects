package controladores;
import io.javalin.Javalin;


public class ControladorIndex extends BaseControlador {

    public ControladorIndex(Javalin app) {super(app);}

    @Override
    public void aplicarRutas() {
        app.get("/", ctx -> {ctx.render("public/index.html", generarModeloSesion(ctx,"Inicio"));});
    }
}
