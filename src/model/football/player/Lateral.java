package model.football.player;

import model.football.foul.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lateral extends FootballPlayer {
    private int crossing;

    /**
     * Construtor de Lateral
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
     * @param crossing cruze do jogador
     */
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

    /**
     * Construtor de Lateral
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
     * @param crossing cruze do jogador
     */
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

    /**
     * Construtor da classe Lateral
     * @param lateral jogador a copiar
     */
    public Lateral(Lateral lateral) {
        super(lateral);
        changeCrossing(lateral.getCrossing());
    }

    /**
     * Setter de cruze
     * @param crossing novo cruze
     */
    private void setCrossing(int crossing) {
        if(crossing > 100) crossing = 100;
        else if(crossing < 0) crossing = 0;
        this.crossing = crossing;
    }

    /**
     * Getter de cruze
     * @return cruze
     */
    public int getCrossing() {
        return this.crossing;
    }

    /**
     * Modificador de cruze
     * @param crossing novo cruze
     */
    public void changeCrossing(int crossing) {
       setCrossing(crossing);
       updateOverallSkill();
    }

    /**
     * Calculo da habilidade de um lateral
     * @return habilidade do jogador
     */
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

    /**
     * ToString da classe Lateral
     * @return lateral no formato string
     */
    @Override
    public String toString(){
        return  new String("Team : " + getCurTeam()
                + "\nLateral: "+getName() + "\nShirt: "+ getNumber()
                + "\nSpeed: " + getSpeed()
                + "\nResistance: " + getResistance()
                + "\nDexterity: " + getDexterity()
                + "\nImplosion: " + getImplosion()
                + "\nHeadGame: " + getHeadGame()
                + "\nKick: " + getKick()
                + "\nPassing: " + getPassing()
                + "\nCrossing: " + getCrossing()
                + "\nOverall Skill: " + calcOverallSkill()
                + "\nBackground: " + getBackground().toString()
        );
    }

    /**
     * Equals da classe Lateral
     * @param o objeto a comparar
     * @return resultado da comparacao
     */
    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;

        if(o == null || !this.getClass().equals(o.getClass()))
            return false;

        Lateral lateral = (Lateral) o;

        return super.equals(lateral) && this.getCrossing() == lateral.getCrossing();
    }

    /**
     * Torna lateral em uma string saveable
     * @return string saveable
     */
    public String toCSV() {
        return "Lateral:" +
                super.toCSV() + ";" +
                this.getCrossing() + "\n";
    }

    /**
     * Cloner da classe Lateral
     * @return clone
     */
    @Override
    public Lateral clone() {
        return new Lateral(this);
    }

    /**
     * Carrega um lateral a partir de uma string
     * @param csvLine string a analisar
     * @param team equipa do lateral
     * @param teacher indicador do formato do ficheiro de dados
     * @return lateral carregado
     */
    public static Lateral load(String csvLine, String team, boolean teacher) {
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
        int crossing;
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
            crossing = Integer.parseInt(tokens[9]);
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
            crossing = Integer.parseInt(tokens[12]);
        }

        name = tokens[0];
        number = Integer.parseInt(tokens[1]);

        return new Lateral
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
                        crossing
                );
    }

}
