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

    /**
     * Construtor de FootballGame
     * @param t1 equipa da casa
     * @param t2 equipa visitante
     */
    public FootballGame(Team t1, Team t2) {
        super(t1, t2);
        this.setHome(new FootballLineup());
        this.setAway(new FootballLineup());
    }

    /**
     * Construtor de FootballGame
     * @param t1 equipa da casa
     * @param t2 equipa visitante
     * @param home plantel da equipa da casa
     * @param away plantel da equipa visitante
     */
    public FootballGame(Team t1, Team t2, FootballLineup home, FootballLineup away) {
        super(t1, t2);
        this.setHome(home);
        this.setAway(away);
    }

    /**
     * Construtor de FootballGame
     * @param b booleano que indica se o jogo ja terminou
     * @param points1 score da equipa da casa
     * @param points2 score da equipa visitante
     * @param date data de criacao do jogo
     * @param timer contador
     * @param t1 equipa da casa
     * @param t2 equipa visitante
     * @param fl1 plantel da equipa da casa
     * @param fl2 plantel da equipa visitante
     */
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

    /**
     * Construtor de FootballGame
     * @param fg jogo a copiar
     */
    public FootballGame(FootballGame fg) {
        super(fg);
        this.setHome(fg.getHome());
        this.setAway(fg.getAway());
    }

    /**
     * Construtor de FootballGame
     * cria automaticamente o plantel de cada equipa
     * @param TeamHome equipa da casa
     * @param strategyHome estrategia da equipa da casa
     * @param TeamAway equipa visitante
     * @param strategyAway estrategia da equipa visitante
     */
    public FootballGame(FootballTeam TeamHome, int strategyHome, FootballTeam TeamAway, int strategyAway){
            super(true,0,0, LocalDate.now(),0,TeamHome,TeamAway);
            setHome(new FootballLineup(TeamHome,strategyHome));
            setAway(new FootballLineup(TeamAway,strategyAway));
    }

    /**
     * Setter do plantel da casa
     * @param home plantel a copiar
     */
    public void setHome(FootballLineup home) {
        this.home = new FootballLineup(home);
    }

    /**
     * Getter do plantel da casa
     * @return plantel da casa
     */
    public FootballLineup getHome() {
        return new FootballLineup(this.home);
    }

    /**
     * Setter do plantel visitante
     * @param away plantel a copiar
     */
    public void setAway(FootballLineup away) {
        this.away = new FootballLineup(away);
    }

    /**
     * Getter do plantel visitante
     * @return
     */
    public FootballLineup getAway() {
        return new FootballLineup(this.away);
    }

    /**
     * Equals da classe
     * @param o objeto a comparar
     * @return booleano que indica resultado da comparacao
     */
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || !this.getClass().equals(o.getClass()))
            return false;

        FootballGame g = (FootballGame) o;

        return this.home.equals(g.getHome()) &&
                this.away.equals(g.getAway()) &&
                super.equals(o);
    }

    @Override
    /**
     * Cloner de FootballGame
     */
    public FootballGame clone() {
        return new FootballGame(this);
    }

    /**
     * Torna os dados de FootballGame em uma string
     * @return FootballGame em forma de string
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Game \n").append(getPoints1()).append("-").append(getPoints2())
                .append("\nHome: ").append(home.toString())
                .append("Away: ").append(away.toString());
        return sb.toString();
    }

    @Override
    /**
     * Metodo que torna FootballGame no formato de uma string gravavel
     */
    public String toCSV() {
        StringBuilder s = new StringBuilder(super.toCSV()).append(",");

        for(FootballPlayer p: this.getHome().getPlaying())
            s.append(p.getNumber()).append(",");
        for(Substitution sub: this.getHome().getSubstituitions())
            s.append(sub.toCSV()).append(",");
        for(FootballPlayer p: this.getAway().getPlaying())
            s.append(p.getNumber()).append(",");
        for(Substitution sub: this.getAway().getSubstituitions())
            s.append(sub.toCSV()).append(",");

        return "Jogo:" + s.append("\n");
    }

    /**
     * Metodo que carrega de uma string um FootballGame
     * @param csvLine string a ler
     * @param teams map de equipas para verificar se equipas na string sao validas
     * @return FootballGame carregado
     */
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
