package generic.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Player implements Serializable {
    private String name;
    private int number;
    private String curTeam;
    private List<String> background;
    private int overallSkill;

    public Player(String name) {
        this.setName(name);
        this.number = 0;
        this.setCurTeam("");
        this.background = new ArrayList<>();
        this.setOverallSkill(0);
    }

    public Player(String name, String team) {
        this.setName(name);
        this.number = 0;
        this.background = new ArrayList<>();
        this.setCurTeam(team);
        this.setOverallSkill(0);
    }

    public Player(String name, String team,int number) {
        this.setName(name);
        this.number = number;
        this.background = new ArrayList<>();
        this.setCurTeam(team);
        this.setOverallSkill(0);
    }

    public Player(String name, String team, List<String> background) {
        this.setName(name);
        this.number = 0;
        this.setCurTeam(team);
        this.background = new ArrayList<>(background);
        this.setOverallSkill(0);
    }

    public Player(String name, List<String> background) {
        this.setName(name);
        this.number = 0;
        this.setCurTeam("");
        this.background = new ArrayList<>(background);
        this.setOverallSkill(0);
    }

    public Player(String name,int newNumber, String newTeam, List<String> background, int newSkill) {
        this.setName(name);
        this.number = newNumber;
        this.setCurTeam(newTeam);
        this.setBackgorund(background);
        this.setOverallSkill(newSkill);
    }

    public Player(Player player) {
        this.setName(player.getName());
        this.number = player.getNumber();
        this.setCurTeam(player.getCurTeam());
        this.background = new ArrayList<>(player.getBackground());
        this.setOverallSkill(player.getOverallSkill());
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public int getNumber(){
        return number;
    }

    public void setNumber(int newNumber){
        number = newNumber;
    }

    public void setCurTeam(String team) {
        this.curTeam = team;
    }
    public String getCurTeam() {
        return this.curTeam;
    }

    public void setBackgorund(List<String> newBackground){
        background = new ArrayList<>(newBackground);
    }
    public void addBackground(String team) {
        this.background.add(team);
    }
    public List<String> getBackground() {
        return new ArrayList<>(this.background);
    }

    private void setOverallSkill(int overallSkill) {
        this.overallSkill = overallSkill;
    }
    public int getOverallSkill() {
        return overallSkill;
    }
    public void updateOverallSkill() {
        setOverallSkill(this.calcOverallSkill());
    }
    public abstract int calcOverallSkill();

    public boolean equals(Player player) {
        boolean ret = false;

        if(this == player)
            ret = true;
        else if
        (
                this.getClass() == player.getClass()                &&
                this.getName().equals(player.getName())             &&
                this.getCurTeam().equals(player.getCurTeam())       &&
                this.getBackground().equals(player.getBackground()) &&
                this.getOverallSkill() == player.getOverallSkill()
        ) ret = true;

        return ret;
    }

}
