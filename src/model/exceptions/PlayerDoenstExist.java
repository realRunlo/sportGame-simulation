package model.exceptions;

public class PlayerDoenstExist extends Exception{
    public PlayerDoenstExist() {
        super();
    }

    public PlayerDoenstExist(String msg) {
        super(msg);
    }
}
