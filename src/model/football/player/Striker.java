package model.football.player;

import model.football.foul.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Striker extends FootballPlayer {
    private int shooting;

    /**
     * Construtor de Striker
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
     * @param shooting remate do jogador
     */
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

    /**
     * Construtor de Striker
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
     * @param shooting remate do jogador
     */
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

    /**
     * Construtor da classe Striker
     * @param striker jogador a copiar
     */
    public Striker(Striker striker) {
        super(striker);
        this.changeShooting(striker.getShooting());
    }


    /**
     * Calculo da habilidade de um striker
     * @return habilidade do jogador
     */
    @Override
    public int calcOverallSkill() {
        return (int) (
                (this.getSpeed() * 0.15)      +
                (this.getResistance() * 0.10) +
                (this.getDexterity() * 0.05)  +
                (this.getImplosion() * 0.1)  +
                (this.getHeadGame() * 0.15)   +
                (this.getKick() * 0.2)       +
                (this.getPassing() * 0.05)    +
                (this.getShooting() * 0.2)
        );
    }

    /**
     * Modificador de remate
     * @param shooting novo remate
     */
    public void changeShooting(int shooting) {
        this.setShooting(shooting);
        this.updateOverallSkill();
    }

    /**
     * Getter de remate
     * @return remate
     */
    public int getShooting(){
        return shooting;
    }

    /**
     * Setter de remate
     * @param shooting novo remate
     */
    public void setShooting(int shooting){
        if(shooting > 100) shooting = 100;
        else if(shooting < 0) shooting = 0;
        this.shooting = shooting;
    }

    /**
     * ToString da classe Striker
     * @return Striker no formato de string
     */
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

    /**
     * Equals da classe Striker
     * @param o objeto a comparar
     * @return resultado da comparacao
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if(o == null || !this.getClass().equals(o.getClass()))
            return false;

        Striker striker = (Striker) o;

        return super.equals(striker) && this.getShooting() == striker.getShooting();
    }

    /**
     * Clone da classe Striker
     * @return clone
     */
    @Override
    public Striker clone() {
        return new Striker(this);
    }

    /**
     * Torna Striker em uma string saveable
     * @return string saveable
     */
    public String toCSV() {
        return "Avancado:" +
                super.toCSV() + ";" +
                this.getShooting() + "\n";
    }

    /**
     * Carrega um Striker a partir de uma string
     * @param csvLine string a analisar
     * @param team equipa do lateral
     * @param teacher indicador do formato do ficheiro de dados
     * @return Striker carregado
     */
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