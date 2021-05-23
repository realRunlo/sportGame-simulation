package model.interfaces;

import model.exceptions.InvalidCSVLine;

import java.io.BufferedReader;

public interface Saveable {
    public String toCSV();

    public static Saveable load(BufferedReader br) throws InvalidCSVLine {
        return null;
    }

    public void save(String filePath);
}
