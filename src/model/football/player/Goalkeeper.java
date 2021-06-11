package model.football.player;

import model.football.foul.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Goalkeeper extends FootballPlayer{
    private int elasticity;

    /**
     * Construtor de Goalkeeper
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
     * @param elasticity elasticidade do jogador
     */
    public Goalkeeper
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
                    int elasticity
            )
    {
        super(name, number, team, speed, resistance, dexterity, implosion, headGame, kick, passing);
        this.changeElasticity(elasticity);
    }

    /**
     * Construtor de Goalkeeper
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
     * @param elasticity elasticidade do jogador
     */
    public Goalkeeper
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
                    int elasticity
            )
    {
        super(name, number, team, background, speed, resistance, dexterity, implosion, headGame, kick, passing, card);
        this.changeElasticity(elasticity);
    }


    /**
     * Construtor de Goalkeeper
     * @param goalkeeper jogador a copiar
     */
    public Goalkeeper(Goalkeeper goalkeeper) {
        super(goalkeeper);
        this.changeElasticity(goalkeeper.getElasticity());
    }

    /**
     * Modificador de elasticidade
     * @param elasticity nova elasticidade
     */
    public void changeElasticity(int elasticity) {
        setElasticity(elasticity);
        updateOverallSkill();
    }

    /**
     * Setter de elasticidade
     * @param elasticity nova elasticidade
     */
    private void setElasticity(int elasticity) {
        if(elasticity > 100) elasticity = 100;
        else if(elasticity < 0) elasticity = 0;
        this.elasticity = elasticity;
    }

    /**
     * Getter de elasticidade
     * @return elasticidade
     */
    public int getElasticity() {
        return this.elasticity;
    }

    /**
     * Calculo da habilidade de um guarda-redes
     * @return habilidade
     */
    @Override
    public int calcOverallSkill() {
        return (int) ((this.getSpeed() * 0.15)      +
                      (this.getResistance() * 0.15) +
                      (this.getDexterity() * 0.15)  +
                      (this.getImplosion() * 0.05)  +
                      (this.getHeadGame() * 0.05)   +
                      (this.getKick() * 0.05)       +
                      (this.getPassing() * 0.10)    +
                      (this.getElasticity() * 0.30));
    }

    /**
     * ToString da classe Goalkeeper
     * @return goalkeeper no formato de string
     */
    @Override
    public String toString(){
        return  new String("Team : " + getCurTeam()
                + "\nGoalkeeper: "+getName() + "\nShirt: "+ getNumber()
                + "\nSpeed: " + getSpeed()
                + "\nResistance: " + getResistance()
                + "\nDexterity: " + getDexterity()
                + "\nImplosion: " + getImplosion()
                + "\nHeadGame: " + getHeadGame()
                + "\nKick: " + getKick()
                + "\nPassing: " + getPassing()
                + "\nElasticity: " + getElasticity()
                + "\nOverall Skill: " + calcOverallSkill()
                + "\nBackground: " + getBackground().toString()
        );
    }

    /**
     * Equals da classe Goalkeeper
     * @param o objeto a comparar
     * @return resultado da comparacao
     */
    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;

        if(o == null || !this.getClass().equals(o.getClass()))
            return false;

        Goalkeeper goalkeeper = (Goalkeeper) o;

        return super.equals(goalkeeper) && this.getElasticity() == goalkeeper.getElasticity();
    }

    /**
     * Cloner da classe Goalkeeper
     * @return clone
     */
    @Override
    public Goalkeeper clone() {
        return new Goalkeeper(this);
    }

    /**
     * Torna goalkeeper em uma string saveable
     * @return string saveable
     */
    public String toCSV() {
        return "Guarda-Redes:" +
                super.toCSV() + ";" +
                this.getElasticity() + "\n";
    }

    /**
     * Carrega um goalkeeper a partir de uma sting
     * @param csvLine string a analisar
     * @param team equipa do guarda-redes
     * @param teacher indicador do formato do ficheiro de dados
     * @return Goalkeeper carregado
     */
    public static Goalkeeper load(String csvLine, String team, boolean teacher) {
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
        int elasticity;
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
            elasticity = Integer.parseInt(tokens[9]);
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
            elasticity = Integer.parseInt(tokens[12]);
        }

        name = tokens[0];
        number = Integer.parseInt(tokens[1]);

        return new Goalkeeper
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
                        elasticity
                );
    }

}
