package model.football.state;
import model.football.game.FootballGame;
import model.football.player.*;
import model.football.team.FootballTeam;
import model.generic.player.Player;
import model.interfaces.Saveable;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;



public class FootballState implements Saveable,Serializable{
    private Map<String, FootballPlayer> playersList; //a key sera um concat do nome do jogador com a sua camisola
    private Map<String, FootballTeam> teams;
    private List<FootballGame> gameHistory;
    private int numbPlayers;
    private int numbTeams;
    private int day;

    private static final int MAX_PLAYER_TEAM = 23;
    public FootballState(){
        setPlayersList(new HashMap<>());
        setTeams(new HashMap<>());
        setGameHistory(new ArrayList<>());
        setNumbPlayers(0);
        setNTeams(0);
        setDay(0);
    }

    public FootballState(int day) {
        this.setPlayersList(new HashMap<>());
        this.setTeams(new HashMap<>());
        this.setGameHistory(new ArrayList<>());
        this.setNumbPlayers(0);
        this.setNTeams(0);
        this.setDay(day);
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

    public FootballPlayer getPlayer(String name, Integer shirt){
        if(playersList.containsKey(name+shirt)){
            return playersList.get(name+shirt).clone();
        }
        return null;
    }

    public void setDay(int nDay){
        day = nDay;
    }

    public void addGame(FootballGame g){
        List<FootballGame> gHistory = getGameHistory();
        gHistory.add(g.clone());
        setGameHistory(gHistory);
        day++;
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
            pList.put(p.getName()+p.getNumber(),p.clone());
            setPlayersList(pList);
            addPlayer2Team(p, p.getCurTeam());
        }
        else{//caso seja um jogador com o mesmo nome mas diferente numero de camisola pode adicionar
            if(!(pList.get(p.getName()).getNumber() == p.getNumber())){
                pList.put(p.getName()+p.getName(),p.clone());
                setPlayersList(pList);
                addPlayer2Team(p, p.getCurTeam());
            }
        }
    }
    public void addPlayer2Team(FootballPlayer p ,String team){
        if(teams.containsKey(team) && p!= null){
            FootballTeam t = getTeam(team);
            if(!t.existsPlayerNumber(p.getName(),p.getNumber()) && t.getNPlayers() < MAX_PLAYER_TEAM){
                //mudar o numero da camisola se necessario
                if(t.existsShirtNumber(p.getNumber())){
                    p.setNumber(findAvailableShirt(p.getName(),t));
                }
                t.addPlayer(p);
                teams.replace(team,t);
            }
        }
    }

    public int findAvailableShirt(String name, FootballTeam t){
        boolean available = false;
        int shirt = 0;
        while(!available){
            if(!existsPlayer(name,shirt) && !t.existsShirtNumber(shirt)) available = true;
            else shirt++;
        }
        return shirt;
    }

    public void addTeam(FootballTeam t){
        Map<String,FootballTeam> tList = getTeams();
        Map<String,FootballPlayer> pList = getPlayersList();
        tList.put(t.getName(), t.clone());
        setTeams(tList);
        t.getPlayers().forEach((k,v)-> updatePlayer(v)); //adiciona-os ao estadoF
    }

    public void updateTeam(FootballTeam team) {
        team.getPlayers().forEach((e,k)-> updatePlayer(k));
        this.teams.replace(team.getName(), team);
        playersList.values().stream()
                .filter(k->k.getCurTeam().equals(team.getName()) && !team.existsPlayerNumber(k.getName(),k.getNumber()))
                .forEach(Player::setCurTeamNone);
    }

    public void updatePlayer(FootballPlayer player) {
        //da update do jogador na lista de jogadores do estado
        if (this.playersList.containsKey(player.getName()+player.getNumber())) {
            playersList.replace(player.getName()+player.getNumber(), player);
            //da update, caso necessario, do jogador na propria equipa
            if(teams.containsKey(player.getCurTeam()))
                if(teams.get(player.getCurTeam()).existsPlayerNumber(player.getName(),player.getNumber())) {
                    teams.get(player.getCurTeam()).updatePlayer(player);
                }
        }
        //caso nao encontre o jogador no estado, adiciona-o
        else addPlayer(player);
    }


    public void removePlayer(String name,int shirt, String team){
        Map<String,FootballPlayer> pList = getPlayersList();
        if(pList.containsKey(name+shirt)){
            FootballPlayer p = pList.get(name+shirt);
            if(!p.getCurTeam().equals("None")) { //antes de remover o jogador do jogo, remove-o da equipa
                FootballTeam t = getTeam(p.getCurTeam());
                t.removePlayer(p.getName(),p.getNumber());
                updateTeam(t);
            }
            pList.remove(name+shirt);
            setPlayersList(pList);
        }
    }



    public void removeTeam(String team){
        Map<String,FootballTeam> tList = getTeams();
        Map<String,FootballPlayer> pList = getPlayersList();
        if(tList.containsKey(team)) {
            FootballTeam t = getTeam(team);
            if(t.getNPlayers() > 0){
                t.getPlayers().forEach((k,v)-> removePlayer(v.getName(),v.getNumber(),v.getCurTeam())); //caso a equipa ainda tenha jogadores,
            }                                                                                           //remove-os do estado
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

    public void removePlayerFromTeam(FootballPlayer p){
        if(getTeams().containsKey(p.getCurTeam())){
            FootballTeam t = getTeam(p.getCurTeam());
            if(existsPlayer(p)){
                if (t.existsPlayerNumber(p.getName(),p.getNumber())) {
                    t.removePlayer(p.getName(),p.getNumber());
                }
            updateTeam(t);
            p.setCurTeamNone();
            updatePlayer(p);
            }
        }
    }


    public void transferPlayer(FootballPlayer p, FootballTeam teamToTransfer){
        if(p!=null){
            removePlayerFromTeam(p);
            int originalShirt = p.getNumber();
            if(teamToTransfer != null){
                addPlayer2Team(p,teamToTransfer.getName());
            }
            updatePlayer(p);
            if(originalShirt != p.getNumber()) playersList.remove(p.getName()+originalShirt);
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

    public boolean existsPlayer(String name, Integer shirt){
        if(playersList.containsKey(name+shirt)){
            return playersList.get(name+shirt).getName().equals(name);
        }
        return false;
    }

    public boolean existsTeam(String name){
        return teams.containsKey(name);
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

    public String printPlayers(){
        StringBuilder sb = new StringBuilder();
        AtomicInteger changeLine = new AtomicInteger(0);
        sb.append("Available Players:\n");
        playersList.values().forEach(k-> {
            if(changeLine.get() == 5){
                sb.append("\n");
                changeLine.set(0);
            }
            else changeLine.set(changeLine.get()+1);
            sb.append(k.getName())
                    .append(" - ")
                    .append(k.getNumber())
                    .append(", ");
        });
        return sb.toString();
    }

    public String printPlayersWithShirt(int shirt){
        StringBuilder sb = new StringBuilder();
        AtomicInteger changeLine = new AtomicInteger(0);
        sb.append("Available Players:\n");
        playersList.values().stream().filter(e->e.getNumber() == shirt)
                .forEach(k-> {
                    if(changeLine.get() == 5){
                        sb.append("\n");
                        changeLine.set(0);
                    }
                    else changeLine.set(changeLine.get()+1);
                    sb.append(k.getName())
                            .append(" - ")
                            .append(k.getNumber())
                            .append(", ");
                });
        return sb.toString();
    }


    public String printTeams(){
        StringBuilder sb = new StringBuilder();
        sb.append("Available Teams:\n");
        teams.forEach((e,k) ->
                sb.append(e)
                        .append(" - ")
                        .append(k.calcAverageSkill())
                        .append(", "));
        return sb.toString();
    }

    public String printGameHistory(){
        StringBuilder sb = new StringBuilder();
        sb.append("**********************************************\n")
                .append("\t\t\t\tGame History\n");
        gameHistory.forEach(k ->
                sb.append(k.toString()).append("\n"));
        sb.append("\n**********************************************\n");
        return sb.toString();
    }




    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FootballState s = (FootballState) o;

        return this.getPlayersList().equals(s.getPlayersList()) &&
                this.getTeams().equals(s.getTeams()) &&
                this.getGameHistory().equals(s.getGameHistory()) &&
                this.getNPlayers() == s.getNPlayers() &&
                this.getNTeams() == s.getNTeams() &&
                this.getDay() == s.getDay();
    }

    @Override
    public FootballState clone(){
        return new FootballState(this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n*************************************\n")
        .append("\t\t\t\tState\n")
                .append("Number of players: ").append(getNPlayers());
        sb.append("\n").append(printPlayers());

        sb.append("\nNumber of teams: ").append(getNTeams())
        .append("\n").append(printTeams())
        .append("\nDays: ").append(getDay())
        .append("\n*************************************\n");

        return sb.toString();
    }

    @Override
    public String toCSV() {
        return "FootballState: " + this.getDay() + "\n";
    }

    @Override
    public void save(String filePath) throws IOException {
        BufferedWriter br = new BufferedWriter(new FileWriter(filePath));

        br.write(this.toCSV());
        br.flush();
        br.close();

        for(FootballTeam ft: this.getTeams().values()) {
            ft.save(filePath);
        }
        for(FootballPlayer fp: this.getPlayersList().values()) {
            fp.save(filePath);
        }
        for(FootballGame fg: this.getGameHistory()) {
            fg.save(filePath);
        }
    }

    public static FootballState load(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        String[] split;
        FootballState fs;

        split = br.readLine().split(": ");
        fs = new FootballState(Integer.parseInt(split[1]));

        while((line = br.readLine()) != null) {
            split = line.split(": ");

            switch (split[0]) {
                case "Defender" -> {
                    Defender d = Defender.load(split[1]);
                    fs.addPlayer(d);
                    fs.addPlayer2Team(d, d.getCurTeam());
                }
                case "Lateral" -> {
                    Lateral l = Lateral.load(split[1]);
                    fs.addPlayer(l);
                    fs.addPlayer2Team(l, l.getCurTeam());
                }
                case "Midfielder" -> {
                    Midfielder m = Midfielder.load(split[1]);
                    fs.addPlayer(m);
                    fs.addPlayer2Team(m, m.getCurTeam());
                }
                case "Striker" -> {
                    Striker s = Striker.load(split[1]);
                    fs.addPlayer(s);
                    fs.addPlayer2Team(s, s.getCurTeam());
                }
                case "Goalkeeper" -> {
                    Goalkeeper g = Goalkeeper.load(split[1]);
                    fs.addPlayer(g);
                    fs.addPlayer2Team(g, g.getCurTeam());
                }
                case "FootballTeam" -> {
                    FootballTeam ft = FootballTeam.load(split[1]);
                    fs.addTeam(ft);
                }
                /*case "FootballGame" -> {
                    FootballGame fg = FootballGame.
                }*/
            }
        }

        return fs;
    }
}
