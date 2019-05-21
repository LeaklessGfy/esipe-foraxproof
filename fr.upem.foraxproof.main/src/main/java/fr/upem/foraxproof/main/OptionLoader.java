package fr.upem.foraxproof.main;

import fr.upem.foraxproof.core.ForaxProof;
import fr.upem.foraxproof.core.event.EventDispatcher;
import fr.upem.foraxproof.core.event.Registrable;
import fr.upem.foraxproof.core.exporter.Exporter;
import fr.upem.foraxproof.core.exporter.ExporterComposite;
import fr.upem.foraxproof.core.exporter.PrintExporter;
import fr.upem.foraxproof.core.exporter.SQLiteExporter;
import fr.upem.foraxproof.core.exporter.XMLExporter;
import fr.upem.foraxproof.core.Rule;
import fr.upem.foraxproof.core.runner.DefaultRunner;
import fr.upem.foraxproof.core.runner.ConcurrentRunner;
import fr.upem.foraxproof.core.runner.Runner;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.io.File;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fr.upem.foraxproof.main.CommandLineParser.Option;

class OptionLoader {
    static Optional<File> file(Option option) {
        if (option.inputJar == null) {
            return Optional.empty();
        }
        return Optional.of(Paths.get(option.inputJar).toFile());
    }

    static Optional<JarURLConnection> jarURLConnection(Option option) throws IOException {
        if (option.inputUrl == null) {
            return Optional.empty();
        }
        URL url = new URL("jar:" + option.inputUrl + "!/");
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        return Optional.of(jarURLConnection);
    }

    @ForaxProof("Impossible to reduce this, we use a stream here")
    static Stream<Registrable> rules(Option options) {
        ServiceLoader<Registrable> loader = ServiceLoader.load(Registrable.class, Registrable.class.getClassLoader());
        System.out.println("Rules {");
        List<Registrable> rules = loader.stream()
                .filter(ruleProvider -> {
                    Rule rule = (Rule) ruleProvider.type().getAnnotation(Rule.class);
                    return rule != null && (options.allRules || options.rules.contains(rule.value()));
                })
                .map(ServiceLoader.Provider::get)
                .peek(r -> System.out.println("\t" + r))
                .collect(Collectors.toList());
        System.out.println("}");
        return rules.stream();
    }

    static Runner runner(Option option, EventDispatcher dispatcher) {
        if (option.mode.equals("concurrent")) {
            return new ConcurrentRunner(dispatcher, option.thread);
        }
        return new DefaultRunner(dispatcher);
    }

    static Exporter exporter(Option option) {
        String[] array = option.exporter.split(",");
        if (array.length < 2) {
            return stringToExporter(array[0]);
        }
        HashSet<String> setExporters = new HashSet<>();
        setExporters.addAll(Arrays.asList(array));
        Collection<Exporter> exporters = setExporters.stream().map(OptionLoader::stringToExporter).collect(Collectors.toList());
        return new ExporterComposite(exporters);
    }

    @ForaxProof("Impossible to reduce here, use of switch case")
    private static Exporter stringToExporter(String ex) {
        try {
            switch (ex) {
                case "sql":
                    return SQLiteExporter.build();
                case "xml":
                    return XMLExporter.build();
                case "print":
                    return new PrintExporter();
            }
        } catch (XMLStreamException | SQLException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Unexpected exporter " + ex);
    }
}
