package com.fmi.myfitnesspal.command.user;

import com.fmi.myfitnesspal.exception.InvalidCommandException;
import com.fmi.myfitnesspal.registration_cli.UserRegistration;
import com.fmi.myfitnesspal.user.UserHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegisterNewUserCommandTest {

    private Scanner scanner;
    private UserHolder userHolder;
    private UserRegistration userRegistration;
    private RegisterNewUserCommand command;

    @BeforeEach
    void setUp() {
        scanner = new Scanner(new ByteArrayInputStream(("INCH\n70\nSTONE\n50\n50\nMALE\nUS\nACTIVE\n"
                + "STONE\n45\nSTONE\n1\nLOSE_WEIGHT\nlack of time\n").getBytes()));
        userHolder = new UserHolder();
        userRegistration = new UserRegistration(scanner);
        command = new RegisterNewUserCommand(scanner, userHolder, userRegistration);
    }

    @Test
    void testExecuteSuccess() throws InvalidCommandException {
        List<String> arguments = Collections.emptyList();
        String result = command.execute(arguments);
        assertEquals("User registered successfully.", result);
    }

    @Test
    void testExecuteInvalidArgumentCount() {
        List<String> arguments = Collections.singletonList("extraArg");

        InvalidCommandException exception = assertThrows(InvalidCommandException.class, () -> {
            command.execute(arguments);
        });

        assertEquals("Command expected to be with 0 arguments", exception.getMessage());
    }

    @Test
    void testName() {
        assertEquals("register-new-user", command.name());
    }

    @Test
    void testGetHelp() {
        assertEquals("Usage: register-new-user", command.getHelp());
    }
}
