import org.immutables.value.Value;

import java.time.LocalDateTime;
@Value.Immutable
public abstract class Feed {

    public abstract String id();
    public abstract String uid();
    public abstract String content();
    public abstract LocalDateTime time();
}
