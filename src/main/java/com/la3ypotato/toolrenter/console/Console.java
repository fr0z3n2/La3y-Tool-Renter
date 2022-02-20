package com.la3ypotato.toolrenter.console;

import com.la3ypotato.toolrenter.tool.Tool;
import com.la3ypotato.toolrenter.tool.Tools;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Console {
    // UI Messages
    private final String WELCOME_MSG = "Welcome to...";
    private final String EXIT_MSG = "Exiting Tool Renter...";
    // Menu IU Messages
    private final String INVALID_INPUT_MSG = "Invalid input detected. Please enter a valid option.";
    private final String MENU_GUIDANCE_MSG = "Key in an option below to select it, then press Enter.";
    private final String MENU_OPTION_1_TEXT = "1) Checkout a Tool";
    private final String MENU_OPTION_2_TEXT = "2) Exit";
    // Checkout UI Messages
    private final String CHECKOUT_MENU_GUIDANCE_MSG = "Please key in the following information and press enter after" +
                                                      " each prompt.";
    private final String CHECKOUT_PROMPT_1 = "Enter the tool code for the rental tool";
    private final String CHECKOUT_PROMPT_2 = "Enter the number of rental days.";
    private final String CHECKOUT_PROMPT_3 = "Enter the discount percent";
    private final String CHECKOUT_PROMPT_4 = "Enter the checkout date (using mm/dd/yyyy format)";
    // UI OPTIONS
    private final String MENU_OPTION_1 = "1";
    private final String MENU_OPTION_2 = "2";
    private final int CHECKOUT_OPTION_1 = 1;
    private final int CHECKOUT_OPTION_2 = 2;
    private final int CHECKOUT_OPTION_3 = 3;
    private final int CHECKOUT_OPTION_4 = 4;
    // Class properties
    private boolean consoleExited = true;
    private String errorMsg = "";
    private Map<String, Tool> availableTools = new HashMap<>();

    // Class constructor that
    public Console() {
        consoleExited = false;
    }

    // Shows the tool renter logo
    private void printMenuHeader() {
        System.out.println(WELCOME_MSG);
        System.out.println(" ______          _____           __");
        System.out.println("/_  __/__  ___  / / _ \\___ ___  / /____ ____");
        System.out.println(" / / / _ \\/ _ \\/ / , _/ -_) _ \\/ __/ -_) __/");
        System.out.println("/_/  \\___/\\___/_/_/|_|\\__/_//_/\\__/\\__/_/\n");
    }

    private void printCheckoutMenuHeader() {
        System.out.println(" ______          _____           __");
        System.out.println("/_  __/__  ___  / / _ \\___ ___  / /____ ____");
        System.out.println(" / / / _ \\/ _ \\/ / , _/ -_) _ \\/ __/ -_) __/");
        System.out.println("/_/__\\___/\\___/_/_/|_|\\__/_//_/\\__/\\__/_/");
        System.out.println(" / ___/ /  ___ ____/ /_____  __ __/ /_");
        System.out.println("/ /__/ _ \\/ -_) __/  '_/ _ \\/ // / __/");
        System.out.println("\\___/_//_/\\__/\\__/_/\\_\\\\___/\\_,_/\\__/\n");
    }

    private void initializeTools() {
        Tools tools = new Tools();
        availableTools = tools.getAvailableTools();
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
    private void showMainMenu() {
        printMenuHeader();
        if (errorMsg != "") {
            System.out.println("!! " + errorMsg + " !!\n");
            errorMsg = "";
        }
        // Displaying menu options and guidance.
        System.out.println(MENU_GUIDANCE_MSG);
        System.out.println(MENU_OPTION_1_TEXT);
        System.out.println(MENU_OPTION_2_TEXT);
    }

    private void showCheckoutMenu() {
        printCheckoutMenuHeader();
        System.out.println(CHECKOUT_MENU_GUIDANCE_MSG + "\n");
    }

    // When called, requests user input.
    private void startMainMenu() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (!isConsoleExited()) {
                clearConsole();
                showMainMenu();
                String line = scanner.nextLine();
                handleMenuInput(line);
            }
        } catch (IllegalStateException | NoSuchElementException e) {
            exitApp();
        }
    }

    private void startCheckoutMenu() {
        Scanner scanner = new Scanner(System.in);
        clearConsole();
        showCheckoutMenu();
        try {
            // Displaying prompts and requesting for input.
            System.out.println(CHECKOUT_PROMPT_1);
            String line = scanner.nextLine();
            handleCheckoutMenuInput(CHECKOUT_OPTION_1, line);
            System.out.println("\n" + CHECKOUT_PROMPT_2);
            line = scanner.nextLine();
            handleCheckoutMenuInput(CHECKOUT_OPTION_2, line);
            System.out.println("\n" + CHECKOUT_PROMPT_3);
            line = scanner.nextLine();
            handleCheckoutMenuInput(CHECKOUT_OPTION_3, line);
            System.out.println("\n" + CHECKOUT_PROMPT_4);
            line = scanner.nextLine();
            handleCheckoutMenuInput(CHECKOUT_OPTION_4, line);
            // TODO: Generate a report if the data is correct.
        } catch (IllegalStateException | NoSuchElementException e) {
            exitApp();
        }
    }

    // Validates the user input and handles it based on the passed option.
    private void handleMenuInput(String userInput) {
        switch (userInput) {
            case MENU_OPTION_1:
                // Received input to check out a tool.
                startCheckoutMenu();
                break;
            case MENU_OPTION_2:
                // Received input to exit.
                exitApp();
                break;
            default:
                errorMsg = INVALID_INPUT_MSG;
        }
    }

    private void handleCheckoutMenuInput(int option, String userInput) {
        switch(option) {
            case CHECKOUT_OPTION_1:
                // TODO: Validate input and check if the tools are present in the available tools.
                break;
            case CHECKOUT_OPTION_2:
                // TODO: Validate input is a non-zero number
                break;
            case CHECKOUT_OPTION_3:
                // TODO: Validate input is between 0 and 100
                break;
            case CHECKOUT_OPTION_4:
                // TODO: Validate input is a valid date.
                break;
            default:
                break;
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
        initializeTools();
        startMainMenu();
    }
}
