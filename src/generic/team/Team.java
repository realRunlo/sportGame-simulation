package generic.team;


public abstract class Team {
    private String name;
    private int GlobalSkill;

    public Team(){
        name = " ";
        GlobalSkill = 0;
    }

    public Team(String newName, int newGlobalSkill){
        name = newName;
        GlobalSkill = newGlobalSkill;
    }

    public Team(Team t){
        name = t.getName();
        GlobalSkill = t.getGlobalSkill();
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
        return "Nome de equipa: " + getName() + "\nGlobal Skill: " + getGlobalSkill() + "\n";
    }
}
