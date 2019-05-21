package fr.upem.foraxproof.impl;

import fr.upem.foraxproof.core.runner.DefaultRunner;
import fr.upem.foraxproof.core.event.EventManager;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractRuleTest {
    @Test
    void testRun() throws IOException, XMLStreamException {
        AbstractRule rule = new AbstractRule();
        TestHandler test = new TestHandler();
        EventManager manager = new EventManager(Stream.of(rule, test));
        try (InputStream inputStream = Files.newInputStream(Paths.get("../resources/ExampleClass.class"))) {
            new DefaultRunner(manager).run(inputStream);
            assertEquals(1, test.getErrors());
        }
    }
}
