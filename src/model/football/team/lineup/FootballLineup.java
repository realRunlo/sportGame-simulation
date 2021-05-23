package model.football.team.lineup;

import model.football.player.*;
import model.football.team.FootballTeam;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class FootballLineup implements Serializable {
    private String teamName;
    private int strategy;
    private int globalSkill;
    private Set<FootballPlayer> playing;
    private Set<FootballPlayer> substitutes;

    public FootballLineup() {
        setTeamName("None");
        setStrategy(1);
        setGlobalSkill(0);
        setPlaying(new HashSet<>());
        setSubstitutes(new HashSet<>());
    }

    public FootballLineup(String name,int s,Set<FootballPlayer> playing, Set<FootballPlayer> substitutes) {
        setTeamName(name);
        setStrategy(s);
        setPlaying(playing);
        setSubstitutes(substitutes);
        setGlobalSkill(calcGlobalSkill());
    }

    public FootballLineup(FootballLineup lineup) {
        this.setTeamName(lineup.getTeamName());
        this.setStrategy(lineup.getStrategy());
        this.setPlaying(lineup.getPlaying());
        this.setSubstitutes(lineup.getSubstitutes());
        this.setGlobalSkill(calcGlobalSkill());
    }

    public FootballLineup(FootballTeam t,int s){
        setTeamName(t.getName());
        setStrategy(s);
        setPlaying(new HashSet<>());
        setSubstitutes(new HashSet<>());
        //Adicionar os titulares
        addPlaying(t.getTypePlayer(Goalkeeper.class,playing,substitutes));
        addPlayerTypeToGroup(t,Defender.class,true);
        addPlayerTypeToGroup(t, Lateral.class,true);
        addPlayerTypeToGroup(t, Midfielder.class,true);
        addPlayerTypeToGroup(t, Striker.class,true);


        //Adicionar os substitutos
        addSubstitute(t.getTypePlayer(Goalkeeper.class,playing,substitutes));
        addPlayerTypeToGroup(t,Defender.class,false);
        addPlayerTypeToGroup(t,Lateral.class,false);
        addPlayerTypeToGroup(t,Midfielder.class,false);
        addPlayerTypeToGroup(t,Striker.class,false);

        setGlobalSkill(calcGlobalSkill());
    }

    public String getTeamName(){
        return teamName;
    }

    public void setTeamName(String name){
        teamName = name;
    }

    public int getStrategy(){
        return strategy;
    }

    public void setStrategy(int s){
        strategy = strategyNormalize(s);
    }

    private void setGlobalSkill(int globalSkill) {
        this.globalSkill = globalSkill;
    }
    public int getGlobalSkill() {
        return globalSkill;
    }

   private void setPlaying(Set<FootballPlayer> playing) {
        this.playing = new HashSet<>(playing);
    }
    public Set<FootballPlayer> getPlaying() {
        return new HashSet<>(playing);
    }

    private void setSubstitutes(Set<FootballPlayer> substitutes) {
        this.substitutes = new HashSet<>(substitutes);
    }
    public Set<FootballPlayer> getSubstitutes() {
        return new HashSet<>(this.substitutes);
    }

    public int calcGlobalSkill() {
        int sum = 0;
        int numPlayers = 0;

        Set<FootballPlayer> playing = getPlaying();
        Set<FootballPlayer> substitutes = getSubstitutes();

        numPlayers = playing.size() + substitutes.size();

        for(FootballPlayer player: playing)
            if(player != null) sum += player.getOverallSkill();
        for(FootballPlayer player: substitutes)
            if(player!=null) sum += player.getOverallSkill();

        if(numPlayers > 0)
            sum /= numPlayers;

        return sum;
    }

    public FootballPlayer getPlayer(Class<?> player,boolean play){
        FootballPlayer p;
        if(player != null) {
            if (play) {
                p = getPlaying().stream().filter(e -> e.getClass().equals(player)).findAny().get();
            } else {
                p = getSubstitutes().stream().filter(e -> e.getClass().equals(player)).findAny().get();
            }
            return p;
        }
        else return null;
    }

    public void remPlaying(FootballPlayer player) {
        Set<FootballPlayer> playing = this.getPlaying();

        playing.remove(player);

        this.setPlaying(playing);
    }

    public void addPlaying(FootballPlayer player) {
        Set<FootballPlayer> playing = getPlaying();

        if(playing.size() < 11) {
            playing.add(player);
            setPlaying(playing);
        }
    }

    public void addPlayerTypeToGroup(FootballTeam t,Class<?> playerType, boolean playing){
        int nToAdd = 0;
        if(playing) {
            nToAdd = numberOfType(playerType);
            for(int i =0; i<nToAdd;i++) addPlaying(t.getTypePlayer(playerType,getPlaying(),getSubstitutes()));
        }
        else{
            nToAdd = 2;
            for(int i =0; i<nToAdd;i++) addSubstitute(t.getTypePlayer(playerType,getPlaying(),getSubstitutes()));
        }


    }


    public void remSubstitute(FootballPlayer player) {
        Set<FootballPlayer> subs = this.getSubstitutes();

        subs.remove(player);

        this.setSubstitutes(subs);
        this.setGlobalSkill(this.calcGlobalSkill());
    }

    public void addSubstitute(FootballPlayer player) {
        Set<FootballPlayer> subs = getSubstitutes();

        subs.add(player);
        this.setSubstitutes(subs);
        this.setGlobalSkill(this.calcGlobalSkill());
    }

    public void substitutePlayer(FootballPlayer player, FootballPlayer sub) {
        remPlaying(player);
        remSubstitute(sub);

        addPlaying(sub);
        addSubstitute(player);
    }

    public int numberOfType(Class<?> player_type){
        if(getStrategy() == 1){ //4-4-2
            if (Defender.class.equals(player_type)) return 2;
            else if(Lateral.class.equals(player_type)) return 2;
            else if (Midfielder.class.equals(player_type)) return 4;
            else return 2;
        }
        else{ //4-3-3
            if (Defender.class.equals(player_type)) return 2;
            else if(Lateral.class.equals(player_type)) return 2;
            else if (Midfielder.class.equals(player_type)) return 3;
            else return 3;
        }
    }




    public int strategyNormalize(int strategy){
        if(strategy == 1) return 1;
        else if(strategy == 2) return 2;
            else return 1;
    }
    public boolean equals(FootballLineup fl) {
        boolean bool = false;

        if(this == fl)
            bool = true;
        else if
        (
                this.getGlobalSkill() == fl.getGlobalSkill() &&
                this.getPlaying().equals(fl.getPlaying())    &&
                this.getSubstitutes().equals(fl.getSubstitutes())
        )
            bool = true;

        return bool;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(teamName).append("\n")
                .append("Playing: ");
        playing.forEach(p-> sb.append(p.getName()).append(", "));
        sb.append("\nSubstitutes: ");
        substitutes.forEach(p-> sb.append(p.getName()).append(", "));
        sb.append("\n");
        return sb.toString();
    }
}
