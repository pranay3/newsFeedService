package core;


import heleprs.CassandraConnector;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import org.apache.log4j.Logger;
import resource.CassandraDao;

public class MainVerticle extends AbstractVerticle {

    private static final Logger LOGGER = Logger.getLogger(MainVerticle.class);

    @Override
    public void start(Promise<Void> promise) throws Exception {
        LOGGER.info("Deploying verticles");
        Promise<String> cassandraVerticleDeployment = Promise.promise();
        vertx.deployVerticle(new cassandraVerticle(), cassandraVerticleDeployment);
        cassandraVerticleDeployment.future().compose(id -> {

            Promise<String> httpVerticleDeployment = Promise.promise();
            vertx.deployVerticle(
                    "core.HttpVerticle",
                    new DeploymentOptions().setInstances(1),
                    httpVerticleDeployment);
            return httpVerticleDeployment.future();

        }).setHandler(ar -> {
            if (ar.succeeded()) {
                LOGGER.info("Successfully deployed all verticles.");
                promise.complete();
            } else {
                LOGGER.info("Deployment failed");
                promise.fail(ar.cause() );
            }
        });
    }
}
