package com.la3ypotato.toolrenter.console;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Console {
    // UI Messages
    private String WELCOME_MSG = "Welcome to...";
    private String EXIT_MSG = "Exiting Tool Renter...";
    private String INVALID_INPUT_MSG = "Invalid input detected. Please enter a valid option.";
    private String MENU_GUIDANCE_MSG = "Key in an option below to select it, then press Enter.";
    private String OPTION_1_TEXT = "1) Checkout a Tool";
    private String OPTION_2_TEXT = "2) Exit";
    // UI OPTIONS
    private final String OPTION_1 = "1";
    private final String OPTION_2 = "2";
    // Class properties
    private boolean consoleExited = true;
    private String errorMsg = "";

    // Class constructor that
    public Console() {
        consoleExited = false;
    }

    // Shows the tool renter logo
    private void printHeader() {
        System.out.println(" ______          _____           __        ");
        System.out.println("/_  __/__  ___  / / _ \\___ ___  / /____ ____");
        System.out.println(" / / / _ \\/ _ \\/ / , _/ -_) _ \\/ __/ -_) __/");
        System.out.println("/_/  \\___/\\___/_/_/|_|\\__/_//_/\\__/\\__/_/   ");
        System.out.println();
    }

    // Clears the command line
    private void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Shows the main menu of tool renter.
    private void showMenu() {
        printHeader();
        if (errorMsg != "") {
            System.out.println("!! " + errorMsg + " !!\n");
            errorMsg = "";
        }
        // Displaying menu options and guidance.
        System.out.println(MENU_GUIDANCE_MSG);
        System.out.println(OPTION_1_TEXT);
        System.out.println(OPTION_2_TEXT);
    }

    // When called, requests user input.
    private void requestUserInput() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (!isConsoleExited()) {
                clearConsole();
                showMenu();
                String line = scanner.nextLine();
                handleInput(line);
            }
        } catch (IllegalStateException | NoSuchElementException e) {
            exitApp();
        }
    }

    // Validates the user input and handles it based on the passed option.
    private void handleInput(String userInput) {
        switch (userInput) {
            case OPTION_1:
                break;
            case OPTION_2:
                // Received input to exit.
                exitApp();
                break;
            default:
                errorMsg = INVALID_INPUT_MSG;
        }
    }

    // Handles exiting the app loop.
    private void exitApp() {
        consoleExited = true;
        System.out.println("\n" + EXIT_MSG);
    }

    // This will exit the app loop if set to true.
    public boolean isConsoleExited() {
        return consoleExited;
    }

    // Starts the app loop.
    public void startConsole() {
        clearConsole();
        System.out.println(WELCOME_MSG);
        requestUserInput();
    }
}
