package fr.upem.foraxproof.core.runner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.jar.JarFile;

/**
 * This interface defines a Runner. A runner is used to
 * process some classes with rules.
 */
public interface Runner {
    /**
     * Take a JarFile as parameter to process all the classes contained
     * in that JarFile with the rules.
     * @param file the JarFile to process.
     * @throws IOException file exception
     */
    void run(JarFile file) throws IOException;

    /**
     * Take a collection of classes as parameter to process all the classes contained
     * in that collection with the rules.
     * @param classes the classes to process.
     * @throws IOException file exception
     */
    void run(Collection<String> classes) throws IOException;

    /**
     * Take an InputStream as parameter to process the class contained
     * in that InputStream with the rules.
     * @param inputStream the InputStream of the class to process.
     * @throws IOException file exception
     */
    void run(InputStream inputStream) throws IOException;
}
