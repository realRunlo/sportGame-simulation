package model.football.player;

import model.football.foul.Card;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
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
                    int overallSkill,
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
        super(name, number, team, background, overallSkill, speed, resistance, dexterity, implosion, headGame, kick, passing, card);
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

    @Override
    public Lateral clone() {
        return new Lateral(this);
    }

    @Override
    public Lateral fromCSV(String str) {
        String[] splitStr = str.split(";");
        String[] backgroundStr;
        List<String> background;

        if(!splitStr[3].equals("")) {
            backgroundStr = splitStr[3].split(",");
            background = new ArrayList<>(Arrays.asList(backgroundStr));
        } else background = new ArrayList<>();

        return new Lateral
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
        return "Lateral: " + super.toCSV() + this.getCrossing();
    }
}
