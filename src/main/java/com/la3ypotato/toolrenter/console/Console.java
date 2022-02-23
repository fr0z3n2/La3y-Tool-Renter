package com.la3ypotato.toolrenter.console;

import com.la3ypotato.toolrenter.rentalagreement.RentalAgreement;

import java.util.*;

public class Console {
    // UI Messages
    private final String WELCOME_MSG = "Welcome to...";
    private final String EXIT_MSG = "Exiting Tool Renter...";
    // Menu IU Messages
    private final String INVALID_INPUT_MSG = "Invalid input detected. Please enter a valid option.";
    private final String MENU_GUIDANCE_MSG = "Key in an option below to select it, then press Enter.";
    private final String MENU_OPTION_1_TEXT = "1) Checkout a Tool";
    private final String MENU_OPTION_2_TEXT = "2) Exit";
    // Main Menu Error UI Messages
    private final String INVALID_TOOL_CODE = "The keyed tool code does not match a tool that is available for rent.";
    private final String INVALID_RENTAL_DAY = "They keyed number of days is invalid. Please enter a valid " +
            "integer greater than 0.";
    private final String INVALID_DISCOUNT = "The keyed discount amount is invalid. Please enter a valid " +
            "integer between 0 and 100";
    private final String INVALID_DATE = "The keyed checkout date is invalid. Please enter a valid date. (Ex: " +
            "08/01/2000)";
    private final String INVALID_RENTAL_AGREEMENT = "Rental Agreement not complete. Returning to main menu.";
    // Checkout UI Messages
    private final String CHECKOUT_MENU_GUIDANCE_MSG = "Please key in the following information and press enter after" +
                                                      " each prompt.";
    private final String CHECKOUT_PROMPT_1 = "Enter the tool code for the rental tool";
    private final String CHECKOUT_PROMPT_2 = "Enter the number of rental days.";
    private final String CHECKOUT_PROMPT_3 = "Enter the discount percent";
    private final String CHECKOUT_PROMPT_4 = "Enter the checkout date (using mm/dd/yyyy format)";
    // Rental Agreement UI Messages
    private final String RENTAL_AGREEMENT_GUIDANCE_MSG = "Please review the printed tool rental agreement below:";
    private final String RENTAL_AGREEMENT_CONTINUE = "Please press enter to begin the next ToolRenter transaction...";
    // UI OPTIONS
    private final String MENU_OPTION_1 = "1";
    private final String MENU_OPTION_2 = "2";
    private final int CHECKOUT_OPTION_1 = 0;
    private final int CHECKOUT_OPTION_2 = 1;
    private final int CHECKOUT_OPTION_3 = 2;
    private final int CHECKOUT_OPTION_4 = 3;
    // Class properties
    private boolean consoleExited = true;
    private String errorMsg = "";

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
        if (!errorMsg.equals("")) {
            System.out.println("!! " + errorMsg + " !!\n");
            errorMsg = "";
        }
        // Displaying menu options and guidance.
        System.out.println(MENU_GUIDANCE_MSG);
        System.out.println(MENU_OPTION_1_TEXT);
        System.out.println(MENU_OPTION_2_TEXT);
    }

    // Shows the beginning of the checkout menu.
    private void showCheckoutMenu() {
        printCheckoutMenuHeader();
        System.out.println(CHECKOUT_MENU_GUIDANCE_MSG + "\n");
    }

    private void showRentalAgreementPage() {
        printCheckoutMenuHeader();
        System.out.println(RENTAL_AGREEMENT_GUIDANCE_MSG + "\n");
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

    // Starts the checkout menu by showing the menu as well as requesting
    // for user input.
    private void startCheckoutMenu() {
        Scanner scanner = new Scanner(System.in);
        clearConsole();
        showCheckoutMenu();
        RentalAgreement rentalAgreement = new RentalAgreement();
        try {
            String[] checkoutPrompts = {CHECKOUT_PROMPT_1, CHECKOUT_PROMPT_2, CHECKOUT_PROMPT_3, CHECKOUT_PROMPT_4};
            // Loop through all of the available checkout prompts.
            for (int i = 0; i < checkoutPrompts.length; i++) {
                String prompt = checkoutPrompts[i];
                System.out.println(prompt);
                String line = scanner.nextLine();
                // Validate the input against the rental agreement acceptance
                // criteria.
                handleCheckoutMenuInput(i, line, rentalAgreement);
                // Check if we received a validation error from the input.
                if (!errorMsg.isEmpty()) {
                    // Do not continue if we received an error in any
                    // of the prompts.
                    return;
                }
            }
        } catch (IllegalStateException | NoSuchElementException e) {
            exitApp();
        }
        startRentalAgreementPage(rentalAgreement);
    }

    // Starts the rental agreement page by showing the page as well as
    // finalizing the rental agreement based on previous input and displaying
    // it back to the user.
    private void startRentalAgreementPage(RentalAgreement rentalAgreement) {
        try {
            rentalAgreement.finalizeRentalAgreement();
        } catch (IllegalStateException e) {
            // Return to the main menu if the rental agreement is not complete.
            errorMsg = INVALID_RENTAL_AGREEMENT;
            return;
        }

        clearConsole();
        showRentalAgreementPage();
        String displayText = rentalAgreement.toString();
        System.out.println(displayText);

        try {
            System.out.println("\n" + RENTAL_AGREEMENT_CONTINUE);
            Scanner scanner = new Scanner(System.in);
            // Accept any character to proceed.
            scanner.nextLine();
            System.out.println();
        } catch (IllegalStateException | NoSuchElementException e) {
            exitApp();
            return;
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

    // Handles the user input from the tool checkout menu.
    private void handleCheckoutMenuInput(int option, String userInput, RentalAgreement currRentalAgreement) {
        switch(option) {
            case CHECKOUT_OPTION_1:
                try {
                    currRentalAgreement.setTargetTool(userInput);
                } catch (IllegalArgumentException e) {
                    errorMsg = INVALID_TOOL_CODE;
                }
                break;
            case CHECKOUT_OPTION_2:
                try {
                    currRentalAgreement.setRentalDays(userInput);
                } catch (IllegalArgumentException e) {
                    errorMsg = INVALID_RENTAL_DAY;
                }
                break;
            case CHECKOUT_OPTION_3:
                // If the user doesn't key anything, assume the value is zero.
                userInput = userInput.isEmpty() ? "0" : userInput;
                try {
                    currRentalAgreement.setDiscount(userInput);
                } catch (IllegalArgumentException e) {
                    errorMsg = INVALID_DISCOUNT;
                }
                break;
            case CHECKOUT_OPTION_4:
                try {
                    currRentalAgreement.setCheckoutDate(userInput);
                } catch (IllegalArgumentException e) {
                    errorMsg = INVALID_DATE;
                }
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
        startMainMenu();
    }
}
