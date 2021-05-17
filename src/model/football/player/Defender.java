package model.football.player;

import model.football.foul.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Defender extends FootballPlayer {
    // Don't know what skill to add here

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
                    int passing
            ) {
        super(name, number, team, speed, resistance, dexterity, implosion, headGame, kick, passing);
    }

    public Defender
            (
                    String name,
                    int number,
                    String team,
                    List<String> background,
                    int overallSkill,
                    int speed,
                    int resistance,
                    int dexterity,
                    int implosion,
                    int headGame,
                    int kick,
                    int passing,
                    Card card
            ) {
        super(name, number, team, background, overallSkill, speed, resistance, dexterity, implosion, headGame, kick, passing, card);
    }

    public Defender(Defender defender) {
        super(defender);
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
                        (this.getPassing() * 0.30)
        );
    }

    public boolean equals(Defender defender) {
        boolean ret = false;

        if (this == defender)
            ret = true;
        else if (super.equals(defender))
            ret = true;

        return ret;
    }

    @Override
    public Defender clone() {
        return new Defender(this);
    }

    @Override
    public String toCSV() {
        return "Defender: " + super.toCSV();
    }

    public static Defender fromCSV(String[] tokens) {
        List<String> background;

        if(!tokens[3].equals("")) {
            background = new ArrayList<>(Arrays.asList(tokens[3].split(",")));
        } else background = new ArrayList<>();

        return new Defender
                (
                        tokens[0],
                        Integer.parseInt(tokens[1]),
                        tokens[2],
                        background,
                        Integer.parseInt(tokens[4]),
                        Integer.parseInt(tokens[5]),
                        Integer.parseInt(tokens[6]),
                        Integer.parseInt(tokens[7]),
                        Integer.parseInt(tokens[8]),
                        Integer.parseInt(tokens[9]),
                        Integer.parseInt(tokens[10]),
                        Integer.parseInt(tokens[11]),
                        Card.valueOf(tokens[12])
                );
    }
}
