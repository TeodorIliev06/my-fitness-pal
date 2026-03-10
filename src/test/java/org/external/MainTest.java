package org.external;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MainTest {
    @Test
    void testAdd() {
        Main main = new Main();
        assertEquals(3, main.add(1, 2));
    }
}
