package model.football.player;

import model.football.foul.Card;
import model.interfaces.Saveable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Defender extends FootballPlayer implements Saveable {
    private int ballRetention;

    public Defender
            (
                    String name,
                    int number,
                    String team,
                    int speed,
                    int resistance,
                    int dexterity,
                    int implosion,
                    int headGame,
                    int kick,
                    int passing,
                    int ballRetention
            ) {
        super(name, number, team, speed, resistance, dexterity, implosion, headGame, kick, passing);
        this.changeBallRetention(ballRetention);
    }

    public Defender
            (
                    String name,
                    int number,
                    String curTeam,
                    List<String> background,
                    int speed,
                    int resistance,
                    int dexterity,
                    int implosion,
                    int headGame,
                    int kick,
                    int passing,
                    Card card,
                    int ballRetention
            ) {
        super(name, number, curTeam, background, speed, resistance, dexterity, implosion, headGame, kick, passing, card);
        this.changeBallRetention(ballRetention);
    }

    public Defender(Defender defender) {
        super(defender);
        this.setBallRetention(defender.getBallRetention());
    }

    // TODO: Better balance the weights on the skills
    @Override
    public int calcOverallSkill() {
        return (int) (
                (this.getSpeed() * 0.15) +
                        (this.getResistance() * 0.15) +
                        (this.getDexterity() * 0.10) +
                        (this.getImplosion() * 0.05) +
                        (this.getHeadGame() * 0.15) +
                        (this.getKick() * 0.10) +
                        (this.getPassing() * 0.10) +
                        (this.getBallRetention() * 0.2)
        );
    }

    public void changeBallRetention(int ballRetention) {
        this.setBallRetention(ballRetention);
        this.updateOverallSkill();
    }
    public int getBallRetention(){
        return this.ballRetention;
    }
    public void setBallRetention(int ballRetention){
        if(ballRetention > 100) ballRetention = 100;
        else if(ballRetention < 0) ballRetention = 0;
        this.ballRetention = ballRetention;
    }

    public boolean equals(Defender defender) {
        boolean ret = false;

        if (this == defender)
            ret = true;
        else if (super.equals(defender) && this.getBallRetention() == defender.getBallRetention())
            ret = true;

        return ret;
    }

    @Override
    public Defender clone() {
        return new Defender(this);
    }

    @Override
    public String toCSV() {
        return "Defender: " +
                super.toCSV() + ';' +
                this.getBallRetention();
    }

    public static Defender load(String csvLine) {
        String[] tokens = csvLine.split(";");
        List<String> bg = Arrays.asList(tokens[3].split(","));

        if(bg.get(0).equals("")) {
            bg = new ArrayList<>();
        }

        return new Defender
                (
                        tokens[0],
                        Integer.parseInt(tokens[1]),
                        tokens[2],
                        bg,
                        Integer.parseInt(tokens[4]),
                        Integer.parseInt(tokens[5]),
                        Integer.parseInt(tokens[6]),
                        Integer.parseInt(tokens[7]),
                        Integer.parseInt(tokens[8]),
                        Integer.parseInt(tokens[9]),
                        Integer.parseInt(tokens[10]),
                        Card.valueOf(tokens[11]),
                        Integer.parseInt(tokens[12])
                );
    }
}
