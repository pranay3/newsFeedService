package resource;

import com.datastax.driver.core.Session;
import heleprs.CassandraConnector;

public enum CassandraResource {

    INSTANCE;

    private static final Session SESSION = CassandraConnector.getSession();

    public void createKeyspace(
            String keyspaceName, String replicationStrategy, int replicationFactor) {
        StringBuilder sb =
                new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
                        .append(keyspaceName).append(" WITH replication = {")
                        .append("'class':'").append(replicationStrategy)
                        .append("','replication_factor':").append(replicationFactor)
                        .append("};");

        String query = sb.toString();
        SESSION.execute(query);
    }

    public void createFeedsTable() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append("Feeds").append("(")
                .append("id uuid PRIMARY KEY, ")
                .append("uid uuid,")
                .append("content text);"). append("date date");


        String query = sb.toString();
        SESSION.execute(query);
    }

    public void insertFeed(Feed feed){

    }
}
