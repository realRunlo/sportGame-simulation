package model.generic.team;


import model.interfaces.Saveable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public abstract class Team implements Saveable, Serializable {
    private String name;
    private int globalSkill;

    /**
     * Construtor de Team
     */
    public Team(){
        setName(" ");
        setGlobalSkill(0);
    }

    /**
     * Construtor de Team
     * @param name nome da equipa
     */
    public Team(String name) {
        this.setName(name);
        this.setGlobalSkill(0);
    }

    /**
     * Construtor de Team
     * @param newName nome da equipa
     * @param newGlobalSkill habilidade media da equipa
     */
    public Team(String newName, int newGlobalSkill){
        setName(newName);
        setGlobalSkill(newGlobalSkill);
    }

    /**
     * Construtor de team
     * @param t equipa a copiar
     */
    public Team(Team t){
        setName(t.getName());
        setGlobalSkill(t.getGlobalSkill());
    }

    /**
     * Getter do nome
     * @return nome
     */
    public String getName(){
        return name;
    }

    /**
     * Setter do nome
     * @param newName novo nome
     */
    public void setName(String newName){
        name = newName;
    }

    /**
     * Getter da habilidade media
     * @return habilidade media
     */
    public int getGlobalSkill(){
        return globalSkill;
    }

    /**
     * Setter da habilidade media
     * @param nGS nova habilidade media
     */
    public void setGlobalSkill(int nGS){
        globalSkill = nGS;
    }

    /**
     * Equals de Team
     * @param obj objeto a comparar
     * @return resultado da comparacao
     */
    public boolean equals(Object obj){
        if(obj == this) return true;
        if(obj == null || obj.getClass() != this.getClass()) return false;

        Team t = (Team) obj;
        return name.equals(t.getName()) && globalSkill == t.getGlobalSkill();
    }

    /**
     * ToString de Team
     * @return Team em formato string
     */
    public String toString(){
        return "Team: " + getName() + "\nGlobal Skill: " + getGlobalSkill() + "\n";
    }

    /**
     * Clone de Team
     * @return clone
     */
    public abstract Team clone();

    /**
     * Torna Team em uma string saveable
     * @return string saveable
     */
    public String toCSV() {
        return "" + this.getName();
    }

    /**
     * Grava Team em um ficheiro especificado
     * @param filePath ficheiro onde gravar
     * @throws IOException
     */
    public void save(String filePath) throws IOException {
        BufferedWriter br = new BufferedWriter(new FileWriter(filePath, true));

        br.write(this.toCSV());
        br.flush();
        br.close();
    }
}
