package football.Game;

import football.team.FootballTeam;
import football.team.lineup.FootballLineup;
import generic.Game.Game;

import java.io.Serializable;

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

    /**
     * Dados dois nomes de equipas, automaticamente cria uma lineup para cada uma
     * @param TeamHome equipa visitada
     * @param TeamAway equipa visitante
     */
    public FootballGame(FootballTeam TeamHome,int strategyHome, FootballTeam TeamAway,int strategyAway){
            setHome(new FootballLineup(TeamHome,strategyHome));
            setAway(new FootballLineup(TeamAway,strategyAway));
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

    @Override
    public FootballGame clone() {
        return new FootballGame(this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Game \n").append(getPoints1()).append("-").append(getPoints2())
                .append("\nHome: ").append(home.toString())
                .append("Away: ").append(away.toString());
        return sb.toString();
    }
}
