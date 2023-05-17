package main.java;

public class UnknownSideException extends Exception {
    public UnknownSideException() {
        super("ERROR: Unknown value for the enum 'Side'");
    }
}
