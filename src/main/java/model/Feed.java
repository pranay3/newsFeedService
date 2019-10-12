package model;

import org.immutables.value.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value.Style(visibility = Value.Style.ImplementationVisibility.PACKAGE, overshadowImplementation = true)
@Value.Immutable
public interface Feed {

     UUID id();

     UUID uid();

     String content();

     LocalDateTime time();

    class Builder extends ImmutableFeed.Builder {
    }
}
