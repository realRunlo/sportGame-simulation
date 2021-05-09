package football.team;
import football.player.FootballPlayer;
import football.player.Goalkeeper;
import generic.player.Player;
import generic.team.Team;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class FootballTeam extends Team  {
    private Map<String, FootballPlayer> players;
    private int AverageOverlSkill;
    private int Nplayers;
    public FootballTeam(){
        super();
        players = new HashMap<>();
        AverageOverlSkill = 0;
        Nplayers = 0;
    }

    public FootballTeam(String name, int GlobalSkill, Map<String, FootballPlayer> newPlayers, int newAverage,int newNplayers){
        super(name,GlobalSkill);
        setPlayers(newPlayers);
        setAverageOverlSkill(newAverage);
        Nplayers = newNplayers;
    }

    public FootballTeam(FootballTeam newTeam){
        super(newTeam);
        players = newTeam.getPlayers();
        AverageOverlSkill = newTeam.getAverageOverlSkill();
        Nplayers = newTeam.getNPlayers();
    }

    public Map<String, FootballPlayer> getPlayers() {
        return players.entrySet().stream().collect(Collectors.toMap(e-> e.getKey(), e->e.getValue().clone()));
    }

    public void setPlayers(Map<String, FootballPlayer> newPlayers){
        players = newPlayers.entrySet().stream().collect(Collectors.toMap(e-> e.getKey(), e->e.getValue().clone()));
    }

    public int getAverageOverlSkill(){
        return AverageOverlSkill;
    }

    public void setAverageOverlSkill(int newAverage){
        AverageOverlSkill = newAverage;
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
                && getAverageOverlSkill() == team.getAverageOverlSkill()
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
}
