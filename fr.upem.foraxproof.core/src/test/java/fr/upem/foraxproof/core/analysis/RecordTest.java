package fr.upem.foraxproof.core.analysis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecordTest {
    @Test
    void testError() {
        Location location = new Location.Builder()
                .setType(JavaType.CLASS)
                .setSource("Test")
                .toLocation();

        Record record = Record.error(location, "Test", "Test");
        assertEquals(Level.ERROR, record.getLevel());
        assertEquals("Test", record.getRule());
    }
    @Test
    void testWarning() {
        Location location = new Location.Builder()
                .setType(JavaType.CLASS)
                .setSource("Test")
                .toLocation();

        Record record = Record.warning(location,"Test2", "Test");
        assertEquals(Level.WARNING, record.getLevel());
        assertEquals("Test2", record.getRule());
    }
    @Test
    void testInfo() {
        Location location = new Location.Builder()
                .setType(JavaType.CLASS)
                .setSource("Test")
                .toLocation();

        Record record = Record.info(location,"Test3", "Test");
        assertEquals(Level.INFO, record.getLevel());
        assertEquals("Test3", record.getRule());
    }
    @Test
    void testNullPointerException() {
        assertThrows(NullPointerException.class, () -> Record.error(null, null, null));
    }
}
