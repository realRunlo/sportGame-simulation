package football.team.lineup;

import football.player.FootballPlayer;

import java.util.HashSet;
import java.util.Set;

public class FootballLineup {
    private int globalSkill;
    Set<FootballPlayer> playing;
    Set<FootballPlayer> substitutes;

    public FootballLineup() {
        globalSkill = 0;
        this.playing = new HashSet<>();
        this.substitutes = new HashSet<>();
    }

    public FootballLineup(Set<FootballPlayer> playing, Set<FootballPlayer> substitutes) {
        this.setPlaying(playing);
        this.substitutes = new HashSet<>(substitutes);
        this.setGlobalSkill(calcGlobalSkill());
    }

    public FootballLineup(FootballLineup lineup) {
        this.setPlaying(lineup.getPlaying());
        this.setSubstitutes(lineup.getSubstitutes());
        this.setGlobalSkill(calcGlobalSkill());
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

        return sum / numPlayers;
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
    }

    public void addSubstitute(FootballPlayer player) {
        Set<FootballPlayer> subs = getSubstitutes();

        subs.add(player);
        this.setSubstitutes(subs);
    }

    public void substitutePlayer(FootballPlayer player, FootballPlayer sub) {
        remPlaying(player);
        remSubstitute(sub);

        addPlaying(sub);
        addSubstitute(player);
    }
}
