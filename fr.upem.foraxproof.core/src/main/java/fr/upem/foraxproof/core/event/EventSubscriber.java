package fr.upem.foraxproof.core.event;

import java.util.function.BiConsumer;

/**
 * EventSubscriber is the interface that describe how a subscriber of event works.
 */
public interface EventSubscriber {
    /**
     * Subscribe a specific listener on a specific event.
     * @param key the class of the event to listen
     * @param listener the BiConsumer to call when this event occur
     * @param <T> the type of the event
     */
    <T> void subscribe(Class<T> key, BiConsumer<T, EventDispatcher> listener);
}
