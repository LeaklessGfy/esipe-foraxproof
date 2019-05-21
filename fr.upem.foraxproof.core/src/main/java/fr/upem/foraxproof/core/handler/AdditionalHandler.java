package fr.upem.foraxproof.core.handler;

import fr.upem.foraxproof.core.event.EventDispatcher;
import fr.upem.foraxproof.core.event.EventSubscriber;
import fr.upem.foraxproof.core.event.Registrable;
import fr.upem.foraxproof.core.event.app.AddAdditionalEvent;
import fr.upem.foraxproof.core.runner.Runners;
import org.objectweb.asm.ClassVisitor;

import java.io.IOException;
import java.net.URLClassLoader;

/**
 * AdditionalHandler is a registrable that take care to start appropriate reader with specific ClassVisitor.
 */
public final class AdditionalHandler implements Registrable {
    private final URLClassLoader urlClassLoader;

    /**
     * Constructs a new AdditionalHandler with the specified URLClassLoader.
     * @param urlClassLoader the URLClassLoader to use to load classes.
     */
    public AdditionalHandler(URLClassLoader urlClassLoader) {
        this.urlClassLoader = urlClassLoader;
    }

    @Override
    public void register(EventSubscriber subscriber) {
        subscriber.subscribe(AddAdditionalEvent.class, this::onAddAdditional);
    }

    private void onAddAdditional(AddAdditionalEvent event, EventDispatcher dispatcher) {
        String clazz = event.getClazz() + ".class";
        try {
            Runners.run(urlClassLoader, clazz, event.getVisitor(), event.getFlags());
        } catch (IOException e) {
            System.err.println(e.getMessage() + " - " + clazz);
        }
    }

    @Override
    public String toString() {
        return "Additional Analysis Event Handler";
    }
}
