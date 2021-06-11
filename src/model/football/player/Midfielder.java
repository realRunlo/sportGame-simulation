package model.football.player;

import model.football.foul.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Midfielder extends FootballPlayer {
    private int ballRecovery;

    /**
     * Construtor de Midfielder
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
     * @param ballRecovery recuperacao de bola do jogador
     */
    public Midfielder
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
                    int ballRecovery
            )
    {
        super(name, number, team, speed, resistance, dexterity, implosion, headGame, kick, passing);
        this.changeBallRecovery(ballRecovery);
    }

    /**
     * Construtor de Midfielder
     * @param name nome do jogador
     * @param number numero da camisola
     * @param team equipa do jogador
     * @param background lista de equipas por onde o jogador passou
     * @param speed velocidade do jogador
     * @param resistance resistencia do jogador
     * @param dexterity destreza do jogador
     * @param implosion implosao do jogador
     * @param headGame jogo de cabeca do jogador
     * @param kick pontape do jogdor
     * @param passing passe do jogador
     * @param card cartao do jogador
     * @param ballRecovery recuperacao de bola do jogador
     */
    public Midfielder
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
                    int ballRecovery
            )
    {
        super(name, number, team, background, speed, resistance, dexterity, implosion, headGame, kick, passing, card);
        this.changeBallRecovery(ballRecovery);
    }

    /**
     * Construtor de Midfielder
     * @param midfielder jogador a copiar
     */
    public Midfielder(Midfielder midfielder) {
        super(midfielder);
        this.changeBallRecovery(midfielder.getBallRecovery());
    }

    /**
     * Modificar da recuperacao de bola
     * @param ballRecovery nova recuperacao de bola
     */
    public void changeBallRecovery(int ballRecovery) {
        this.setBallRecovery(ballRecovery);
        updateOverallSkill();
    }

    /**
     * Setter da recuperacao de bola
     * @param ballRecovery nova recuperacao de bola
     */
    private void setBallRecovery(int ballRecovery) {
        if(ballRecovery > 100) ballRecovery = 100;
        else if(ballRecovery < 0) ballRecovery = 0;
        this.ballRecovery = ballRecovery;
    }

    /**
     * Getter da recuperacao de bola
     * @return recuperacao de bola
     */
    public int getBallRecovery() {
        return this.ballRecovery;
    }

    /**
     * Calcula a habilidade de um medio
     * @return habilidade do jogador
     */
    @Override
    public int calcOverallSkill() {
        return (int) ((this.getSpeed() * 0.20) +
                (this.getResistance() * 0.15) +
                (this.getDexterity() * 0.05) +
                (this.getImplosion() * 0.05) +
                (this.getHeadGame() * 0.10) +
                (this.getKick() * 0.10) +
                (this.getPassing() * 0.15) +
                (this.getBallRecovery() * 0.20));
    }

    /**
     * Equals da classe Midfielder
     * @param o objeto a comparar
     * @return resultado da comparacao
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if(o == null || !this.getClass().equals(o.getClass()))
            return false;

        Midfielder midfielder = (Midfielder) o;

        return super.equals(midfielder) && this.getBallRecovery() == midfielder.getBallRecovery();
    }

    /**
     * Clone da classe Midfielder
     * @return clone
     */
    @Override
    public Midfielder clone() {
        return new Midfielder(this);
    }

    /**
     * Torna Midfielder em uma string saveable
     * @return string saveable
     */
    public String toCSV() {
        return "Medio:" +
                super.toCSV() + ";" +
                this.getBallRecovery() + "\n";
    }

    /**
     * ToString da classe Midfielder
     * @return midfielder em formato string
     */
    @Override
    public String toString(){
        return  new String("Team : " + getCurTeam()
                + "\nMidfielder: "+getName() + "\nShirt: "+ getNumber()
                + "\nSpeed: " + getSpeed()
                + "\nResistance: " + getResistance()
                + "\nDexterity: " + getDexterity()
                + "\nImplosion: " + getImplosion()
                + "\nHeadGame: " + getHeadGame()
                + "\nKick: " + getKick()
                + "\nPassing: " + getPassing()
                + "\nBall Recovery: " + getBallRecovery()
                + "\nOverall Skill: " + calcOverallSkill()
                + "\nBackground: " + getBackground().toString()
        );
    }

    /**
     * Carrega um Midfielder a partir de uma string
     * @param csvLine string a analisar
     * @param team equipa do jogador
     * @param teacher indicador do formato do ficheiro de dados
     * @return Midfielder carregado
     */
    public static Midfielder load(String csvLine, String team, boolean teacher) {
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
        int ballRecovery;
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
            ballRecovery = Integer.parseInt(tokens[9]);
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
            ballRecovery = Integer.parseInt(tokens[12]);
        }

        name = tokens[0];
        number = Integer.parseInt(tokens[1]);

        return new Midfielder
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
                        ballRecovery
                );
    }

}
