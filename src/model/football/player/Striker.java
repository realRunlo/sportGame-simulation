package model.football.player;

import model.football.foul.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Striker extends FootballPlayer {
    private int shooting;

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
                    int passing,
                    int shooting
            )
    {
        super(name, number, team, speed, resistance, dexterity, implosion, headGame, kick, passing);
        this.changeShooting(shooting);
    }

    public Striker
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
                    int shooting
            )
    {
        super(name, number, team, background, speed, resistance, dexterity, implosion, headGame, kick, passing, card);
        this.changeShooting(shooting);
    }

    public Striker(Striker striker) {
        super(striker);
        this.changeShooting(striker.getShooting());
    }


    @Override
    public int calcOverallSkill() {
        return (int) (
                (this.getSpeed() * 0.15)      +
                (this.getResistance() * 0.15) +
                (this.getDexterity() * 0.05)  +
                (this.getImplosion() * 0.1)  +
                (this.getHeadGame() * 0.15)   +
                (this.getKick() * 0.15)       +
                (this.getPassing() * 0.05)    +
                (this.getShooting() * 0.2)
        );
    }

    public void changeShooting(int shooting) {
        this.setShooting(shooting);
        this.updateOverallSkill();
    }
    public int getShooting(){
        return shooting;
    }
    public void setShooting(int shooting){
        if(shooting > 100) shooting = 100;
        else if(shooting < 0) shooting = 0;
        this.shooting = shooting;
    }

    @Override
    public String toString(){
        return  new String("Team : " + getCurTeam()
                +"\nStriker: "+getName() + "\nShirt: "+ getNumber()
                + "\nSpeed: " + getSpeed()
                + "\nResistance: " + getResistance()
                + "\nDexterity: " + getDexterity()
                + "\nImplosion: " + getImplosion()
                + "\nHeadGame: " + getHeadGame()
                + "\nKick: " + getKick()
                + "\nPassing: " + getPassing()
                + "\nShooting: " + getShooting()
                + "\nOverall Skill: " + calcOverallSkill()
                + "\nBackground: " + getBackground().toString()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if(o == null || !this.getClass().equals(o.getClass()))
            return false;

        Striker striker = (Striker) o;

        return super.equals(striker) && this.getShooting() == striker.getShooting();
    }

    @Override
    public Striker clone() {
        return new Striker(this);
    }

    public String toCSV() {
        return "Avancado:" +
                super.toCSV() + ";" +
                this.getShooting() + "\n";
    }

    public static Striker load(String csvLine, String team, boolean teacher) {
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
        int shooting;
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
            shooting = averageValue( speed,  resistance, dexterity,  implosion,  headGame, kick, passing);
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
            shooting = Integer.parseInt(tokens[12]);
        }

        name = tokens[0];
        number = Integer.parseInt(tokens[1]);
        return new Striker
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
                        shooting
                );
    }
}