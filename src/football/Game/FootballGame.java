package football;

import football.team.lineup.FootballLineup;
import generic.Game.Game;

public class FootballGame extends Game {
    private FootballLineup home;
    private FootballLineup away;

    public FootballGame() {
        super();

        FootballLineup flHome = new FootballLineup();
        FootballLineup flAway = new FootballLineup();

        this.setHome(flHome);
        this.setAway(flAway);
    }

    public FootballGame(FootballLineup home, FootballLineup away) {
        super();
        this.setHome(home);
        this.setAway(away);
    }

    public FootballGame(FootballGame fg) {
        super(fg);
        this.setHome(fg.getHome());
        this.setAway(fg.getAway());
    }

    public void setHome(FootballLineup home) {
        this.home = new FootballLineup(home);
    }
    public FootballLineup getHome() {
        return new FootballLineup(this.home);
    }

    public void setAway(FootballLineup away) {
        this.away = new FootballLineup(away);
    }
    public FootballLineup getAway() {
        return new FootballLineup(this.away);
    }

    public boolean equals(FootballGame fg) {
        boolean bool = false;
        if(this == fg)
            bool = true;
        else if(this.getHome().equals(fg.getHome()) && this.getAway().equals(fg.getHome()))
            bool = true;

        return bool;
    }

    public FootballGame clone() {
        return new FootballGame(this);
    }
}
