package model.football.game;

import model.football.player.FootballPlayer;
import model.football.team.FootballTeam;
import model.football.team.lineup.FootballLineup;
import model.generic.game.Game;
import model.generic.team.Team;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class FootballGame extends Game {
    private FootballLineup home;
    private FootballLineup away;

    public FootballGame(Team t1, Team t2) {
        super(t1, t2);
        this.setHome(new FootballLineup());
        this.setAway(new FootballLineup());
    }

    public FootballGame(Team t1, Team t2, FootballLineup home, FootballLineup away) {
        super(t1, t2);
        this.setHome(home);
        this.setAway(away);
    }

    public FootballGame
            (
                    boolean b,
                    int points1,
                    int points2,
                    LocalDate date,
                    int timer,
                    FootballTeam t1,
                    FootballTeam t2,
                    FootballLineup fl1,
                    FootballLineup fl2
            ) {
        super(b, points1, points2, date, timer, t1, t2);
        this.setHome(fl1);
        this.setAway(fl2);
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
    public FootballGame(FootballTeam TeamHome, int strategyHome, FootballTeam TeamAway, int strategyAway){
            super(true,0,0, LocalDate.now(),0,TeamHome,TeamAway);
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

    @Override
    public String toCSV() {
        StringBuilder s = new StringBuilder(super.toCSV()).append(",");

        for(FootballPlayer p: this.getHome().getPlaying())
            s.append(p.getNumber()).append(",");
        for(Substitution sub: this.getHome().getSubstituitions())
            s.append(sub.toCSV()).append(",");
        for(FootballPlayer p: this.getAway().getPlaying())
            s.append(p.getName()).append(",");
        for(Substitution sub: this.getAway().getSubstituitions())
            s.append(sub.toCSV()).append(",");

        return "Jogo:" + s.append("\n");
    }

    public static FootballGame load(String csvLine, Map<String,FootballTeam> teams) {
        String[] tokens = csvLine.split(",");
        FootballLineup home = new FootballLineup();
        FootballLineup away = new FootballLineup();
        FootballTeam t1 = teams.get(tokens[0]);
        FootballTeam t2 = teams.get(tokens[1]);
        String[] subTokens;
        home.setTeamName(t1.getName());
        away.setTeamName(t2.getName());
        int i;
        int awayIndex;

        for(i = 5; i < 16; i++)
            home.addPlaying(t1.getPlayer(Integer.parseInt(tokens[i])));
        for(; tokens[i].contains("->"); i++) {
            subTokens = tokens[i].split("->");
            home.addSubstituition(t1.getPlayer(Integer.parseInt(subTokens[0])), t1.getPlayer(Integer.parseInt(subTokens[1])));
        }

        awayIndex = i;
        for(; i < awayIndex+11; i++)
            away.addPlaying(t2.getPlayer(Integer.parseInt(tokens[i])));
        for(; i < tokens.length; i++) {
            subTokens = tokens[i].split("->");
            away.addSubstituition(t2.getPlayer(Integer.parseInt(subTokens[0])), t2.getPlayer(Integer.parseInt(subTokens[1])));
        }

        LocalDate date = LocalDate.parse(tokens[4],DateTimeFormatter.ISO_LOCAL_DATE);

        return new FootballGame(true, Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), date, 90, t1, t2, home, away);
    } 
}
