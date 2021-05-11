package model.generic.player;

import model.Saveable;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public abstract class Player implements Saveable {
    private String name;
    private int number;
    private String curTeam;
    private List<String> background;
    private int overallSkill;

    public Player(String name, int number) {
        this.setName(name);
        this.number = number;
        this.setCurTeam("");
        this.background = new ArrayList<>();
        this.setOverallSkill(0);
    }

    public Player(String name, int number, String team) {
        this.setName(name);
        this.number = number;
        this.background = new ArrayList<>();
        this.setCurTeam(team);
        this.setOverallSkill(0);
    }

    public Player(String name, int number, String team, List<String> background) {
        this.setName(name);
        this.number = number;
        this.setCurTeam(team);
        this.background = new ArrayList<>(background);
        this.setOverallSkill(0);
    }

    public Player(String name, int number, List<String> background) {
        this.setName(name);
        this.number = number;
        this.setCurTeam("");
        this.background = new ArrayList<>(background);
        this.setOverallSkill(0);
    }

    public Player(String name, int newNumber, String newTeam, List<String> background, int newSkill) {
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

    public void setNumber(int newNumber){
        this.number = newNumber;
    }
    public int getNumber() {
        return this.number;
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

    public String backgroundCSV() {
        List<String> background = this.getBackground();
        StringBuilder str = new StringBuilder();
        int len = background.size();
        int i;

        for(i = 0; i < len - 1; i++) {
            str.append(background.get(i)).append(',');
        }

        if(len > 0)
            str.append(background.get(i));

        return str.toString();
    }

    public String toCSV() {
        StringBuilder str = new StringBuilder();

        str.append(this.getName()).append(';');
        str.append(this.getNumber()).append(';');
        str.append(this.getCurTeam()).append(';');
        str.append(this.backgroundCSV()).append(';');
        str.append(this.getOverallSkill());

        return str.toString();
    }

    public void save(String fileName) throws FileNotFoundException {
        PrintWriter file = new PrintWriter(fileName);

        file.println(this.toCSV());
        file.flush();
        file.close();
    }
}
