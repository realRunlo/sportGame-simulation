package model.football.player;

import model.football.foul.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Striker extends FootballPlayer {
    // TODO: Add instance variable for this class

    public Striker
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
            )
    {
        super(name, number, team, speed, resistance, dexterity, implosion, headGame, kick, passing);
        // TODO: Put changeVariableName() here
    }

    public Striker
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
            )
    {
        super(name, number, team, background, overallSkill, speed, resistance, dexterity, implosion, headGame, kick, passing, card);
        // TODO: Put changeVariableName() here
    }

    public Striker(Striker striker) {
        super(striker);
        // TODO: Also here
    }

    // TODO: Better balance the weights on the skills
    @Override
    public int calcOverallSkill() {
        return (int) (
                (this.getSpeed() * 0.15)      +
                (this.getResistance() * 0.15) +
                (this.getDexterity() * 0.10)  +
                (this.getImplosion() * 0.10)  +
                (this.getHeadGame() * 0.15)   +
                (this.getKick() * 0.25)       +
                (this.getPassing() * 0.10)
        );
    }

    public boolean equals(Striker striker) {
        boolean ret = false;

        if (this == striker)
            ret = true;
        else if (super.equals(striker))
            ret = true;

        return ret;
    }

    @Override
    public Striker clone() {
        return new Striker(this);
    }

    public static Striker fromCSV(String[] tokens) {
        List<String> background;

        if(!tokens[3].equals("")) {
            background = new ArrayList<>(Arrays.asList(tokens[3].split(",")));
        } else background = new ArrayList<>();

        return new Striker
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

    @Override
    public String toCSV() {
        return "Striker: " + super.toCSV();
    }
}