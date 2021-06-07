package model.generic.team;


import model.interfaces.Saveable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public abstract class Team implements Saveable, Serializable {
    private String name;
    private int globalSkill;

    public Team(){
        setName(" ");
        setGlobalSkill(0);
    }

    public Team(String name) {
        this.setName(name);
        this.setGlobalSkill(0);
    }

    public Team(String newName, int newGlobalSkill){
        setName(newName);
        setGlobalSkill(newGlobalSkill);
    }

    public Team(Team t){
        setName(t.getName());
        setGlobalSkill(t.getGlobalSkill());
    }

    public String getName(){
        return name;
    }

    public void setName(String newName){
        name = newName;
    }


    public int getGlobalSkill(){
        return globalSkill;
    }

    public void setGlobalSkill(int nGS){
        globalSkill = nGS;
    }

    public boolean equals(Object obj){
        if(obj == this) return true;
        if(obj == null || obj.getClass() != this.getClass()) return false;

        Team t = (Team) obj;
        return name.equals(t.getName()) && globalSkill == t.getGlobalSkill();
    }


    public String toString(){
        return "Team: " + getName() + "\nGlobal Skill: " + getGlobalSkill() + "\n";
    }

    public abstract Team clone();

    public String toCSV() {
        return "" + this.getName();
    }

    public void save(String filePath) throws IOException {
        BufferedWriter br = new BufferedWriter(new FileWriter(filePath, true));

        br.write(this.toCSV());
        br.flush();
        br.close();
    }
}
