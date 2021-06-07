package model.football.player;

import model.football.foul.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Goalkeeper extends FootballPlayer{
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
        super(name, number, team, background, speed, resistance, dexterity, implosion, headGame, kick, passing, card);
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

    public String toString(){
        return  new String("Team : " + getCurTeam()
                + "\nGoalkeeper: "+getName() + "\nShirt: "+ getNumber()
                + "\nSpeed: " + getSpeed()
                + "\nResistance: " + getResistance()
                + "\nDexterity: " + getDexterity()
                + "\nImplosion: " + getImplosion()
                + "\nHeadGame: " + getHeadGame()
                + "\nKick: " + getKick()
                + "\nPassing: " + getPassing()
                + "\nElasticity: " + getElasticity()
                + "\nOverall Skill: " + calcOverallSkill()
                + "\nBackground: " + getBackground().toString()
        );
    }

    public boolean equals(Object o) {
        if(this == o)
            return true;

        if(o == null || !this.getClass().equals(o.getClass()))
            return false;

        Goalkeeper goalkeeper = (Goalkeeper) o;

        return super.equals(goalkeeper) && this.getElasticity() == goalkeeper.getElasticity();
    }

    @Override
    public Goalkeeper clone() {
        return new Goalkeeper(this);
    }

    public String toCSV() {
        return "Goalkeeper: " +
                super.toCSV() + ";" +
                this.getElasticity() + "\n";
    }

    public static Goalkeeper load(String csvLine) {
        String[] tokens = csvLine.split(";");
        List<String> bg = Arrays.asList(tokens[3].split(","));

        if(bg.get(0).equals("")) {
            bg = new ArrayList<>();
        }

        return new Goalkeeper
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
