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

    @Override
    public Midfielder fromCSV(String str) {
        String[] splitStr = str.split(";");
        String[] backgroundStr;
        List<String> background;

        if(!splitStr[3].equals("")) {
            backgroundStr = splitStr[3].split(",");
            background = new ArrayList<>(Arrays.asList(backgroundStr));
        } else background = new ArrayList<>();

        return new Midfielder
                (
                        splitStr[0],
                        Integer.parseInt(splitStr[1]),
                        splitStr[2],
                        background,
                        Integer.parseInt(splitStr[4]),
                        Integer.parseInt(splitStr[5]),
                        Integer.parseInt(splitStr[6]),
                        Integer.parseInt(splitStr[7]),
                        Integer.parseInt(splitStr[8]),
                        Integer.parseInt(splitStr[9]),
                        Integer.parseInt(splitStr[10]),
                        Integer.parseInt(splitStr[11]),
                        Card.valueOf(splitStr[12]),
                        Integer.parseInt(splitStr[13])
                );
    }

    @Override
    public String toCSV() {
        return "Midfielder: " + super.toCSV() + ";" + this.getBallRecovery();
    }
}
