package model.interfaces;

import model.exceptions.InvalidCSVLine;

import java.io.BufferedReader;
import java.io.IOException;

public interface Saveable {
    public String toCSV();

    public static Saveable load(String csvLine) throws InvalidCSVLine {
        return null;
    }

    public void save(String filePath) throws IOException;
}
