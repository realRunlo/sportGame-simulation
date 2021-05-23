package model.generic.team;


import java.io.Serializable;

public abstract class Team implements Serializable {
    private String name;
    private int GlobalSkill;

    public Team(){
        setName(" ");
        setGlobalSkill(0);
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
        return GlobalSkill;
    }

    public void setGlobalSkill(int nGS){
        GlobalSkill = nGS;
    }

    public boolean equals(Object obj){
        if(obj == this) return true;
        if(obj == null || obj.getClass() != this.getClass()) return false;

        Team t = (Team) obj;
        return name.equals(t.getName()) && GlobalSkill == t.getGlobalSkill();
    }


    public String toString(){
        return "Team: " + getName() + "\nGlobal Skill: " + getGlobalSkill() + "\n";
    }

    public abstract Team clone();
}
