package model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@Value.Immutable
@JsonSerialize(as = ImmutableFeed.class)
@JsonDeserialize(as = ImmutableFeed.class)
public interface Feed {

     UUID id();

     UUID uid();

     String content();

     LocalDateTime time();

    class Builder extends ImmutableFeed.Builder {
    }
}
