package core;

import heleprs.CassandraConnector;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.log4j.Logger;

public class HttpVerticle extends AbstractVerticle {

    private static final Logger LOGGER = Logger.getLogger(HttpVerticle.class);

    @Override
    public void start() {
        LOGGER.info("Starting HTTP Verticle");
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.get("/feeds/:userId").handler(new GetFeedsHandler(vertx.eventBus()));
        router.post().handler(BodyHandler.create());
        router.post("/feed").handler(new FeedPostHandler(vertx.eventBus()));

        server.requestHandler(router)
                .listen(7070, ar -> {
                    if (ar.succeeded()) {
                        System.out.println("HTTP server running on port 7070");
                    } else {
                        System.out.println("Could not start a HTTP server: " + ar.cause());
                    }
                });
    }

}
