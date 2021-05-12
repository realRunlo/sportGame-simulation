package football.Game;

import football.player.Defender;
import football.player.FootballPlayer;
import football.player.Midfielder;
import football.player.Striker;
import football.team.FootballTeam;

import java.util.Random;
import java.util.Set;

public class ExecuteFootballGame {
    private FootballGame g;
    private FootballPlayer playerWithTheBall; //jogador com a bola neste momento
    private boolean home;

    public ExecuteFootballGame(){
        Random rand = new Random();
        g = new FootballGame();
        int random = rand.nextInt(2);
        if(random == 0){
            playerWithTheBall = g.getAway().getPlaying().stream()
                    .filter(e-> e.getClass().equals(Striker.class))
                    .findAny().get();
            home = false;
        }
        else {
            playerWithTheBall = g.getHome().getPlaying().stream()
                    .filter(e-> e.getClass().equals(Striker.class))
                    .findAny().get();
            home = true;
        }
    }

    public ExecuteFootballGame(FootballGame g){
        g = g.clone();
    }

    public void ExecutePlay(){
        Set<FootballPlayer> playingHome = g.getHome().getPlaying();
        Set<FootballPlayer> playingAway = g.getAway().getPlaying();
        if(playerWithTheBall.getClass().equals(Defender.class)){


        }
    }
}
