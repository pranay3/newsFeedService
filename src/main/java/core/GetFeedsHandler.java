package core;

import heleprs.CassandraConnector;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;

import java.net.URLDecoder;
import java.util.Optional;
import java.util.UUID;

public class GetFeedsHandler implements Handler<RoutingContext> {

    private final EventBus bus;
    private static final Logger LOGGER = Logger.getLogger(GetFeedsHandler.class);

    public GetFeedsHandler(EventBus bus) {
        this.bus = bus;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        LOGGER.info("Received feeds get request");
        try {
            String uid = routingContext.request().getParam("userId");

            String startTime = routingContext.request().query().split("&")[0].split("=")[1];
            String endTime = routingContext.request().query().split("&")[1].split("=")[1];
            JsonObject req = new JsonObject();
            req.put("userId", uid);
            req.put("start", URLDecoder.decode(startTime));
            req.put("end", URLDecoder.decode(endTime));
            bus.request("feed.get", req.toString(), reply -> {
                if (reply.succeeded())
                    routingContext.request().response().putHeader("content-type", "application/json").end(reply.result().body().toString());
                else
                routingContext.fail(500, reply.cause());
            });
        }
        catch (Exception e){
            LOGGER.error(e);
            routingContext.fail(500);
        }
    }
}
