package fr.upem.foraxproof.main;

import fr.upem.foraxproof.core.event.Registrable;
import fr.upem.foraxproof.core.event.EventManager;
import fr.upem.foraxproof.core.exporter.Exporter;
import fr.upem.foraxproof.core.handler.AddRecordHandler;
import fr.upem.foraxproof.core.handler.AdditionalHandler;
import fr.upem.foraxproof.core.handler.CounterHandler;
import fr.upem.foraxproof.core.handler.TimeHandler;
import fr.upem.foraxproof.core.runner.Runner;
import fr.upem.foraxproof.rest.Server;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import static fr.upem.foraxproof.main.CommandLineParser.Option;

public class Main {
    private static Option parseOption(String[] args) {
        Option option;
        try {
            option = CommandLineParser.parseArguments(args);
            System.out.println(option);
        } catch (IllegalArgumentException e) {
            System.err.println("[Error] " + e.getMessage());
            System.err.println(CommandLineParser.getHelpMessage());
            throw e;
        }
        return option;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static URLClassLoader urlClassLoader(Optional<File> file, Optional<JarURLConnection> connection, HashSet<String> classes) throws MalformedURLException {
        HashSet<URL> urls = new HashSet<>();
        if (file.isPresent()) {
            populateURL(file.get(), urls);
        }
        connection.ifPresent(c -> populateURL(c, urls));
        populateURL(classes, urls);
        return new URLClassLoader(urls.toArray(new URL[urls.size()]));
    }

    private static void populateURL(File file, HashSet<URL> list) throws MalformedURLException {
        if (file != null) {
            list.add(file.toURI().toURL());
        }
    }

    private static void populateURL(JarURLConnection connection, HashSet<URL> list) {
        if (connection != null) {
            list.add(connection.getJarFileURL());
        }
    }

    private static void populateURL(HashSet<String> classes, HashSet<URL> list) throws MalformedURLException {
        for (String clazz : classes) {
            Path path = Paths.get(clazz);
            list.add(path.getParent().toUri().toURL());
        }
    }

    private static Stream<Registrable> registrableStream(Option option, URLClassLoader urlClassLoader) {
        Exporter exporter = OptionLoader.exporter(option);
        Stream<Registrable> rules = OptionLoader.rules(option);
        Stream<Registrable> handlers = Stream.of(new AddRecordHandler(exporter), new AdditionalHandler(urlClassLoader), new CounterHandler(), new TimeHandler());
        return Stream.concat(rules, handlers);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static void run(Runner runner, Optional<File> file, Optional<JarURLConnection> connection, Option option) throws IOException {
        if (file.isPresent()) {
            run(runner, file.get());
        }
        if (connection.isPresent()) {
            run(runner, connection.get());
        }
        run(runner, option.inputClasses);
    }

    private static void run(Runner runner, File file) throws IOException {
        try (JarFile jar = new JarFile(file)) {
            runner.run(jar);
        }
    }

    private static void run(Runner runner, JarURLConnection connection) throws IOException {
        try (JarFile jar = connection.getJarFile()) {
            runner.run(jar);
        }
    }

    private static void run(Runner runner, HashSet<String> classes) throws IOException {
        if (!classes.isEmpty()) {
            runner.run(classes);
        }
    }

    private static void server(Option option, String[] args) {
        if (option.server) {
            Server.run(args);
        }
    }

    public static void main(String[] args) throws Exception {
        Option option = parseOption(args);
        Optional<File> file = OptionLoader.file(option);
        Optional<JarURLConnection> connection = OptionLoader.jarURLConnection(option);
        EventManager manager = new EventManager(registrableStream(option, urlClassLoader(file, connection, option.inputClasses)));
        run(OptionLoader.runner(option, manager), file, connection, option);
        server(option, args);
    }
}
