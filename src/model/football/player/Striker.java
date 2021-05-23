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

    // TODO: Better balance the weights on the skills
    @Override
    public int calcOverallSkill() {
        return (int) (
                (this.getSpeed() * 0.15)      +
                (this.getResistance() * 0.5) +
                (this.getDexterity() * 0.10)  +
                (this.getImplosion() * 0.10)  +
                (this.getHeadGame() * 0.5)   +
                (this.getKick() * 0.25)       +
                (this.getPassing() * 0.10)    +
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


    public boolean equals(Striker striker) {
        boolean ret = false;

        if (this == striker)
            ret = true;
        else if (super.equals(striker) && this.getShooting() == striker.getShooting())
            ret = true;

        return ret;
    }

    @Override
    public Striker clone() {
        return new Striker(this);
    }

    public String toCSV() {
        return "Striker: " +
                super.toCSV() + ';' +
                this.getShooting();
    }

    public static Striker load(String csvLine) {
        String[] tokens = csvLine.split(";");
        List<String> bg = Arrays.asList(tokens[3].split(","));

        if(bg.get(0).equals("")) {
            bg = new ArrayList<>();
        }

        return new Striker
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