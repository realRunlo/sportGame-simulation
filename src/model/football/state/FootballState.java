package model.football.state;

import model.football.Game.FootballGame;
import model.football.player.FootballPlayer;
import model.football.team.FootballTeam;
import model.generic.state.State;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



public class FootballState extends State {
    private Map<Integer, FootballPlayer> playersList;
    private Map<String, FootballTeam> teams;
    private List<FootballGame> gameHistory;

    private static final int MAX_PLAYER_TEAM = 22;
    public FootballState(){
        super();
        this.setPlayersList(new HashMap<>());
        this.setPlayersList(new HashMap<>());
        this.setPlayersList(new HashMap<>());
    }

    public FootballState(int playerN, int teamN, int day) {
        super(playerN, teamN, day);
        this.setPlayersList(new HashMap<>());
        this.setTeams(new HashMap<>());
        this.setGameHistory(new ArrayList<>());
    }

    public FootballState(Map<Integer, FootballPlayer> newList, Map<String, FootballTeam> newTeams, int newPlayers, int newNumbTeams, int newDay){
        super(newPlayers, newNumbTeams, newDay);
        this.setPlayersList(newList);
        this.setTeams(newTeams);
        this.setGameHistory(new ArrayList<>());
    }

    public FootballState(Map<Integer, FootballPlayer> pList, Map<String, FootballTeam> tList, List<FootballGame> games, int playerN, int teamN, int day) {
        super(playerN, teamN, day);
        this.setPlayersList(pList);
        this.setTeams(tList);
        this.setGameHistory(games);
    }

    public FootballState(FootballState s){
        super(s);
        this.setPlayersList(s.getPlayersList());
        this.setTeams(s.getTeams());
        this.getGameHistory(s.getGameHistory());
    }

    public Map<Integer, FootballPlayer> getPlayersList(){
        return playersList.entrySet().stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue().clone()));
    }

    public void setPlayersList(Map<Integer, FootballPlayer> newList){
        playersList = newList.entrySet().stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue().clone()));
    }

    public Map<String, FootballTeam> getTeams(){
        return teams.entrySet().stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue().clone()));
    }

    public FootballTeam getTeam(String teamName){
        return teams.get(teamName).clone();
    }

    public void setTeams(Map<String, FootballTeam> newTeams){
        teams = newTeams.entrySet().stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue().clone()));
    }

    public List<FootballGame> getGameHistory(){
        return new ArrayList<>(gameHistory);
    }

    public void setGameHistory(List<FootballGame> newHistory){
        gameHistory = new ArrayList<>(newHistory);
    }
    
    public void addPlayer(FootballPlayer p){
        if(!playersList.containsKey(p.getName())){
            playersList.put(p.getNumber(),p.clone());
            incNPlayers();
        }
    }

    public void removePlayer(Integer number){
        if(playersList.containsKey(number)){
            FootballPlayer p = playersList.get(number);
            if(!p.getCurTeam().equals("None")) {    //antes de remover o jogador do jogo, remove-o da equipa
                FootballTeam t = teams.get(p.getCurTeam());
                t.removePlayer(p.getName());
            }
            playersList.remove(number);
            decNPlayers();
        }
    }

    public void addTeam(FootballTeam t){
        if(!this.getTeams().containsKey(t.getName())) {
            teams.put(t.getName(), t.clone());
            incNTeams();
            t.getPlayers().forEach((k,v)->{                      //caso a nova equipa introduza jogadores que ainda nao existiam,
                if(!playersList.containsKey(k)) addPlayer(v);}); //adiciona-os ao estado
        }
    }

    public void removeTeam(String team){
        if(this.getTeams().containsKey(team)) {
            FootballTeam t = this.getTeams().get(team);
            if(t.getNPlayers() > 0){
                t.getPlayers().forEach((k,v)-> removePlayer(k)); //caso a equipa ainda tenha jogadores,
            }                                                    //remove-os da equipa
            this.getTeams().remove(team);
            this.decNTeams();
        }
    }

    public void addPlayer2Team(FootballPlayer p ,String team){
        if(this.getTeams().containsKey(team)){
            FootballTeam t = this.getTeams().get(team);
            if(!t.existsPlayer(p.getName()) && t.getNPlayers() < MAX_PLAYER_TEAM){
                t.addPlayer(p);
            }
        }
    }

    public void removePlayerFromTeam(String name, String team){
        if(this.getTeams().containsKey(team)){
            FootballTeam t = this.getTeams().get(team);
            if(t.existsPlayer(name)) {
                t.removePlayer(name);
            }
        }
    }

    public void createGame(String team1, String team2){
        if(this.getTeams().containsKey(team1) && teams.containsKey(team2)){
            FootballTeam t1 = this.getTeams().get(team1);
            FootballTeam t2 = this.getTeams().get(team2);
            if(t1.getNPlayers() >= 11 && t2.getNPlayers() >= 2){
                LocalDateTime date = LocalDateTime.now();
            }
        }
    }

    public void saveState(String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.flush();
        oos.close();
    }

    public boolean equals(FootballState o) {
        if (this == o) return true;

        boolean bool =  this.getPlayersList().equals(o.getPlayersList()) &&
                this.getTeams().equals(o.getTeams()) &&
                this.getGameHistory().equals(o.getGameHistory()) &&
                this.getNumPlayers() == o.getNumPlayers() &&
                this.getNumTeams() == o.getNumTeams() &&
                this.getDay() == o.getDay();

        return bool;
    }

    @Override
    public FootballState clone(){
        return new FootballState(this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Estado\n").append("Numero de jogadores: ")
                .append("\nNumero de equipas: ")
                .append("\nDias: ");

        return sb.toString();
    }

    public String toCSV() {
        StringBuilder str = new StringBuilder("FootballState: ");

        str.append(super.toCSV()).append('\n');

        for(FootballTeam ft: this.getTeams().values()) {
            str.append(ft.toCSV()).append('\n');
        }
        for(FootballPlayer fp: this.getPlayersList().values()) {
            str.append(fp.toCSV()).append('\n');
        }
        for(FootballGame fg: this.getGameHistory()) {
            str.append(fg.toCSV()).append('\n');
        }

        return str.toString();
    }

    public static FootballState fromCSV(String[] tokens) {
        return new FootballState(0, 0, Integer.parseInt(tokens[0]));
    }
}
