package model.football.player;

import model.football.foul.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Goalkeeper extends FootballPlayer {
    private int elasticity;

    public Goalkeeper
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
                    int elasticity
            )
    {
        super(name, number, team, speed, resistance, dexterity, implosion, headGame, kick, passing);
        this.changeElasticity(elasticity);
    }

    public Goalkeeper
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
                    int elasticity
            )
    {
        super(name, number, team, background, overallSkill, speed, resistance, dexterity, implosion, headGame, kick, passing, card);
        this.changeElasticity(elasticity);
    }

    public Goalkeeper(Goalkeeper goalkeeper) {
        super(goalkeeper);
        this.changeElasticity(goalkeeper.getElasticity());
    }

    public void changeElasticity(int elasticity) {
        setElasticity(elasticity);
        updateOverallSkill();
    }
    private void setElasticity(int elasticity) {
        if(elasticity > 100) elasticity = 100;
        else if(elasticity < 0) elasticity = 0;
        this.elasticity = elasticity;
    }
    public int getElasticity() {
        return this.elasticity;
    }

    // TODO: Better balance the weights on the skills
    @Override
    public int calcOverallSkill() {
        return (int) ((this.getSpeed() * 0.05)      +
                      (this.getResistance() * 0.15) +
                      (this.getDexterity() * 0.10)  +
                      (this.getImplosion() * 0.05)  +
                      (this.getHeadGame() * 0.05)   +
                      (this.getKick() * 0.05)       +
                      (this.getPassing() * 0.20)    +
                      (this.getElasticity() * 0.30));
    }

    public boolean equals(Goalkeeper goalkeeper) {
        boolean ret = false;

        if(this == goalkeeper)
            ret = true;
        else if(super.equals(goalkeeper) && this.getElasticity() == goalkeeper.getElasticity())
            ret = true;

        return ret;
    }

    @Override
    public Goalkeeper clone() {
        return new Goalkeeper(this);
    }

    public static Goalkeeper fromCSV(String str) {
        String[] splitStr = str.split(";");
        List<String> background;

        if(!splitStr[3].equals("")) {
            background = new ArrayList<>(Arrays.asList(splitStr[3].split(",")));
        } else background = new ArrayList<>();

        return new Goalkeeper
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
        return "Goalkeeper: " + super.toCSV() + ";" + this.getElasticity();
    }
}
