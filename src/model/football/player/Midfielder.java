package model.football.player;

import model.football.foul.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Midfielder extends FootballPlayer {
    private int ballRecovery;

    public Midfielder
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
                    int ballRecovery
            )
    {
        super(name, number, team, speed, resistance, dexterity, implosion, headGame, kick, passing);
        this.changeBallRecovery(ballRecovery);
    }

    public Midfielder
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
                    Card card,
                    int ballRecovery
            )
    {
        super(name, number, team, background, overallSkill, speed, resistance, dexterity, implosion, headGame, kick, passing, card);
        this.changeBallRecovery(ballRecovery);
    }
    public Midfielder(Midfielder midfielder) {
        super(midfielder);
        this.changeBallRecovery(midfielder.getBallRecovery());
    }

    public void changeBallRecovery(int ballRecovery) {
        this.setBallRecovery(ballRecovery);
        updateOverallSkill();
    }

    private void setBallRecovery(int ballRecovery) {
        if(ballRecovery > 100) ballRecovery = 100;
        else if(ballRecovery < 0) ballRecovery = 0;
        this.ballRecovery = ballRecovery;
    }

    public int getBallRecovery() {
        return this.ballRecovery;
    }

    // TODO: Better balance the weights on the skills
    @Override
    public int calcOverallSkill() {
        return (int) ((this.getSpeed() * 0.20) +
                (this.getResistance() * 0.15) +
                (this.getDexterity() * 0.05) +
                (this.getImplosion() * 0.05) +
                (this.getHeadGame() * 0.10) +
                (this.getKick() * 0.10) +
                (this.getPassing() * 0.15) +
                (this.getBallRecovery() * 0.20));
    }

    public boolean equals(Midfielder midfielder) {
        boolean ret = false;

        if (this == midfielder)
            ret = true;
        else if (super.equals(midfielder) && this.getBallRecovery() == midfielder.getBallRecovery())
            ret = true;

        return ret;
    }

    @Override
    public Midfielder clone() {
        return new Midfielder(this);
    }

    public static Midfielder fromCSV(String[] tokens) {
        List<String> background;

        if(!tokens[3].equals("")) {
            background = new ArrayList<>(Arrays.asList(tokens[3].split(",")));
        } else background = new ArrayList<>();

        return new Midfielder
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
                        Card.valueOf(tokens[12]),
                        Integer.parseInt(tokens[13])
                );
    }

    @Override
    public String toCSV() {
        return "Midfielder: " + super.toCSV() + ";" + this.getBallRecovery();
    }
}
