package fr.upem.foraxproof.core.event;

import java.util.HashMap;
import java.util.function.Consumer;

public final class CallbackManager {
    private final HashMap<Class<?>, Consumer<?>> callbacks = new HashMap<>();

    public <T> void on(Class<T> type, Consumer<T> callback) {
        callbacks.put(type, callback);
    }

    public <T> void call(T event) {
        @SuppressWarnings("unchecked") Consumer<T> callback = (Consumer<T>) callbacks.get(event.getClass());
        if (callback != null) {
            callback.accept(event);
        }
    }

    public boolean hasCallback(Class<?> type) {
        return callbacks.containsKey(type);
    }
}
