package model.football.team;
import model.football.player.FootballPlayer;
import model.generic.team.Team;

import java.util.*;
import java.util.stream.Collectors;

public class FootballTeam extends Team  {
    private Map<Integer, FootballPlayer> players;
    private int AverageOverlSkill;
    private int Nplayers;
    private static final int NumberOfPlayers = 40;
    public FootballTeam(){
        super();
        players = new HashMap<>();
        AverageOverlSkill = 0;
        Nplayers = 0;
    }

    public FootballTeam(String name, int GlobalSkill, Map<Integer, FootballPlayer> newPlayers, int newAverage,int newNplayers){
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

    public Map<Integer, FootballPlayer> getPlayers() {
        return players.entrySet().stream().collect(Collectors.toMap(e-> e.getKey(), e->e.getValue().clone()));
    }

    public void setPlayers(Map<Integer, FootballPlayer> newPlayers){
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

    public FootballPlayer getPlayer(int shirtNumber){
        return players.get(shirtNumber).clone();
    }

    public boolean existsShirtNumber(int shirtNumber){
        return players.containsKey(shirtNumber);
    }

    public boolean existsPlayerNumber(String name, int shirtNumber){
        if(existsShirtNumber(shirtNumber)){
            return players.get(shirtNumber).getName().equals(name);
        }
        return false;
    }

    public void addPlayer(FootballPlayer p){
        if(getNPlayers() < NumberOfPlayers) {
            if (!players.containsKey(p.getNumber())) {
                p.setNumber(getNPlayers() + 1);
                p.setCurTeam(getName());
                p.addBackground(getName());
                players.put(p.getNumber(), p.clone());
                incTeamPlayers();
            }
        }
    }

    public void removePlayer(String name, int shirtNumber){
        if(getNPlayers() > 0) {
            if (existsPlayerNumber(name, shirtNumber)) {
                FootballPlayer p = players.get(shirtNumber);
                p.setCurTeam("None");
                players.remove(shirtNumber);
                decTeamPlayers();
            }
        }
    }

    public void removePlayer(int shirtNumber){
        if(getNPlayers() > 0) {
            if (existsShirtNumber(shirtNumber)) {
                FootballPlayer p = players.get(shirtNumber);
                p.setCurTeam("None");
                players.remove(shirtNumber);
                decTeamPlayers();
            }
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
