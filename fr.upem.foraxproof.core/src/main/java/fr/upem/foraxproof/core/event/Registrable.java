package fr.upem.foraxproof.core.event;

public interface Registrable {
    void register(EventSubscriber subscriber);
}
