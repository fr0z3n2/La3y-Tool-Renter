package com.la3ypotato.toolrenter.console;

import com.la3ypotato.toolrenter.rentalagreement.RentalAgreement;

import java.util.*;

/**
 * This class handles the app loop and displays the menus accordingly. While the app loop is started and ongoing, the
 * menu will always be the start of the application. The current design approach is that if any subsequent menu results
 * in an errorMsg being set the main menu will be displayed forcing the operator to restart the rental transaction.
 *
 * @author Logan Stanfield
 * @version 1.0
 * @since 02/23/2022
 */
public class Console {
    // UI Messages
    private final String WELCOME_MSG = "Welcome to...";
    private final String EXIT_MSG = "Exiting Tool Renter...";
    // Menu IU Messages
    private final String INVALID_INPUT_MSG = "Invalid input detected. Please enter a valid option (1 or 2).";
    private final String MENU_GUIDANCE_MSG = "Key in an option (by entering 1 or 2) below to select it, then press Enter.";
    private final String MENU_OPTION_1_TEXT = "1) Check out a Tool";
    private final String MENU_OPTION_2_TEXT = "2) Exit";
    // Main Menu Error UI Messages
    private final String INVALID_TOOL_CODE = "The keyed tool code does not match a tool that is available for rent.";
    private final String INVALID_RENTAL_DAY = "They keyed number of days is invalid. Please enter a valid " +
            "number of days greater than 0.";
    private final String INVALID_DISCOUNT = "The keyed discount amount is invalid. Please enter a valid " +
            "number between 0 and 100 (exclude % symbol)";
    private final String INVALID_DATE = "The keyed checkout date is invalid. Please enter a valid date. (Ex: " +
            "08/01/2000)";
    private final String INVALID_RENTAL_AGREEMENT = "Rental Agreement not complete. Returning to main menu.";
    // Checkout UI Messages
    private final String CHECKOUT_MENU_GUIDANCE_MSG = "Please key in the following information and press enter after" +
                                                      " each prompt.";
    private final String CHECKOUT_PROMPT_1 = "Key in the tool code for desired tool to checkout (ex: 'CHNS', 'LADW', " +
                                             "JAKD, JAKR, etc):";
    private final String CHECKOUT_PROMPT_2 = "Enter the number of rental days (ex: 5):";
    private final String CHECKOUT_PROMPT_3 = "Enter the discount percent (ex: 10):";
    private final String CHECKOUT_PROMPT_4 = "Enter the checkout date (ex: 08/01/2000):";
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

    // Empty default constructor.
    public Console() {

    }

    /**
     * This method displays the tool renter logo that's printed to the console.
     *
     * @see <a href="https://patorjk.com/software/taag/#p=display&f=Small%20Slant&t=ToolRenter">patorjk.com</a>
     */
    private void printMenuHeader() {
        System.out.println(WELCOME_MSG);
        System.out.println(" ______          _____           __");
        System.out.println("/_  __/__  ___  / / _ \\___ ___  / /____ ____");
        System.out.println(" / / / _ \\/ _ \\/ / , _/ -_) _ \\/ __/ -_) __/");
        System.out.println("/_/  \\___/\\___/_/_/|_|\\__/_//_/\\__/\\__/_/\n");
    }

    /**
     * This method displays the tool renter checkout header that's printed to the console.
     *
     * @see <a href="https://patorjk.com/software/taag/#p=display&f=Small%20Slant&t=ToolRenter">patorjk.com</a>
     */
    private void printCheckoutMenuHeader() {
        System.out.println(" ______          _____           __");
        System.out.println("/_  __/__  ___  / / _ \\___ ___  / /____ ____");
        System.out.println(" / / / _ \\/ _ \\/ / , _/ -_) _ \\/ __/ -_) __/");
        System.out.println("/_/__\\___/\\___/_/_/|_|\\__/_//_/\\__/\\__/_/");
        System.out.println(" / ___/ /  ___ ____/ /_____  __ __/ /_");
        System.out.println("/ /__/ _ \\/ -_) __/  '_/ _ \\/ // / __/");
        System.out.println("\\___/_//_/\\__/\\__/_/\\_\\\\___/\\_,_/\\__/\n");
    }

    /**
     * This method clears the console using commands depending on the host OS.
     */
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

    /**
     * This method will print the menu header, guidance message, and available options to select to the console. If an
     * errorMsg is set, then it will be displayed on the screen to notify the user of the error that is needed for
     * operations.
     */
    private void showMainMenu() {
        printMenuHeader();
        if (!errorMsg.equals("")) {
            System.out.println("!! " + errorMsg + " !!\n");
            errorMsg = "";
        }
        // Displaying menu options and guidance.
        System.out.println(MENU_GUIDANCE_MSG + "\n");
        System.out.println(MENU_OPTION_1_TEXT);
        System.out.println(MENU_OPTION_2_TEXT);
    }

    /**
     * This method prints the checkout menu header to the console as well as providing a guidance message
     * to the user.
     */
    private void showCheckoutMenu() {
        printCheckoutMenuHeader();
        System.out.println(CHECKOUT_MENU_GUIDANCE_MSG + "\n");
    }

    /**
     * This method prints the rental agreement header (which re-uses the checkout header) to the console as well
     * as the guidance message to the user.
     */
    private void showRentalAgreementPage() {
        printCheckoutMenuHeader();
        System.out.println(RENTAL_AGREEMENT_GUIDANCE_MSG + "\n");
    }

    /**
     * This method when invoked will display the menu header and begin to request input from the user. This method
     * serves as the main loop for the application that will continue until the application is exited. Each menu or
     * page in this application should return here when the operations are completed. The loop is halted when waiting
     * for input from the user.
     */
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
            exitConsole();
        }
    }

    /**
     * This method starts the checkout menu by showing the checkout menu header and then requesting input from the user.
     * If any of the provided input is invalid, based on the handling the user will be sent back to the main menu.
     */
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
                System.out.println();
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
            exitConsole();
        }
        // Attempt to finalize the rentail agreement based on the received input.
        startRentalAgreementPage(rentalAgreement);
    }

    /**
     * This method initially attempts to finalize the rental agreement based on the previous entered user input. If the
     * rental agreement has not been completed, an IllegalStateException will be thrown thus showing an error on the
     * main menu. If the rental agreement is complete, it will be printed to the console such that it can be observed
     * by the clerk as well as the renter.
     *
     * In order to continue to the next rental transaction the operator must press enter.
     *
     * @param rentalAgreement - the current and completed rental agreement.
     */
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
            exitConsole();
            return;
        }
    }

    /**
     * This method accepts a user input string that will be compared with the available menu options. If invalid user
     * input is received, then an error message will be set.
     *
     * @param userInput - The desired menu option (1 or 2).
     */
    private void handleMenuInput(String userInput) {
        switch (userInput) {
            case MENU_OPTION_1:
                // Received input to check out a tool.
                startCheckoutMenu();
                break;
            case MENU_OPTION_2:
                // Received input to exit.
                exitConsole();
                break;
            default:
                // If neither of the cases are satisfied, display an error message.
                errorMsg = INVALID_INPUT_MSG;
        }
    }

    /**
     * This method handles the user input and checks if the input is valid based on the current rental agreement. The
     * RentalAgreement object has setters that will validate the user input string and throw an IllegalArgumentException
     * if the input is invalid. Currently there are only four checkout menu options available.
     *
     * @param option - selected checkout menu option.
     * @param userInput - user provided input string.
     * @param currRentalAgreement - the current rental agreement being drafted.
     */
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
                // If the user doesn't key anything, assume the value is zero. No need to force the user to enter 0
                // each time.
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

    /**
     * This method serves as a tool to exit the app in a friendly way. It will also exit the app loop.
     */
    public void exitConsole() {
        consoleExited = true;
        System.out.println("\n" + EXIT_MSG);
    }

    /**
     * Checks if the main app loop has exited which is determined by the consoleExited flag.
     *
     * @return - flag to note if the app loop has been exited.
     */
    public boolean isConsoleExited() {
        return consoleExited;
    }

    /**
     * This method starts the app loop when the menu is displayed. Note, when consoleExited is set to true, the loop
     * will exit.
     */
    public void startConsole() {
        consoleExited = false;
        clearConsole();
        startMainMenu();
    }
}
