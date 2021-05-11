package model.football.team;
import model.football.player.FootballPlayer;
import model.generic.team.Team;

import java.util.*;
import java.util.stream.Collectors;

public class FootballTeam extends Team  {
    private Map<String, FootballPlayer> players;
    private int Nplayers;

    public FootballTeam(){
        super();
        players = new HashMap<>();
        Nplayers = 0;
    }

    public FootballTeam(String name, int GlobalSkill, Map<String, FootballPlayer> newPlayers, int newNplayers){
        super(name,GlobalSkill);
        setPlayers(newPlayers);
        Nplayers = newNplayers;
    }

    public FootballTeam(FootballTeam newTeam){
        super(newTeam);
        players = newTeam.getPlayers();
        Nplayers = newTeam.getNPlayers();
    }

    public Map<String, FootballPlayer> getPlayers() {
        return players.entrySet().stream().collect(Collectors.toMap(e-> e.getKey(), e->e.getValue().clone()));
    }

    public void setPlayers(Map<String, FootballPlayer> newPlayers){
        players = newPlayers.entrySet().stream().collect(Collectors.toMap(e-> e.getKey(), e->e.getValue().clone()));
    }

    public int getNPlayers(){
        return Nplayers;
    }

    public void setNplayers(int n){
        Nplayers = n;
    }

    public int calcAverageSkill(){
        return (int) players.values().stream().map(FootballPlayer::calcOverallSkill).count();
    }

    public FootballPlayer getPlayer(String name){
        return players.get(name).clone();
    }

    public boolean existsPlayer(String name){
        return players.containsKey(name);
    }

    public void addPlayer(FootballPlayer p){
        if(!players.containsKey(p.getName())) {
            players.put(p.getName(), p.clone());
            p.setCurTeam(getName());
            p.addBackground(getName());
            incTeamPlayers();
        }
    }

    public void removePlayer(String name){
        if(players.containsKey(name)) {
            FootballPlayer p = players.get(name);
            p.setCurTeam("None");
            players.remove(name);
            decTeamPlayers();
        }
    }

    public void incTeamPlayers(){
        Nplayers++;
    }

    public void decTeamPlayers(){
        Nplayers--;
    }


    public FootballPlayer getTypePlayer(Class<?> t,Set<FootballPlayer> Playinglist,Set<FootballPlayer> Substituteslist){
        if(getNPlayers() != 0) {
            Optional<FootballPlayer> p_type = players.values().stream()
                    .filter(p ->p.getClass() == t && (!Playinglist.contains(p)) && !Substituteslist.contains(p))
                    .findFirst();
            if (p_type.isPresent()) return p_type.get().clone();
        }
        return null;
    }

    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FootballTeam team = (FootballTeam) o;

        return getName().equals(team.getName())
                && Nplayers == team.getNPlayers()
                && players.equals(team.getPlayers());
    }



    @Override
    public FootballTeam clone(){
        return new FootballTeam(this);
    }


    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append(getName()).append("\nNumber of players: ")
                .append(getNPlayers())
                .append("\nPlayers: ");
        players.values().forEach(p->sb.append(p.getName()).append(", "));
        sb.append("\n");
        return sb.toString();
    }

    public String toCSV() {
        return super.toCSV() + ';' + this.getNPlayers();
    }

    public static FootballTeam fromCSV(String line) {
        String[] splitLine = line.split(";");

        return new FootballTeam
                (
                        splitLine[0],
                        Integer.parseInt(splitLine[1]),
                        new HashMap<>(),
                        Integer.parseInt(splitLine[2])
                );
    }
}
