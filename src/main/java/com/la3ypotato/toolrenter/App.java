package com.la3ypotato.toolrenter;

import com.la3ypotato.toolrenter.console.Console;
/**
 * This class serves as a starting point for the application. When the
 * main method is invoked, the console when started will begin the application
 * loop.
 *
 * @author Logan Stanfield
 * @version 1.0
 * @since 02/23/2022
 */
public class App {
    // Application entry-point.
    public static void main( String[] args ) {
        Console console = new Console();
        // Start the tool renter app loop.
        console.startConsole();
    }
}
