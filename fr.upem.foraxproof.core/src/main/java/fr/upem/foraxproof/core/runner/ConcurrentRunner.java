package fr.upem.foraxproof.core.runner;

import fr.upem.foraxproof.core.event.EventDispatcher;
import fr.upem.foraxproof.core.event.app.EndEvent;
import fr.upem.foraxproof.core.event.app.StartEvent;
import fr.upem.foraxproof.core.visitor.EventVisitor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;

/**
 * A ConcurrentRunner is an implementation of a Runner that is able
 * to process events in a concurrent way.
 * Time improvements comparing to DefaultRunner with big amount of classes to process.
 */
public final class ConcurrentRunner implements Runner {
    private final EventDispatcher dispatcher;
    private final ExecutorService executors;

    /**
     * Constructs a new ConcurrentRunner.
     * @param dispatcher the EventDispatcher to use.
     * @param nbThread the number of threads to use.
     */
    public ConcurrentRunner(EventDispatcher dispatcher, int nbThread) {
        this.dispatcher = dispatcher;
        this.executors = Executors.newFixedThreadPool(nbThread);
    }

    @Override
    public void run(JarFile file) throws IOException {
        dispatcher.dispatch(new StartEvent());
        Runners.iterateOnJarFile(file, entry -> executors.submit(() -> Runners.runSecurely(file, entry, new EventVisitor(dispatcher), 0)));
        onEnd();
    }

    @Override
    public void run(Collection<String> classes) throws IOException {
        dispatcher.dispatch(new StartEvent());
        for (String clazz : classes) {
            executors.submit(() -> Runners.runSecurely(Paths.get(clazz), new EventVisitor(dispatcher), 0));
        }
        onEnd();
    }

    @Override
    public void run(InputStream inputStream) throws IOException {
        dispatcher.dispatch(new StartEvent());
        Runners.run(inputStream, new EventVisitor(dispatcher), 0);
        dispatcher.dispatch(new EndEvent());
    }

    private void onEnd() {
        try {
            executors.shutdown();
            executors.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            dispatcher.dispatch(new EndEvent());
        }
    }
}
