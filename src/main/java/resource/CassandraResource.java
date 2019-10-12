package resource;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import heleprs.CassandraConnector;
import model.Feed;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

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
                .append("FEEDS").append("(")
                .append("id uuid PRIMARY KEY, ")
                .append("uid uuid,")
                .append("content text);").append("time timestamp");


        String query = sb.toString();
        SESSION.execute(query);
    }

    public void insertFeed(Feed feed) {
        StringBuilder sb = new StringBuilder("INSERT INTO ")
                .append("FEEDS")
                .append("(id, title) ")
                .append("VALUES (")
                .append(feed.id())
                .append(", '")
                .append(feed.uid())
                .append(", '")
                .append(feed.content())
                .append(", '")
                .append(feed.time())
                .append("');");
        String query = sb.toString();
        SESSION.execute(query);
    }

    public List<Feed> searchFeedsByTime(LocalDateTime t) {
        StringBuilder sb = new StringBuilder("SELECT * FROM FEEDS WHERE time > ").append(t);

        String query = sb.toString();
        ResultSet rs = SESSION.execute(query);

        List<Feed> feeds = new ArrayList<>();

        rs.forEach(r ->
            feeds.add (new Feed.Builder().id(r.getUUID("id")).uid(r.getUUID("uid")).content(r.getString("content"))
                    .time(LocalDateTime.ofInstant(r.getTimestamp("time").toInstant(),
                            ZoneId.systemDefault())).build())

        );
        return feeds;
    }
}
