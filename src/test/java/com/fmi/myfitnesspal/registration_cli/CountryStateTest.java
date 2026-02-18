package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.UserBuilder;
import com.fmi.myfitnesspal.user.country.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CountryStateTest {

    private CountryState countryState;

    @BeforeEach
    public void setUp() {
        countryState = new CountryState();
    }

    @Test
    public void testPerformStateValidInputCountry() {
        UserBuilder builder = new UserBuilder();
        Scanner scanner = new Scanner(new ByteArrayInputStream("US\n".getBytes()));
        countryState.performState(builder, scanner);
        assertEquals(Country.US, builder.build().country());
    }

    @Test
    public void testPerformStateInvalidInputCountry() {
        UserBuilder builder = new UserBuilder();
        Scanner scanner = new Scanner(new ByteArrayInputStream("INVALID\n".getBytes()));
        assertThrows(IllegalArgumentException.class, () -> countryState.performState(builder, scanner));
    }
}
