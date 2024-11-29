package controladores;

import io.javalin.Javalin;

public class ControladorLogin extends BaseControlador {
    public ControladorLogin(Javalin app) {super(app);}

    @Override
    public void aplicarRutas() {
        app.get("/login", ctx -> {ctx.render("public/login.html", generarModeloSesion(ctx, "Login"));
        });

        app.post("/login", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            if (username != null && password != null) {
                ctx.sessionAttribute("usuario", username);
                if(username.equals("admin")){
                    ctx.sessionAttribute("rol", true);
                } else {
                    ctx.sessionAttribute("rol", false);
                }
                ctx.redirect("/");
            } else {
                ctx.redirect("/login?error=Invalid username or password");
            }
        });
    }
}

