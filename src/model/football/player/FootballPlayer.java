package model.football.player;

import model.football.foul.Card;
import model.generic.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FootballPlayer extends Player {
    private int speed;
    private int resistance;
    private int dexterity;
    private int implosion;
    private int headGame;
    private int kick;
    private int passing;
    private Card card;

    public FootballPlayer(String name, int number, String team) {
        super(name, number, team);
        this.setSpeed(0);
        this.setResistance(0);
        this.setDexterity(0);
        this.setImplosion(0);
        this.setHeadGame(0);
        this.setKick(0);
        this.setPassing(0);
        this.setCard(Card.NONE);
    }

    @Override
    public int calcOverallSkill() {
        int sum = this.getSpeed()      +
                  this.getResistance() +
                  this.getDexterity()  +
                  this.getImplosion()  +
                  this.getHeadGame()   +
                  this.getKick()       +
                  this.getPassing();
        return sum / 7;
    }

    public FootballPlayer(String name,
                          int number,
                          String team,
                          int speed,
                          int resistance,
                          int dexterity,
                          int implosion,
                          int headGame,
                          int kick,
                          int passing) {
        super(name, number, team);
        this.changeSpeed(speed);
        this.changeResistance(resistance);
        this.changeDexterity(dexterity);
        this.changeImplosion(implosion);
        this.changeHeadGame(headGame);
        this.changeKick(kick);
        this.changePassing(passing);
        this.setCard(Card.NONE);
    }

    public FootballPlayer(
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
            Card card
    ) {
        super(name, number, team, background, overallSkill);
        this.changeSpeed(speed);
        this.changeResistance(resistance);
        this.changeDexterity(dexterity);
        this.changeImplosion(implosion);
        this.changeHeadGame(headGame);
        this.changeKick(kick);
        this.changePassing(passing);
        this.setCard(card);
    }

    public FootballPlayer(FootballPlayer player) {
        super(player);
        this.changeSpeed(player.getSpeed());
        this.changeResistance(player.getResistance());
        this.changeDexterity(player.getDexterity());
        this.changeImplosion(player.getImplosion());
        this.changeHeadGame(player.getHeadGame());
        this.changeKick(player.getKick());
        this.changePassing(player.getPassing());
        this.setCard(player.getCard());
    }

    public void setSpeed(int speed) {
        if(speed > 100) speed = 100;
        else if(speed < 0) speed = 0;
        this.speed = speed;
    }
    public int getSpeed() {
        return this.speed;
    }
    public void changeSpeed(int speed) {
        this.setSpeed(speed);
        this.updateOverallSkill();
    }

    public void setResistance(int resistance) {
        if(resistance > 100) resistance = 100;
        else if(resistance < 0) resistance = 0;
        this.resistance = resistance;
    }
    public int getResistance() {
        return this.resistance;
    }
    public void changeResistance(int resistance) {
        this.setResistance(resistance);
        this.updateOverallSkill();
    }

    public void setDexterity(int dexterity) {
        if(dexterity > 100) dexterity = 100;
        else if(dexterity < 0) dexterity = 0;
        this.dexterity = dexterity;
    }
    public int getDexterity() {
        return dexterity;
    }
    public void changeDexterity(int dexterity) {
        this.setDexterity(dexterity);
        this.updateOverallSkill();
    }

    public void setImplosion(int implosion) {
        if(implosion > 100) implosion = 100;
        else if(implosion < 0) implosion = 0;
        this.implosion = implosion;
    }
    public int getImplosion() {
        return implosion;
    }
    public void changeImplosion(int implosion) {
        this.setImplosion(implosion);
        this.updateOverallSkill();
    }

    public void setHeadGame(int headGame) {
        if(headGame > 100) headGame = 100;
        else if(headGame < 0) headGame = 0;
        this.headGame = headGame;
    }
    public int getHeadGame() {
        return headGame;
    }
    public void changeHeadGame(int headGame) {
        this.setHeadGame(headGame);
        this.updateOverallSkill();
    }

    public void setKick(int kick) {
        if(kick > 100) kick = 100;
        else if(kick < 0) kick = 0;
        this.kick = kick;
    }
    public int getKick() {
        return kick;
    }
    public void changeKick(int kick) {
        this.setKick(kick);
        this.updateOverallSkill();
    }

    public void setPassing(int passing) {
        if(passing > 100) passing = 100;
        else if(passing < 0) passing = 0;
        this.passing = passing;
    }
    public int getPassing() {
        return passing;
    }
    public void changePassing(int passing) {
        this.setPassing(passing);
        this.updateOverallSkill();
    }

    public void setCard(Card card) {
        this.card = card;
    }
    public Card getCard() {
        return card;
    }

    public boolean equals(FootballPlayer player) {
        boolean ret = false;

        if(this == player)
            ret = true;
        else if
        (
                super.equals(player)                           &&
                this.getSpeed()      == player.getSpeed()      &&
                this.getResistance() == player.getResistance() &&
                this.getDexterity()  == player.getDexterity()  &&
                this.getImplosion()  == player.getImplosion()  &&
                this.getHeadGame()   == player.getHeadGame()   &&
                this.getKick()       == player.getKick()       &&
                this.getPassing()    == player.getPassing()    &&
                this.getCard()       == player.getCard()
        ) ret = true;

        return ret;
    }

    @Override
    public FootballPlayer clone() {
        return new FootballPlayer(this);
    }

    public static FootballPlayer fromCSV(String line) {
        String[] elems = line.split(";");
        List<String> background;

        if(!elems[3].equals("")) {
            background = Arrays.asList(elems[3].split(","));
        } else background = new ArrayList<>();

        return new FootballPlayer
                (
                        elems[0],
                        Integer.parseInt(elems[1]),
                        elems[2],
                        background,
                        Integer.parseInt(elems[4]),
                        Integer.parseInt(elems[5]),
                        Integer.parseInt(elems[6]),
                        Integer.parseInt(elems[7]),
                        Integer.parseInt(elems[8]),
                        Integer.parseInt(elems[9]),
                        Integer.parseInt(elems[10]),
                        Integer.parseInt(elems[11]),
                        Card.valueOf(elems[12])
                );
    }

    public String toCSV() {
        StringBuilder str = new StringBuilder(super.toCSV()).append(';');

        str.append(this.getSpeed()).append(';');
        str.append(this.getResistance()).append(';');
        str.append(this.getDexterity()).append(';');
        str.append(this.getImplosion()).append(';');
        str.append(this.getHeadGame()).append(';');
        str.append(this.getKick()).append(';');
        str.append(this.getPassing()).append(';');
        str.append(this.getCard().toString());

        return str.toString();
    }
}
