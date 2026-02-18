package com.fmi.myfitnesspal.command;

import java.util.List;

public record Command(String command, List<String> arguments) {
}
