package model.football.player;

import model.football.foul.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lateral extends FootballPlayer {
    private int crossing;

    public Lateral
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
                    int crossing
            )
    {
        super(name, number, team, speed, resistance, dexterity, implosion, headGame, kick, passing);
        changeCrossing(crossing);
    }

    public Lateral
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
                    int crossing
            )
    {
        super(name, number, team, background, speed, resistance, dexterity, implosion, headGame, kick, passing, card);
        changeCrossing(crossing);
    }

    public Lateral(Lateral lateral) {
        super(lateral);
        changeCrossing(lateral.getCrossing());
    }

    private void setCrossing(int crossing) {
        if(crossing > 100) crossing = 100;
        else if(crossing < 0) crossing = 0;
        this.crossing = crossing;
    }
    public int getCrossing() {
        return this.crossing;
    }
    public void changeCrossing(int crossing) {
       setCrossing(crossing);
       updateOverallSkill();
    }

    @Override
    public int calcOverallSkill() {
        return (int) ((this.getSpeed() * 0.15)      +
                (this.getResistance() * 0.15) +
                (this.getDexterity() * 0.10)  +
                (this.getImplosion() * 0.10)  +
                (this.getHeadGame() * 0.05)   +
                (this.getKick() * 0.10)       +
                (this.getPassing() * 0.20)    +
                (this.getCrossing() * 0.15));
    }

    @Override
    public String toString(){
        return  new String("Team : " + getCurTeam()
                + "\nLateral: "+getName() + "\nShirt: "+ getNumber()
                + "\nSpeed: " + getSpeed()
                + "\nResistance: " + getResistance()
                + "\nDexterity: " + getDexterity()
                + "\nImplosion: " + getImplosion()
                + "\nHeadGame: " + getHeadGame()
                + "\nKick: " + getKick()
                + "\nPassing: " + getPassing()
                + "\nCrossing: " + getCrossing()
                + "\nOverall Skill: " + calcOverallSkill()
                + "\nBackground: " + getBackground().toString()
        );
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;

        if(o == null || !this.getClass().equals(o.getClass()))
            return false;

        Lateral lateral = (Lateral) o;

        return super.equals(lateral) && this.getCrossing() == lateral.getCrossing();
    }

    public String toCSV() {
        return "Lateral:" +
                super.toCSV() + ";" +
                this.getCrossing() + "\n";
    }

    @Override
    public Lateral clone() {
        return new Lateral(this);
    }

    public static Lateral load(String csvLine, String team, boolean teacher) {
        String[] tokens;
        String name;
        int number;
        String teamName;
        List<String> bg;
        int speed;
        int resistance;
        int dexterity;
        int implosion;
        int headGame;
        int kick;
        int passing;
        int crossing;
        Card card;

        if(teacher) {
            tokens = csvLine.split(",");
            bg = new ArrayList<>();
            teamName = team;
            speed = Integer.parseInt(tokens[2]);
            resistance = Integer.parseInt(tokens[3]);
            dexterity = Integer.parseInt(tokens[4]);
            implosion = Integer.parseInt(tokens[5]);
            headGame = Integer.parseInt(tokens[6]);
            kick = Integer.parseInt(tokens[7]);
            passing = Integer.parseInt(tokens[8]);
            card = Card.NONE;
            crossing = Integer.parseInt(tokens[9]);
        }

        else {
            tokens = csvLine.split(";");
            bg = Arrays.asList(tokens[3].split(","));
            if(bg.get(0).equals("")) {
                bg = new ArrayList<>();
            }
            teamName = tokens[2];
            speed = Integer.parseInt(tokens[4]);
            resistance = Integer.parseInt(tokens[5]);
            dexterity = Integer.parseInt(tokens[6]);
            implosion = Integer.parseInt(tokens[7]);
            headGame = Integer.parseInt(tokens[8]);
            kick = Integer.parseInt(tokens[9]);
            passing = Integer.parseInt(tokens[10]);
            card = Card.valueOf(tokens[11]);
            crossing = Integer.parseInt(tokens[12]);
        }

        name = tokens[0];
        number = Integer.parseInt(tokens[1]);

        return new Lateral
                (
                        name,
                        number,
                        teamName,
                        bg,
                        speed,
                        resistance,
                        dexterity,
                        implosion,
                        headGame,
                        kick,
                        passing,
                        card,
                        crossing
                );
    }

}
