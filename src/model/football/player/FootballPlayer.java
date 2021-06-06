package model.football.player;

import model.football.foul.Card;
import model.generic.player.Player;
import model.interfaces.Saveable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public abstract class FootballPlayer extends Player implements Saveable {
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
        this.changeSpeed(0);
        this.changeResistance(0);
        this.changeDexterity(0);
        this.changeImplosion(0);
        this.changeHeadGame(0);
        this.changeKick(0);
        this.changePassing(0);
        this.setCard(Card.NONE);
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

    public FootballPlayer
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
                    Card card
            ) {
        super(name, number, team, background);
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


    public boolean equals(Object o) {
        if (this == o)
            return true;

        if(o == null || !this.getClass().equals(o.getClass()))
            return false;

        FootballPlayer player = (FootballPlayer) o;

        return super.equals(player) &&
                this.getSpeed() == player.getSpeed() &&
                this.getResistance() == player.getResistance() &&
                this.getDexterity() == player.getDexterity() &&
                this.getImplosion() == player.getImplosion() &&
                this.getHeadGame() == player.getHeadGame() &&
                this.getKick() == player.getKick() &&
                this.getPassing() == player.getPassing() &&
                this.getCard() == player.getCard();
    }

    public abstract FootballPlayer clone();

    public String toCSV() {
        return super.toCSV()         + ";" +
                this.getSpeed()      + ";" +
                this.getResistance() + ";" +
                this.getDexterity()  + ";" +
                this.getImplosion()  + ";" +
                this.getHeadGame()   + ";" +
                this.getKick()       + ";" +
                this.getPassing()    + ";" +
                this.getCard();
    }

}
