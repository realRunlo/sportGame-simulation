package model.generic.player;

import model.interfaces.Saveable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Player implements Saveable,Serializable {
    private String name;
    private int number;
    private String curTeam;
    private List<String> background;
    private int overallSkill;

    /**
     * Construtor de Player
     * @param name nome do jogador
     */
    public Player(String name) {
        this.setName(name);
        this.setNumber(0);
        this.setCurTeamNone();
        this.setBackground(new ArrayList<>());
        this.setOverallSkill(0);
    }

    /**
     * Construtor de Player
     * @param name nome do jogador
     * @param number numero de camisola do jogador
     * @param team nome da equipa atual
     */
    public Player(String name, int number, String team) {
        this.setName(name);
        this.setNumber(number);
        this.setBackground(new ArrayList<>());
        addBackground(team);
        this.setCurTeam(team);
        this.setOverallSkill(0);
    }

    /**
     * Construtor de Player
     * @param name nome do jogador
     * @param number camisola do jogador
     * @param team nome da equipa atual
     * @param background lista de equipas por onde o jogador passou
     */
    public Player(String name, int number, String team, List<String> background) {
        this.setName(name);
        this.setNumber(number);
        this.setCurTeam(team);
        this.setBackground(background);
        if(!team.equals("None")) addBackground(team);
        this.setOverallSkill(0);
    }

    /**
     * Construtor de Player
     * @param name nome do jogador
     * @param number numero da camisola
     * @param background lista de equipas por onde o jogador passou
     */
    public Player(String name, int number, List<String> background) {
        this.setName(name);
        this.setNumber(number);
        this.setCurTeamNone();
        this.setBackground(background);
        this.setOverallSkill(0);
    }

    /**
     * Construtor de Player
     * @param name nome do jogador
     * @param newNumber numero da camisola
     * @param newTeam nome da equipa atual
     * @param background lista de equipas por onde o jogador passou
     * @param newSkill valor de habilidade
     */
    public Player(String name,int newNumber, String newTeam, List<String> background, int newSkill) {
        this.setName(name);
        this.setNumber(newNumber);
        this.setCurTeam(newTeam);
        this.setBackground(background);
        this.setOverallSkill(newSkill);
    }

    /**
     * Construtor de Player
     * @param player jogador a copiar
     */
    public Player(Player player) {
        this.setName(player.getName());
        this.setNumber(player.getNumber());
        this.setCurTeam(player.getCurTeam());
        this.setBackground(player.getBackground());
        this.setOverallSkill(player.getOverallSkill());
    }

    /**
     * Setter de nome
     * @param name novo nome
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter de nome
     * @return nome
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter de numero de camisola
     * @return numero de camisola
     */
    public int getNumber(){
        return number;
    }

    /**
     * Setter de numero de camisola
     * @param newNumber novo numero de camisola
     */
    public void setNumber(int newNumber){
        number = newNumber;
    }

    /**
     * Setter de equipa atual
     * @param team nova equipa atual
     */
    public void setCurTeam(String team) {
        this.curTeam = team;
    }

    /**
     * Getter de equipa atual
     * @return equipa atual
     */
    public String getCurTeam() {
        return this.curTeam;
    }

    /**
     * Setter de equipa atual nula
     */
    public void setCurTeamNone(){
        setCurTeam("None");
    }

    /**
     * Setter de lista de equipas por onde passou
     * @param newBackground nova lista
     */
    public void setBackground(List<String> newBackground){
        background = new ArrayList<>(newBackground);
    }

    /**
     * Adiciona uma nova equipa na lista de equipas por onde passou
     * @param team nome da equipa a adicionar na lista
     */
    public void addBackground(String team) {
        List<String> b = getBackground();
        if(b.size() > 0){
            if(!b.get(b.size()-1).equals(team))
                b.add(team);
        }else b.add(team);

        setBackground(b);
    }

    /**
     * Getter da lista de equipas por onde passou
     * @return lista
     */
    public List<String> getBackground() {
        return new ArrayList<>(this.background);
    }

    /**
     * Setter da habilidade do jogador
     * @param overallSkill nova habilidade
     */
    private void setOverallSkill(int overallSkill) {
        this.overallSkill = overallSkill;
    }

    /**
     * Getter de habilidade
     * @return habilidade
     */
    public int getOverallSkill() {
        return overallSkill;
    }

    /**
     * Atualiza a habilidade do jogador
     */
    public void updateOverallSkill() {
        setOverallSkill(this.calcOverallSkill());
    }

    /**
     * Obriga as classes herdeiras a implementarem um metodo
     * que calcula a habilidade
     * @return habilidade calculada
     */
    public abstract int calcOverallSkill();

    /**
     * Equals de Player
     * @param o objeto a comparar
     * @return resultado da comparacao
     */
    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || !o.getClass().equals(this.getClass()))
            return false;

        Player p = (Player) o;

        return this.getName().equals(p.getName())             &&
                this.getCurTeam().equals(p.getCurTeam())       &&
                this.getBackground().equals(p.getBackground()) &&
                this.getOverallSkill() == p.getOverallSkill();
    }

    /**
     * Implementa um hashCode especifico
     * @return valor do hashcode
     */
    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    /**
     * Clone de Player
     * @return clone
     */
    @Override
    public abstract Player clone();

    /**
     * Torna Player em uma string saveable
     * @return string saveable
     */
    public String toCSV(){
        StringBuilder csv = new StringBuilder();
        List<String> bg = this.getBackground();
        int bgSize = bg.size();
        int i;

        csv.append(this.getName()).append(";")
                .append(this.getNumber()).append(";")
                .append(this.getCurTeam()).append(";");

        for(i = 0; i < bgSize - 1; i++) {
            csv.append(bg.get(i)).append(";");
        }
        if(bgSize != 0) {
            csv.append(bg.get(i));
        }

        return csv.toString();
    }

    /**
     * Grava um jogador em ficheiro
     * @param filePath ficheiro onde gravar
     * @throws IOException
     */
    public void save(String filePath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));

        bw.write(this.toCSV());
        bw.flush();
        bw.close();
    }
}
