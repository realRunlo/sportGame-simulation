package sportm.generic.player;

import java.util.ArrayList;
import java.util.List;

public class Player {
    String name;
    List<String> background;
    int overallSkill;

    public Player(String name) {
        this.setName(name);
        this.background = new ArrayList<String>();
        this.setOverallSkill(0);
    }

    public Player(String name, String team) {
        this.setName(name);
        this.background = new ArrayList<String>();
        this.addBackground(team);
        this.setOverallSkill(0);
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void addBackground(String team) {
        this.background.add(team);
    }
    public List<String> getBackground() {
        return new ArrayList<String>(this.background);
    }

    public void setOverallSkill(int globalSkill) {
        this.overallSkill = globalSkill;
    }
    public int getGlobalSkill() {
        return overallSkill;
    }
}
