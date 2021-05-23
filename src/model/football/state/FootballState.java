package model.football.state;
import model.football.game.FootballGame;
import model.football.player.FootballPlayer;
import model.football.team.FootballTeam;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



public class FootballState implements Serializable {
    private Map<String, FootballPlayer> playersList;
    private Map<String, FootballTeam> teams;
    private List<FootballGame> gameHistory;
    private int numbPlayers;
    private int numbTeams;
    private int day;

    private static final int MAX_PLAYER_TEAM = 22;
    public FootballState(){
        setPlayersList(new HashMap<>());
        setTeams(new HashMap<>());
        setGameHistory(new ArrayList<>());
        setNumbPlayers(0);
        setNTeams(0);
        setDay(0);
    }
    public FootballState(Map<String, FootballPlayer> newList, Map<String, FootballTeam> newTeams, int newPlayers, int newNumbTeams, int newDay){
        setPlayersList(newList);
        setTeams(newTeams);
        setGameHistory(new ArrayList<>());
        setNumbPlayers(newPlayers);
        setNTeams(newNumbTeams);
        setDay(newDay);
    }

    public FootballState(FootballState s){
        setPlayersList(s.getPlayersList());
        setTeams(s.getTeams());
        setGameHistory(s.getGameHistory());
        setNumbPlayers(s.getNPlayers());
        setNTeams(s.getNTeams());
        setDay(s.getDay());
    }

    public Map<String, FootballPlayer> getPlayersList(){
        return playersList.entrySet().stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue().clone()));
    }

    public void setPlayersList(Map<String, FootballPlayer> newList){
        playersList = newList.entrySet().stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue().clone()));
        setNumbPlayers(playersList.size());
    }

    public Map<String, FootballTeam> getTeams(){
        return teams.entrySet().stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue().clone()));
    }

    public FootballTeam getTeam(String teamName){
        return getTeams().get(teamName).clone();
    }

    public void setTeams(Map<String, FootballTeam> newTeams){
        teams = newTeams.entrySet().stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue().clone()));
        setNTeams(teams.size());
    }

    public List<FootballGame> getGameHistory(){
        return new ArrayList<>(gameHistory);
    }

    public void setGameHistory(List<FootballGame> newHistory){
        gameHistory = new ArrayList<>(newHistory);
    }

    public int getNPlayers(){
        return numbPlayers;
    }

    public void setNumbPlayers(int nP){
        numbPlayers = nP;
    }

    public int getNTeams(){
        return numbTeams;
    }

    public void setNTeams(int nTeams){
        numbTeams = nTeams;
    }

    public int getDay(){
        return day;
    }

    public void setDay(int nDay){
        day = nDay;
    }

    public void addGame(FootballGame g){
        List<FootballGame> gHistory = getGameHistory();
        gHistory.add(g.clone());
        setGameHistory(gHistory);
    }

    public void removeIndexGame(int i){
        List<FootballGame> gHistory = getGameHistory();
        gHistory.remove(Math.min(i, gHistory.size() - 1));
        setGameHistory(gHistory);
    }


    public void addPlayer(FootballPlayer p){
        Map<String,FootballPlayer> pList = getPlayersList();
        //caso nao exista jogador com o mesmo nome adiciona imediatamente
        if(!pList.containsKey(p.getName())){
            pList.put(p.getName(),p.clone());
            setPlayersList(pList);
        }
        else{//caso seja um jogador com o mesmo nome mas stats diferentes, adiciona
            if(!pList.get(p.getName()).equals(p)){
                pList.put(p.getName(),p.clone());
                setPlayersList(pList);
            }
        }
    }


    public void removePlayer(String name,int shirt, String team){
        Map<String,FootballPlayer> pList = getPlayersList();
        if(pList.containsKey(name)){
            FootballPlayer p = pList.values().stream().filter(e-> e.getName().equals(name)
                    && e.getNumber() == shirt
                    && e.getCurTeam().equals(team)).findFirst().get();
            if(!p.getCurTeam().equals("None")) { //antes de remover o jogador do jogo, remove-o da equipa
                FootballTeam t = getTeam(p.getCurTeam());
                t.removePlayer(p.getName(),p.getNumber());
                addTeam(t);
            }
            pList.remove(name);
            setPlayersList(pList);
        }
    }

    public void addTeam(FootballTeam t){
        Map<String,FootballTeam> tList = getTeams();
        Map<String,FootballPlayer> pList = getPlayersList();
        tList.put(t.getName(), t.clone());
        setTeams(tList);
        t.getPlayers().forEach((k,v)->{    //caso a nova equipa introduza jogadores que ainda nao existiam,
        if(!pList.containsKey(v.getName())) addPlayer(v);}); //adiciona-os ao estado
        setPlayersList(pList);

    }

    public void removeTeam(String team){
        Map<String,FootballTeam> tList = getTeams();
        Map<String,FootballPlayer> pList = getPlayersList();
        if(tList.containsKey(team)) {
            FootballTeam t = getTeam(team);
            if(t.getNPlayers() > 0){
                t.getPlayers().forEach((k,v)-> removePlayer(v.getName(),v.getNumber(),v.getCurTeam())); //caso a equipa ainda tenha jogadores,
            }                                                                                           //remove-os da equipa
            tList.remove(team);
            setTeams(tList);
        }
    }

    //remove apenas a equipa, nao removendo os jogadores
    public void removeOnlyTeam(String team){
        Map<String,FootballTeam> tList = getTeams();
        if(tList.containsKey(team)) {
            FootballTeam t = getTeam(team);
            if(t.getNPlayers() > 0) {
                t.getPlayers().forEach((k, v) -> removePlayerFromTeam(v)); //caso a equipa ainda tenha jogadores,
            }                                                                                 //remove-os da equipa
            tList.remove(team);
            setTeams(tList);
        }
    }

    public void incNPlayers(){
        numbPlayers++;
    }

    public void decNPlayers(){
        numbPlayers--;
    }

    public void incNTeams(){
        numbTeams++;
    }

    public void decNTeams(){
        numbTeams--;
    }

    public boolean existsPlayer(FootballPlayer p){
        Map<String,FootballPlayer> pList = getPlayersList();
        for(FootballPlayer player: pList.values()){
            if(p.equals(player)) return true;
        }
        return false;
    }

    public void addPlayer2Team(FootballPlayer p ,String team){
        if(teams.containsKey(team)){
            FootballTeam t = teams.get(team);
            if(!t.existsPlayerNumber(p.getName(),p.getNumber()) && t.getNPlayers() < MAX_PLAYER_TEAM){
                t.addPlayer(p);
            }
        }
    }

    public void removePlayerFromTeam(FootballPlayer p){
        if(getTeams().containsKey(p.getCurTeam())){
            FootballTeam t = getTeam(p.getCurTeam());
            if(existsPlayer(p)){
                if (t.existsPlayerNumber(p.getName(),p.getNumber())) {
                    t.removePlayer(p.getName(),p.getNumber());
                }
                p.setCurTeamNone();
                addTeam(t);
                addPlayer(p);
            }
        }
    }

    public void createGame(String team1, String team2){
        Map<String,FootballTeam> tList = getTeams();
        //Se as equipas existirem e tiverem jogadores suficientes e adicionado um novo jogo
        if(tList.containsKey(team1) && tList.containsKey(team2)){
            FootballTeam t1 = getTeam(team1);
            FootballTeam t2 = getTeam(team2);
            if(t1.getNPlayers() == MAX_PLAYER_TEAM && t2.getNPlayers() == MAX_PLAYER_TEAM){
                FootballGame g = new FootballGame(t1,1,t2,1);
                addGame(g);
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




    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FootballState s = (FootballState) o;

        return playersList.equals(s.getPlayersList()) &&
                teams.equals(s.getTeams()) &&
                numbPlayers == s.getNPlayers() &&
                numbTeams == s.getNTeams() &&
                day == s.getDay();
    }

    @Override
    public FootballState clone(){
        return new FootballState(this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Estado\n").append("Numero de jogadores: ").append(getNPlayers())
                .append("\nNumero de equipas: ").append(getNTeams())
                .append("\nDias: ").append(getDay());

        return sb.toString();
    }
}
