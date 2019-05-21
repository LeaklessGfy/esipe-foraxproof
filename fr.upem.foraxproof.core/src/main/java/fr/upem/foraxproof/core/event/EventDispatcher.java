package fr.upem.foraxproof.core.event;

/**
 * EventDispatcher is the interface that describe how a dispatcher should work.
 */
public interface EventDispatcher {
    /**
     * Dispatch the following event to listener.
     * @param event the event to dispatch
     * @param <T> the type of event it dispatch and witch listener to call
     */
    <T> void dispatch(T event);

    /**
     * Ask if a listener is setup on a specific event.
     * @param key the asm of the event
     * @param <T> the type of event
     * @return if a listener is setup on this event
     */
    <T> boolean hasListener(Class<T> key);
}
