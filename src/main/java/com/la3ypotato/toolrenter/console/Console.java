package com.la3ypotato.toolrenter.console;

public class Console {

    // Default Console class constructor.
    public Console() {
    }

    private void printHeader() {
        System.out.println("Welcome to...");
        System.out.println(" ______          _____           __        ");
        System.out.println("/_  __/__  ___  / / _ \\___ ___  / /____ ____");
        System.out.println(" / / / _ \\/ _ \\/ / , _/ -_) _ \\/ __/ -_) __/");
        System.out.println("/_/  \\___/\\___/_/_/|_|\\__/_//_/\\__/\\__/_/   ");
        System.out.println();
    }

    public void startConsole() {
        printHeader();
    }
}
