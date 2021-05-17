package controller.errors;

public class NoFootballStateFoundException extends Exception {
    public NoFootballStateFoundException() {
        super();
    }

    public NoFootballStateFoundException(String s) {
        super(s);
    }
}
