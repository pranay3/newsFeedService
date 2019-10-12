package core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;

public class firstVerticle extends AbstractVerticle {

    @Override
    public void start() {
        vertx.createHttpServer()
                .requestHandler(r -> {
                    r.response().end("<h1>Welcome to Pranay's Home Server.</h1>  " +
                            "<h3>This is a Vert.x 3 application</h3>");
                })
                .listen(7070, res -> {
                    if (res.succeeded()) {
                        System.out.println("Server is now listening!");
                    } else {
                        System.out.println("Failed to bind!");
                    }
                });
    }


}
