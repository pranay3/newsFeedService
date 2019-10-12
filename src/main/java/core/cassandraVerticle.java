package core;

import heleprs.CassandraConnector;
import io.vertx.core.AbstractVerticle;

public class cassandraVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        super.start();
        CassandraConnector.connect("127.0.0.1",9042);
    }
}
