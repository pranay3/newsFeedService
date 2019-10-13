package core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import heleprs.CassandraConnector;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import model.Feed;
import org.apache.log4j.Logger;
import resource.CassandraDao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class cassandraVerticle extends AbstractVerticle {

    private static final Logger LOGGER = Logger.getLogger(cassandraVerticle.class);

    @Override
    public void start() throws Exception {
        LOGGER.info("Starting cassandra verticle");
        vertx.executeBlocking(s->{CassandraConnector.connect("127.0.0.1", 9042);
        CassandraDao.INSTANCE.createKeyspace("newsfeed","SimpleStrategy",1);
        CassandraDao.INSTANCE.createFeedsTable();},
                res -> {
                    if (res.succeeded())
                        LOGGER.info("Cassandra initial setup complete.");
                    else
                        LOGGER.info("Initial setup failed: ", res.cause());
                    });

         MessageConsumer<JsonObject> consumer=  vertx.eventBus().consumer("feed.insert");
         consumer.handler(f -> {
            LOGGER.info("Received feed insert request");
            final JsonObject feedJson = new JsonObject(f.body().toString());
            final Feed feed = new Feed.Builder().id(UUID.randomUUID()).uid(UUID.fromString(feedJson.getString("userId"))).content(feedJson.getString("content")).time(LocalDateTime.now()).build();
            vertx.executeBlocking(a->CassandraDao.INSTANCE.insertFeed(feed),res -> {
                if (res.succeeded())
                    LOGGER.info("Feed Insert succeeded.");
                else
                    LOGGER.info("Feed insert failed: ", res.cause());
            });
        });

        vertx.eventBus().consumer("feed.get", f -> {
            LOGGER.info("Received get feeds request");
            final JsonObject req = new JsonObject(f.body().toString());
            vertx.executeBlocking(b->{
                List<Feed> feeds = CassandraDao.INSTANCE.searchFeedsByUserAndTime(req.getString("userId"), LocalDateTime.parse(req.getString("start"),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),LocalDateTime.parse(req.getString("end"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                try {
                    f.reply(new ObjectMapper().writeValueAsString(feeds));
                } catch (JsonProcessingException e) {
                    f.fail(500, e.getMessage());
                }
            },res -> {
                if (res.succeeded())
                    LOGGER.info("Feeds retrieval succeeded.");
                else
                    LOGGER.info("Feeds retrieval failed: ", res.cause());
                    f.fail(500, res.cause().getMessage());
            });

        });
    }
}
