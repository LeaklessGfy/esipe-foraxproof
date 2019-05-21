package fr.upem.foraxproof.core.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * EventManager is the class that handle every interaction with events.
 * In fact, this allow to subscribe to a specific event and dispatch a specific one.
 * All the listener that are subscribed on an event are called when this event occurred.
 */
public final class EventManager implements EventDispatcher {
    private static class Subscriber implements EventSubscriber {
        private final HashMap<Class<?>, List<BiConsumer<?, EventDispatcher>>> register = new HashMap<>();

        @Override
        public <T> void subscribe(Class<T> key, BiConsumer<T, EventDispatcher> listener) {
            register.computeIfAbsent(key, k -> new ArrayList<>())
                    .add(listener);
        }
    }

    private final Subscriber subscriber;

    /**
     * Construct EventManager from Stream of registrable
     * @param registry stream
     */
    public EventManager(Stream<Registrable> registry) {
        this.subscriber = new Subscriber();
        registry.forEach(c -> c.register(subscriber));
    }

    @Override
    public <T> void dispatch(T event)  {
        List<BiConsumer<?, EventDispatcher>> listeners = subscriber.register.getOrDefault(event.getClass(), List.of());
        listeners.forEach(l -> {
            @SuppressWarnings("unchecked") BiConsumer<T, EventDispatcher> listener = (BiConsumer<T, EventDispatcher>) l;
            listener.accept(event, this);
        });
    }

    @Override
    public <T> boolean hasListener(Class<T> key) {
        return subscriber.register.containsKey(key);
    }
}
