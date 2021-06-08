package model.football.player;

import model.football.foul.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Defender extends FootballPlayer {
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
        this.changeBallRetention(defender.getBallRetention());
    }


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

    @Override
    public String toString(){
        return  new String("Team : " + getCurTeam()
                + "\nDefender: "+getName() + "\nShirt: "+ getNumber()
                + "\nSpeed: " + getSpeed()
                + "\nResistance: " + getResistance()
                + "\nDexterity: " + getDexterity()
                + "\nImplosion: " + getImplosion()
                + "\nHeadGame: " + getHeadGame()
                + "\nKick: " + getKick()
                + "\nPassing: " + getPassing()
                + "\nBall Retention: " + getBallRetention()
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

        Defender defender = (Defender) o;

        return super.equals(defender) && this.getBallRetention() == defender.getBallRetention();
    }

    @Override
    public Defender clone() {
        return new Defender(this);
    }

    @Override
    public String toCSV() {
        return "Defesa:" +
                super.toCSV() + ";" +
                this.getBallRetention() + "\n";
    }

    public static Defender load(String csvLine, String team, boolean teacher) {
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
        int ballRetention;
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
            ballRetention = 0;
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
            ballRetention = Integer.parseInt(tokens[12]);
        }

        name = tokens[0];
        number = Integer.parseInt(tokens[1]);

        return new Defender
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
                        ballRetention
                );
    }
}
