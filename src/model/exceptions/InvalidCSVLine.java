package model.exceptions;

public class InvalidCSVLine extends Exception {
    public InvalidCSVLine() {
        super();
    }

    public InvalidCSVLine(String msg) {
        super(msg);
    }
}
