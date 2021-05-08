package football.team.lineup;

import football.player.*;
import football.team.FootballTeam;

import java.util.HashSet;
import java.util.Set;

public class FootballLineup {
    private int strategy;
    private int globalSkill;
    private Set<FootballPlayer> playing;
    private Set<FootballPlayer> substitutes;

    public FootballLineup() {
        strategy = 1;
        globalSkill = 0;
        this.playing = new HashSet<>();
        this.substitutes = new HashSet<>();
    }

    public FootballLineup(int s,Set<FootballPlayer> playing, Set<FootballPlayer> substitutes) {
        strategy = strategyNormalize(s);
        this.setPlaying(playing);
        this.substitutes = new HashSet<>(substitutes);
        this.setGlobalSkill(calcGlobalSkill());
    }

    public FootballLineup(FootballLineup lineup) {
        this.setStrategy(lineup.getStrategy());
        this.setPlaying(lineup.getPlaying());
        this.setSubstitutes(lineup.getSubstitutes());
        this.setGlobalSkill(calcGlobalSkill());
    }

    public FootballLineup(FootballTeam t,int s){
        setStrategy(s);
        addPlaying(t.getTypePlayer(Goalkeeper.class,playing,substitutes));
        int nDefenders = numberOfType(Defender.class);
        int nMidfilders = numberOfType(Midfielder.class);
        int nStrikers = numberOfType(Striker.class);
        for(int i =0; i<nDefenders;i++) addPlaying(t.getTypePlayer(Defender.class,playing,substitutes));
        for(int i =0; i<nMidfilders;i++) addPlaying(t.getTypePlayer(Midfielder.class,playing,substitutes));
        for(int i =0; i<nStrikers;i++) addPlaying(t.getTypePlayer(Striker.class,playing,substitutes));
        addSubstitute(t.getTypePlayer(Goalkeeper.class,playing,substitutes));
        for(int i =0; i<2;i++) addSubstitute(t.getTypePlayer(Defender.class,playing,substitutes));
        for(int i =0; i<2;i++) addSubstitute(t.getTypePlayer(Midfielder.class,playing,substitutes));
        for(int i =0; i<2;i++) addSubstitute(t.getTypePlayer(Striker.class,playing,substitutes));
        globalSkill = calcGlobalSkill();
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
            sum += player.getOverallSkill();
        for(FootballPlayer player: substitutes)
            sum += player.getOverallSkill();

        if(numPlayers > 0)
            sum /= numPlayers;

        return sum;
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
        if(strategy == 1){
            if (Defender.class.equals(player_type)) return 4;
            else if (Midfielder.class.equals(player_type)) return 4;
            else return 2;
        }
        else{
            if (Defender.class.equals(player_type)) return 4;
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
}
