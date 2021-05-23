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

    public boolean equals(Lateral lateral) {

        boolean ret = false;

        if(this == lateral)
            ret = true;
        else if(super.equals(lateral) && this.getCrossing() == lateral.getCrossing())
            ret = true;

        return ret;
    }

    public String toCSV() {
        return "Lateral: " +
                super.toCSV() + ';' +
                this.getCrossing();
    }

    @Override
    public Lateral clone() {
        return new Lateral(this);
    }

    public static Lateral load(String csvLine) {
        String[] tokens = csvLine.split(";");
        List<String> bg = Arrays.asList(tokens[3].split(","));

        if(bg.get(0).equals("")) {
            bg = new ArrayList<>();
        }

        return new Lateral
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
