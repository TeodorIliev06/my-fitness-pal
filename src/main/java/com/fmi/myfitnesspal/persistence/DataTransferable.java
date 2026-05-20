package com.fmi.myfitnesspal.persistence;

import com.fmi.myfitnesspal.exception.InvalidCommandException;

public interface DataTransferable {

    void importFromUserFiles() throws InvalidCommandException;

    void exportToUserFiles() throws InvalidCommandException;
}
