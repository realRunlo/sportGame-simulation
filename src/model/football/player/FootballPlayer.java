package model.football.player;

import model.football.foul.Card;
import model.generic.player.Player;
import model.interfaces.Saveable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public abstract class FootballPlayer extends Player implements Saveable {
    private int speed;
    private int resistance;
    private int dexterity;
    private int implosion;
    private int headGame;
    private int kick;
    private int passing;
    private Card card;

    /**
     * Construtor de FootballPlauer
     * @param name nome do jogador
     * @param number numero da camisola
     * @param team equipa do jogador
     */
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

    /**
     * Construtor de FootballPlayer
     * @param name nome do jogador
     * @param number numero da camisola
     * @param team equipa do jogador
     * @param speed velocidade do jogador
     * @param resistance resistencia do jogador
     * @param dexterity destreza do jogador
     * @param implosion implosao do jogador
     * @param headGame jogo de cabeca do jogador
     * @param kick pontape do jogdor
     * @param passing passe do jogador
     */
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

    /**
     * Construtor de FootballPlayer
     * @param name nome do jogador
     * @param number numero da camisola
     * @param team equipa do jogador
     * @param background lista de equipas por onde o jogador ja passou
     * @param speed velocidade do jogador
     * @param resistance resistencia do jogador
     * @param dexterity destreza do jogador
     * @param implosion implosao do jogador
     * @param headGame jogo de cabeca do jogador
     * @param kick pontape do jogdor
     * @param passing passe do jogador
     * @param card cartao do jogador
     */
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

    /**
     * Construtor de FootballPlayer
     * @param player jogador a copiar
     */
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

    /**
     * Setter de speed
     * @param speed novo valor
     */
    public void setSpeed(int speed) {
        if(speed > 100) speed = 100;
        else if(speed < 0) speed = 0;
        this.speed = speed;
    }

    /**
     * Getter de speed
     * @return speed
     */
    public int getSpeed() {
        return this.speed;
    }

    /**
     * Modificador de speed
     * @param speed novo speed
     */
    public void changeSpeed(int speed) {
        this.setSpeed(speed);
        this.updateOverallSkill();
    }

    /**
     * Setter de resistance
     * @param resistance novo resistance
     */
    public void setResistance(int resistance) {
        if(resistance > 100) resistance = 100;
        else if(resistance < 0) resistance = 0;
        this.resistance = resistance;
    }

    /**
     * Getter de resistance
     * @return resistance
     */
    public int getResistance() {
        return this.resistance;
    }

    /**
     * Modificador de resistance
     * @param resistance novo resistance
     */
    public void changeResistance(int resistance) {
        this.setResistance(resistance);
        this.updateOverallSkill();
    }

    /**
     * Setter de dexterity
     * @param dexterity novo dexterity
     */
    public void setDexterity(int dexterity) {
        if(dexterity > 100) dexterity = 100;
        else if(dexterity < 0) dexterity = 0;
        this.dexterity = dexterity;
    }

    /**
     * Getter de dexterity
     * @return dexterity
     */
    public int getDexterity() {
        return dexterity;
    }

    /**
     * Modificador de dexterity
     * @param dexterity novo dexterity
     */
    public void changeDexterity(int dexterity) {
        this.setDexterity(dexterity);
        this.updateOverallSkill();
    }

    /**
     * Setter de implosion
     * @param implosion novo implosion
     */
    public void setImplosion(int implosion) {
        if(implosion > 100) implosion = 100;
        else if(implosion < 0) implosion = 0;
        this.implosion = implosion;
    }

    /**
     * Getter de implosion
     * @return implosion
     */
    public int getImplosion() {
        return implosion;
    }

    /**
     * Modificador de implosion
     * @param implosion novo implosion
     */
    public void changeImplosion(int implosion) {
        this.setImplosion(implosion);
        this.updateOverallSkill();
    }

    /**
     * Setter de headGame
     * @param headGame novo headGame
     */
    public void setHeadGame(int headGame) {
        if(headGame > 100) headGame = 100;
        else if(headGame < 0) headGame = 0;
        this.headGame = headGame;
    }

    /**
     * Getter de headGame
     * @return headGame
     */
    public int getHeadGame() {
        return headGame;
    }

    /**
     * Modificador de headGame
     * @param headGame novo headGame
     */
    public void changeHeadGame(int headGame) {
        this.setHeadGame(headGame);
        this.updateOverallSkill();
    }

    /**
     * Setter de kick
     * @param kick novo kick
     */
    public void setKick(int kick) {
        if(kick > 100) kick = 100;
        else if(kick < 0) kick = 0;
        this.kick = kick;
    }

    /**
     * Getter de kick
     * @return kick
     */
    public int getKick() {
        return kick;
    }

    /**
     * Modificador de kick
     * @param kick novo kick
     */
    public void changeKick(int kick) {
        this.setKick(kick);
        this.updateOverallSkill();
    }

    /**
     * Setter de passing
     * @param passing novo passing
     */
    public void setPassing(int passing) {
        if(passing > 100) passing = 100;
        else if(passing < 0) passing = 0;
        this.passing = passing;
    }

    /**
     * Getter de passing
     * @return passing
     */
    public int getPassing() {
        return passing;
    }

    /**
     * Modificador de passing
     * @param passing novo passing
     */
    public void changePassing(int passing) {
        this.setPassing(passing);
        this.updateOverallSkill();
    }

    /**
     * Setter de card
     * @param card novo card
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * Getter de card
     * @return card
     */
    public Card getCard() {
        return card;
    }

    /**
     * Calcula o valor medio das habilidades, utilizado para
     * gerar novos valores para habilidades nao definidas nos carregamentos
     * @param speed velocidade do jogador
     * @param resistance resistencia do jogador
     * @param dexterity destreza do jogador
     * @param implosion implosao do jogador
     * @param headGame jogo de cabeca do jogador
     * @param kick pontape do jogador
     * @param passing passe do jogador
     * @return
     */
    public static int averageValue(int speed, int resistance, int dexterity, int implosion, int headGame, int kick, int passing){
        Random rand = new Random();

        return ((speed+resistance+dexterity+implosion+headGame+kick+passing)/7) -5 + rand.nextInt(11);
    }

    /**
     * Equals da classe FootballPlayer
     * @param o objeto a comparar
     * @return resultado da comparacao
     */
    @Override
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

    /**
     * Cloner da classe FootballPlayer
     * @return clone
     */
    @Override
    public abstract FootballPlayer clone();

    /**
     * torna um FootballPlayer em uma string saveable
     * @return string com os dados do jogador
     */
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
