import football.player.FootballPlayer;
import football.team.FootballTeam;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;



public class State {
    private Map<String, FootballPlayer> playersList;
    private Map<String, FootballTeam> teams;
    private int numbPlayers;
    private int numbTeams;
    private int day;
    private Game g;

    private static final int MAX_PLAYER_TEAM = 22;
    public State(){
        playersList = new HashMap<>();
        teams = new HashMap<>();
        numbPlayers = 0;
        numbTeams = 0;
        day = 0;
        g = new Game();
    }
    public State(Map<String, FootballPlayer> newList,Map<String, FootballTeam> newTeams,int newPlayers, int newNumbTeams, int newDay, Game newGame){
        setPlayersList(newList);
        setTeams(newTeams);
        numbPlayers = newPlayers;
        numbTeams = newNumbTeams;
        day = newDay;
        g = newGame.clone();
    }

    public State(State s){
        playersList = s.getPlayersList();
        teams = s.getTeams();
        numbPlayers = s.getNPlayers();
        numbTeams = s.getNTeams();
        day = s.getDay();
        g = s.getGame();
    }

    public Map<String, FootballPlayer> getPlayersList(){
        return playersList.entrySet().stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue().clone()));
    }

    public void setPlayersList(Map<String, FootballPlayer> newList){
        playersList = newList.entrySet().stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue().clone()));
    }

    public Map<String, FootballTeam> getTeams(){
        return teams.entrySet().stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue().clone()));
    }

    public void setTeams(Map<String, FootballTeam> newTeams){
        teams = newTeams.entrySet().stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue().clone()));
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

    public Game getGame(){
        return g.clone();
    }

    public void setGame(Game newG){
        g = new Game(newG);
    }

    public void addPlayer(FootballPlayer p){
        if(playersList.containsKey(p.getName())){
            playersList.put(p.getName(),p.clone());
            incNPlayers();
        }
    }


    public void removePlayer(String name){
        if(playersList.containsKey(name)){
            FootballPlayer p = playersList.get(name);
            if(!p.getCurTeam().equals("None")) {    //antes de remover o jogador do jogo, remove-o da equipa
                FootballTeam t = teams.get(p.getCurTeam());
                t.removePlayer(p.getName());
            }
            playersList.remove(name);
            decNPlayers();
        }
    }

    public void addTeam(FootballTeam t){
        if(!teams.containsKey(t.getName())) {
            teams.put(t.getName(), t.clone());
            incNTeams();
            t.getPlayers().forEach((k,v)->{                      //caso a nova equipa introduza jogadores que ainda nao existiam,
                if(!playersList.containsKey(k)) addPlayer(v);}); //adiciona-os ao estado
        }
    }

    public void removeTeam(String team){
        if(teams.containsKey(team)) {
            FootballTeam t = teams.get(team);
            if(t.getNPlayers() > 0){
                t.getPlayers().forEach((k,v)-> removePlayer(k)); //caso a equipa ainda tenha jogadores,
            }                                                    //remove-os da equipa
            teams.remove(team);
            decNTeams();
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

    public void addPlayer2Team(FootballPlayer p ,String team){
        if(teams.containsKey(team)){
            FootballTeam t = teams.get(team);
            if(!t.existsPlayer(p.getName()) && t.getNPlayers() < MAX_PLAYER_TEAM){
                t.addPlayer(p);
            }
        }
    }

    public void removePlayerFromTeam(String name, String team){
        if(teams.containsKey(team)){
            FootballTeam t = teams.get(team);
            if(t.existsPlayer(name)) {
                t.removePlayer(name);
            }
        }
    }

    public void createGame(String team1, String team2){
        if(teams.containsKey(team1) && teams.containsKey(team2)){
            FootballTeam t1 = teams.get(team1);
            FootballTeam t2 = teams.get(team2);
            if(t1.getNPlayers() >= 11 && t2.getNPlayers() >= 2){
                LocalDateTime date = LocalDateTime.now();
                Game g = new Game(true,0,0,date,t1,t2);
            }
        }
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State s = (State) o;

        return playersList.equals(s.getPlayersList()) &&
                teams.equals(s.getTeams()) &&
                numbPlayers == s.getNPlayers() &&
                numbTeams == s.getNTeams() &&
                day == s.getDay() &&
                g.equals(s.getGame());
    }

    @Override
    public State clone(){
        return new State(this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Estado\n").append("Numero de jogadores: ").append(getNPlayers())
                .append("\nNumero de equipas: ").append(getNTeams())
                .append("\nDias: ").append(getDay())
                .append("\n").append(g.toString());

        return sb.toString();
    }
}
