package com.fmi.myfitnesspal.cli;

import java.util.Scanner;
import com.fmi.myfitnesspal.command.CommandExecutor;
import com.fmi.myfitnesspal.command.CommandParser;
import com.fmi.myfitnesspal.command.ExecutableCommandRegistry;

public final class Menu {

    private final ExecutableCommandRegistry registry;

    private final Scanner scanner;

    public Menu(ExecutableCommandRegistry registry, Scanner scanner) {
        this.registry = registry;
        this.scanner = scanner;
    }

    private static final String EXIT_COMMAND = "exit";

    public void start() {

        CommandParser parser = new CommandParser();
        CommandExecutor executor = new CommandExecutor(registry);

        while (true) {
            System.out.print(">> ");
            String input = this.scanner.nextLine();
            if (input.equalsIgnoreCase(EXIT_COMMAND)) {
                break;
            }

            try {
                String message = executor.execute(parser.parseCommand(input));
                System.out.println(message);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
