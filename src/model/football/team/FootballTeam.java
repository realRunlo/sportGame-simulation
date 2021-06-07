package model.football.team;

import model.football.player.*;
import model.generic.team.Team;
import model.interfaces.Saveable;

import java.util.*;
import java.util.stream.Collectors;

public class FootballTeam extends Team implements Saveable {
    private Map<Integer, FootballPlayer> players;
    private int AverageOverlSkill;
    private int Nplayers;
    private static final int NumberOfPlayers = 23;

    public FootballTeam(){
        super();
        players = new HashMap<>();
        AverageOverlSkill = 0;
        Nplayers = 0;
    }

    public FootballTeam(String name) {
        super(name);
        this.setPlayers(new HashMap<>());
        this.setAverageOverlSkill(0);
        this.setNplayers(0);
        this.setNplayers(0);
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
        if(getNPlayers() > 0)
        return (int) players.values().stream().mapToInt(FootballPlayer::calcOverallSkill).sum() / getNPlayers();
        else return 0;
    }

    public FootballPlayer getPlayer(int shirtNumber){
        FootballPlayer p = null;
        if(players.containsKey(shirtNumber))
            p=players.get(shirtNumber).clone();
        return p;
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
        if(getNPlayers() < NumberOfPlayers && p!=null) {
            if(!existsPlayerNumber(p.getName(),p.getNumber())) {
                if (!p.getCurTeam().equals(this.getName())) {
                    p.setCurTeam(getName());
                    p.addBackground(getName());
                }
                players.put(p.getNumber(), p.clone());
                incTeamPlayers();
            }
        }
    }

    public void removePlayer(String name, int shirtNumber){
        if(getNPlayers() > 0) {
            if (existsPlayerNumber(name, shirtNumber)) {
                FootballPlayer p = players.get(shirtNumber);
                p.setCurTeamNone();
                players.remove(shirtNumber);
                decTeamPlayers();
            }
        }
    }

    public void removePlayer(int shirtNumber){
        if(getNPlayers() > 0) {
            if (existsShirtNumber(shirtNumber)) {
                FootballPlayer p = players.get(shirtNumber);
                p.setCurTeamNone();
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
                    .filter(p ->p.getClass() == t && (Playinglist.stream().noneMatch(k ->  k.getNumber() == (p.getNumber())))
                            && Substituteslist.stream().noneMatch(k -> k.getNumber() == (p.getNumber())))
                    .findFirst();
            if (p_type.isPresent()) return p_type.get().clone();
        }
        return null;
    }

    public void updatePlayer(FootballPlayer p){
        if(this.players.values().stream().anyMatch(e->e.getName().equals(p.getName()) && e.getNumber() == p.getNumber()))
            players.replace(p.getNumber(),this.players.values().stream().filter(e->e.getName().equals(p.getName()) && e.getNumber() == p.getNumber()).findFirst().get(),p);

    }


    public String printPlayers(){
        StringBuilder sb = new StringBuilder();
        sb.append("Available Players ('Name','Shirt','OverallSkill'):\n");
        sb.append("Goalkeepers: "); players.values()
                .forEach(k->{
                    if(k instanceof Goalkeeper)
                        sb.append(k.getName()).append(" , ").append(k.getNumber()).append(" , ").append(k.calcOverallSkill()).append("/ ");
                });
        sb.append("\nDefenders: "); players.values()
                .forEach(k->{
                    if(k instanceof Defender)
                        sb.append(k.getName()).append(" , ").append(k.getNumber()).append(" , ").append(k.calcOverallSkill()).append("/ ");
                });
        sb.append("\nLateral: "); players.values()
                .forEach(k->{
                    if(k instanceof Lateral)
                        sb.append(k.getName()).append(" , ").append(k.getNumber()).append(" , ").append(k.calcOverallSkill()).append("/ ");
                });
        sb.append("\nMidfielders: "); players.values()
                .forEach(k->{
                    if(k instanceof Midfielder)
                        sb.append(k.getName()).append(" , ").append(k.getNumber()).append(" , ").append(k.calcOverallSkill()).append("/ ");
                });
        sb.append("\nStrikers: "); players.values()
                .forEach(k->{
                    if(k instanceof Striker)
                        sb.append(k.getName()).append(" , ").append(k.getNumber()).append(" , ").append(k.calcOverallSkill()).append("/ ");
                });
        return sb.toString();
    }

    public boolean equals(FootballTeam ft){
        if (this == ft) return true;

        return this.getName().equals(ft.getName())
                && this.getAverageOverlSkill() == ft.getAverageOverlSkill()
                && this.getNPlayers() == ft.getNPlayers()
                && this.getPlayers().equals(ft.getPlayers());
    }

    public int typeNeeded(){
        if(Nplayers >= 22) return -1;
        if(players.values().stream().filter(k->k instanceof Goalkeeper).count() < 2) return 0;
        else if(players.values().stream().filter(k->k instanceof Defender).count() < 4) return 1;
        else if(players.values().stream().filter(k->k instanceof Lateral).count() < 4) return 2;
        else if(players.values().stream().filter(k->k instanceof Midfielder).count() < 6) return 3;
        else return 4;
    }



    @Override
    public FootballTeam clone(){
        return new FootballTeam(this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append("\nNumber of players: ")
                .append(getNPlayers()).append("\n");
        sb.append(printPlayers());
        sb.append("\nOverall Skill: ").append(calcAverageSkill()).append("\n");
        return sb.toString();
    }

    public String toCSV() {
        return "Equipa: " + super.toCSV() + "\n";
    }

    public static FootballTeam load(String csvLine) {
        return new FootballTeam(csvLine);
    }
}
