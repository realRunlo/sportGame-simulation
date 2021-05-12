package football.Game;

import football.player.Defender;
import football.player.FootballPlayer;
import football.player.Midfielder;
import football.player.Striker;
import football.team.FootballTeam;
import football.team.lineup.FootballLineup;

import java.util.Random;
import java.util.Set;

public class ExecuteFootballGame {
    private FootballGame g;
    private FootballPlayer playerWithTheBall; //jogador com a bola neste momento
    private boolean home; //team with the ball

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
        FootballLineup playingHome = g.getHome();
        FootballLineup playingAway = g.getAway();
        if(playerWithTheBall.getClass().equals(Defender.class)){
            if(home){
                FootballPlayer striker = playingAway.getPlayer(Striker.class,true);
                if(striker != null){
                    if(speedCheck(playerWithTheBall,striker) < 10){
                        if(!tryStealBall(striker)){
                            tryPass();
                        }
                        else;
                    }
                    else{

                    }
                }
                else{

                }
            }
            else{

            }
        }
    }

    public int speedCheck(FootballPlayer p1, FootballPlayer p2){
        return Math.abs(p1.getSpeed() - p2.getSpeed());
    }


    public boolean tryStealBall(FootballPlayer p){
        Random rand = new Random();
        if(playerWithTheBall.getClass().equals(Defender.class)){//se for defesa tem maior chance de manter a bola
            Defender d = (Defender) playerWithTheBall;
            if(rand.nextInt(101) <= d.getBallRetention()) {
                g.incTimerBy(1);
                return false;
            }
            else {
                setPlayerWithTheBall(p);
                g.incTimerBy(1);
                return true;
            }
            }
        else if(p.getClass().equals(Midfielder.class)){ //melhorar, talvez dar 2 skill checks ao medio
            Midfielder m = (Midfielder) p;
            if(rand.nextInt(101) <= m.getBallRecovery()){
                g.incTimerBy(1);
                return true;
            }
            else{
                g.incTimerBy(1);
                return false;
            }
        }
        else{ //caso normal, verifica quem tem melhor destreza
            if(playerWithTheBall.getDexterity() > p.getDexterity()){
                g.incTimerBy(1);
                return false;
            }
            else{
                g.incTimerBy(1);
                return true;
            }
        }
    }

    public void setPlayerWithTheBall(FootballPlayer p){
        playerWithTheBall = p;
        if(home) home = false;
        else home = true;
    }

}


