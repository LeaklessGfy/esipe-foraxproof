package fr.upem.foraxproof.core.analysis;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocationTest {
    @Test
    void testToLocation() {
        Location.Builder location = new Location.Builder();
        location.setType(JavaType.FIELD);
        location.setSource("Test");
        Location locationImutable = location.toLocation();
        assertEquals(locationImutable.getType(), JavaType.FIELD);
    }
}
