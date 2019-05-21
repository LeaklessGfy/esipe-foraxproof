package fr.upem.foraxproof.impl;

import fr.upem.foraxproof.core.handler.AdditionalHandler;
import fr.upem.foraxproof.core.runner.DefaultRunner;
import fr.upem.foraxproof.core.event.EventManager;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnonymousRuleTest {
    @Test
    void testRun() throws IOException, XMLStreamException {
        AnonymousRule rule = new AnonymousRule();
        TestHandler test = new TestHandler();
        AdditionalHandler handler = new AdditionalHandler(new URLClassLoader(new URL[0]));
        EventManager manager = new EventManager(Stream.of(rule, test, handler));
        try (InputStream inputStream = Files.newInputStream(Paths.get("../resources/ExampleClass.class"))) {
            new DefaultRunner(manager).run(inputStream);
            assertEquals(0, test.getErrors());
        }
    }

    @Test
    void testNotOverrideAll() throws IOException, XMLStreamException {
        AnonymousRule rule = new AnonymousRule();
        TestHandler test = new TestHandler();
        AdditionalHandler handler = new AdditionalHandler(new URLClassLoader(new URL[0]));
        EventManager manager = new EventManager(Stream.of(rule, test, handler));
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream("java/util/ArrayList.class")) {
            new DefaultRunner(manager).run(inputStream);
            assertEquals(0, test.getErrors());
        }
    }

    @Test
    void testNeedAnonymous() throws IOException, XMLStreamException {
        AnonymousRule rule = new AnonymousRule();
        TestHandler test = new TestHandler();
        AdditionalHandler handler = new AdditionalHandler(new URLClassLoader(new URL[0]));
        EventManager manager = new EventManager(Stream.of(rule, test, handler));
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream("java/util/AbstractCollection.class")) {
            new DefaultRunner(manager).run(inputStream);
            assertEquals(1, test.getErrors());
        }
    }
}
