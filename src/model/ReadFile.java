package model;

import model.football.Game.FootballGame;
import model.football.player.*;
import model.football.state.FootballState;
import model.football.team.FootballTeam;
import controller.errors.NoFootballStateFoundException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class ReadFile {
    public static FootballState loadFile(String filePath) throws NoFootballStateFoundException {
        FootballState fs;
        FootballTeam ft;
        FootballPlayer fp;
        List<String> lines = readFile(filePath);
        String[] lineSplit;
        String[] tokens;

        lineSplit = lines.get(0).split(": ");
        tokens = lineSplit[1].split(";");
        if(!lineSplit[0].equals("FootballState")) {
            throw new NoFootballStateFoundException();
        }

        fs = FootballState.fromCSV(tokens);

        for(int i = 1; i < lines.size(); i++) {
            lineSplit = lines.get(i).split(": ");
            tokens = lineSplit[1].split(";");

            switch (lineSplit[0]) {
                case "FootballTeam" -> {
                    ft = FootballTeam.fromCSV(tokens);
                    fs.addTeam(ft.clone());
                }
                case "Goalkeeper" -> {
                    fp = Goalkeeper.fromCSV(tokens);
                    fs.addPlayer(fp.clone());
                    fs.addPlayer2Team(fp.clone(), fp.getCurTeam());
                }
                case "Defender" -> {
                    fp = Defender.fromCSV(tokens);
                    fs.addPlayer(fp.clone());
                    fs.addPlayer2Team(fp.clone(), fp.getCurTeam());
                }
                case "Midfielder" -> {
                    fp = Midfielder.fromCSV(tokens);
                    fs.addPlayer(fp.clone());
                    fs.addPlayer2Team(fp.clone(), fp.getCurTeam());
                }
                case "Lateral" -> {
                    fp = Lateral.fromCSV(tokens);
                    fs.addPlayer(fp.clone());
                    fs.addPlayer2Team(fp.clone(), fp.getCurTeam());
                }
                case "Striker" -> {
                    fp = Striker.fromCSV(tokens);
                    fs.addPlayer(fp.clone());
                    fs.addPlayer2Team(fp.clone(), fp.getCurTeam());
                }
            }
        }
        return fs;
    }

    public static void saveState(FootballState fs, String filePath) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(filePath);

        pw.write(fs.toCSV());
        pw.flush();
        pw.close();
    }

    private static List<String> readFile(String filePath) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            lines = new ArrayList<>();
        }

        return lines;
    }
}
