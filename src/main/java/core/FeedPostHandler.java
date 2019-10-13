package core;

import heleprs.CassandraConnector;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;

public class FeedPostHandler implements Handler<RoutingContext> {

    private final EventBus bus;
    private static final Logger LOGGER = Logger.getLogger(FeedPostHandler.class);

    public FeedPostHandler(EventBus bus) {
        this.bus = bus;
    }

    @Override
    public void handle(final RoutingContext routingContext) {
        final JsonObject data = routingContext.getBodyAsJson();
        bus.send("feed.insert", data);
        routingContext.response().end();
    }
}
