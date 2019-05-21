package fr.upem.foraxproof.core.runner;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class Runners {
    public static void iterateOnJarFile(JarFile file, Consumer<JarEntry> consumer) {
        Iterator<JarEntry> it = file.entries().asIterator();
        while (it.hasNext()) {
            JarEntry entry = it.next();
            if (!entry.getName().endsWith(".class")) {
                continue;
            }
            consumer.accept(entry);
        }
    }

    public static void run(URLClassLoader urlClassLoader, String clazz, ClassVisitor visitor, int flags) throws IOException {
        URL url = urlClassLoader.findResource(clazz);
        if (url != null) {
            Runners.runFromURL(url, visitor, flags);
        } else {
            Runners.runFromSystem(clazz, visitor, flags);
        }
    }

    public static void run(InputStream inputStream, ClassVisitor visitor, int flags) throws IOException {
        ClassReader reader = new ClassReader(inputStream);
        reader.accept(visitor, flags);
    }

    public static void runSecurely(Path path, ClassVisitor visitor, int flags) {
        try (InputStream inputStream = Files.newInputStream(path)) {
            Runners.run(inputStream, visitor, flags);
        } catch (IOException e) {
            System.err.println(e.getMessage() + " " + path);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            throw e;
        }
    }

    public static void runSecurely(JarFile file, JarEntry entry, ClassVisitor visitor, int flags) {
        try (InputStream inputStream = file.getInputStream(entry)) {
            Runners.run(inputStream, visitor, flags);
        } catch (IOException e) {
            System.err.println(e.getMessage() + " " + entry.getName());
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            throw e;
        }
    }

    private static void runFromURL(URL url, ClassVisitor visitor, int flags) throws IOException {
        try (InputStream inputStream = url.openStream()) {
            Runners.run(inputStream, visitor, flags);
        }
    }

    private static void runFromSystem(String clazz, ClassVisitor visitor, int flags) throws IOException {
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(clazz)) {
            Runners.run(inputStream, visitor, flags);
        }
    }
}
