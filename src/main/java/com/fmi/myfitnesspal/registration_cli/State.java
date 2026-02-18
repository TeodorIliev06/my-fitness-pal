package com.fmi.myfitnesspal.registration_cli;

import com.fmi.myfitnesspal.user.UserBuilder;

import java.util.Scanner;

/**
 * The {@code State} interface represents a state in a state machine pattern.
 * Implementations of this interface define specific behaviors and transitions
 * between different states within the state machine.
 */

public interface State {
     /**
      * Executes the actions associated with this state.
      * This method is responsible for performing the operations defined by this state
      * and can use the provided {@link UserBuilder} and {@link Scanner} for its operations.
      *
      * @param builder the {@code UserBuilder} instance that holds the user data and state.
      * @param scanner the {@code Scanner} instance used for reading input from the user.
      */
     void performState(UserBuilder builder, Scanner scanner);
     /**
      * Determines and returns the next state in the state machine.
      * This method is responsible for transitioning to the next state based on the
      * current state's logic.
      *
      * @return the next {@code State} to transition to.
      */
     State getNextState();
}
