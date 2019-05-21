package fr.upem.foraxproof.core.runner;

import fr.upem.foraxproof.core.event.EventDispatcher;
import fr.upem.foraxproof.core.event.app.EndEvent;
import fr.upem.foraxproof.core.event.app.StartEvent;
import fr.upem.foraxproof.core.visitor.EventVisitor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.jar.JarFile;

/**
 * A DefaultRunner is a simple runner that can be used to
 * process little amount of classes.
 * If you have a lot of classes to process, use instead a
 * ConcurrentRunner to have better performances.
 */
public final class DefaultRunner implements Runner {
    private final EventDispatcher dispatcher;

    /**
     * Constructs a new DefaultRunner
     * @param dispatcher the dispatcher to use.
     */
    public DefaultRunner(EventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void run(JarFile file) throws IOException {
        dispatcher.dispatch(new StartEvent());
        Runners.iterateOnJarFile(file, entry -> Runners.runSecurely(file, entry, new EventVisitor(dispatcher), 0));
        file.close();
        dispatcher.dispatch(new EndEvent());
    }

    @Override
    public void run(Collection<String> classes) throws IOException {
        dispatcher.dispatch(new StartEvent());
        for (String clazz : classes) {
            Runners.runSecurely(Paths.get(clazz), new EventVisitor(dispatcher), 0);
        }
        dispatcher.dispatch(new EndEvent());
    }

    @Override
    public void run(InputStream inputStream) throws IOException {
        dispatcher.dispatch(new StartEvent());
        Runners.run(inputStream, new EventVisitor(dispatcher), 0);
        dispatcher.dispatch(new EndEvent());
    }
}
