package heleprs;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraConnector {

    private static Cluster cluster;

    private static Session session;

    public static void connect(String node, Integer port) {
        Cluster.Builder builder = Cluster.builder().addContactPoint(node);
        if (port != null) {
            builder.withPort(port);
        }
        cluster = builder.build();

        session = cluster.connect();
    }

    public static Session getSession() {
        return session;
    }

    public static void close() {
        session.close();
        cluster.close();
    }
}
