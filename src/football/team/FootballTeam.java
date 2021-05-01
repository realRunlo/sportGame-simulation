package football.team;
import football.player.FootballPlayer;
import generic.team.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FootballTeam extends Team{
    private Map<String, FootballPlayer> players;
    private int AverageOverlSkill;

    public FootballTeam(){
        super();
        players = new HashMap<>();
        AverageOverlSkill = 0;
    }

    public FootballTeam(String name, int GlobalSkill, Map<String, FootballPlayer> newPlayers, int newAverage){
        super(name,GlobalSkill);
        setPlayers(newPlayers);
        setAverageOverlSkill(newAverage);
    }

    public FootballTeam(FootballTeam newTeam){
        super(newTeam);
        players = newTeam.getPlayers();
        AverageOverlSkill = newTeam.getAverageOverlSkill();
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
        if(!players.containsKey(p.getName())){ players.put(p.getName(),p.clone());
        }
    }

    public void removePlayer(String nome){
        players.remove(nome);
    }


    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FootballTeam team = (FootballTeam) o;

        return getName().equals(team.getName())
                && getAverageOverlSkill() == team.getAverageOverlSkill()
                && players.equals(team.getPlayers());
    }


    @Override
    public FootballTeam clone(){
        return new FootballTeam(this);
    }
}
