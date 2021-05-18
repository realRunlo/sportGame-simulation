package football.Game;

import football.player.*;
import football.team.lineup.FootballLineup;

import java.util.Random;

public class ExecuteFootballGame {
    private FootballGame g;
    private FootballPlayer playerWithTheBall; //jogador com a bola neste momento
    private boolean home; //team with the ball
    private static final double quickAction = 0.5;
    private static final double averageAction = 1;
    private static final double slowAction = 1.5;

/* Coloquei em comentario porque nao faz sentido criar um executeFootballGame com um game sem equipas
    entao talvez nao valha a pena dar a opcao de o fazer
    public ExecuteFootballGame(){
        Random rand = new Random();
        setGame(new FootballGame());
        int random = rand.nextInt(2);
        if(random == 0){
            setHome(false);
            setPlayerWithTheBall(g.getAway().getPlaying().stream()
                    .filter(e-> e.getClass().equals(Striker.class))
                    .findAny().get(),false);
        }
        else {
            setHome(true);
            setPlayerWithTheBall(g.getHome().getPlaying().stream()
                    .filter(e-> e.getClass().equals(Striker.class))
                    .findAny().get(),true);
        }
    }
*/
    public ExecuteFootballGame(FootballGame g){
        Random rand = new Random();
        setGame(g);
        int random = rand.nextInt(2);
        if(random == 0){
            setHome(false);
            setPlayerWithTheBall(g.getAway().getPlaying().stream()
                    .filter(e-> e.getClass().equals(Midfielder.class))
                    .findAny().get(),false);
        }
        else {
            setHome(true);
            setPlayerWithTheBall(g.getHome().getPlaying().stream()
                    .filter(e-> e.getClass().equals(Midfielder.class))
                    .findAny().get(),true);
        }
    }
    public FootballPlayer getPlayerWithTheBall(){
        return playerWithTheBall.clone();
    }

    public void setPlayerWithTheBall(FootballPlayer p, boolean home){
        playerWithTheBall = p;
        //caso o novo jogador seja da equipa que antes nao tinha posse da bola, inverte a posse
        if(home != getHome()) invertTeamWithBall();
    }

    public boolean getHome(){
        return home;
    }

    public void setHome(boolean homeOrAway){
        home = homeOrAway;
    }


    public FootballGame getGame(){
        return g.clone();
    }

    public void setGame(FootballGame g){
        this.g = g.clone();
    }

    public void ExecutePlay(){
        FootballGame g = getGame();
        FootballLineup playingHome = g.getHome();
        FootballLineup playingAway = g.getAway();
        FootballPlayer p;
        if(getHome()){
            p = playingAway.getPlayer(getFootballPlayerOppositeClass(),true);
            if(p != null) {
                if (playerWithTheBall.getClass().equals(Striker.class)) { // se for um avancado tenta rematar
                    FootballPlayer d = playingAway.getPlayer(Defender.class, true);
                    if(playerWithTheBall.speedCheck(d) > 10) {
                        if (tryShoot());
                        else {
                            playerWithTheBall = d;
                            invertTeamWithBall();
                        }
                    }
                    else{
                        if(!tryStealBall(p)){
                            if (tryShoot());
                            else {
                                playerWithTheBall = p;
                                invertTeamWithBall();
                            }
                        }
                        else{
                            playerWithTheBall = d;
                            invertTeamWithBall();
                        }
                    }
                } else { //outras classes tentam passar
                    if (playerWithTheBall.speedCheck(p) < 10) {
                        if (!tryStealBall(p)) tryPass();
                        else {
                            playerWithTheBall = p;
                            invertTeamWithBall();
                        }
                    } else tryPass();
                }
            }// caso o jogador nao tenha oponente, simplesmente executa a acao sem skillcheck
            else{
                if(playerWithTheBall.getClass().equals(Striker.class)) Shoot();
                else Pass();
            }
        }
        else{
            p = playingHome.getPlayer(getFootballPlayerOppositeClass(),true);
            if(p != null) {
                if (playerWithTheBall.getClass().equals(Striker.class)) { // se for um avancado tenta rematar
                    FootballPlayer d = playingHome.getPlayer(Defender.class, true);
                    if(playerWithTheBall.speedCheck(d) > 10) {
                        if (tryShoot());
                        else {
                            playerWithTheBall = d;
                            invertTeamWithBall();
                        }
                    }
                    else{
                        if(!tryStealBall(p)){
                            if (tryShoot());
                            else {
                                playerWithTheBall = p;
                                invertTeamWithBall();
                            }
                        }
                        else{
                            playerWithTheBall = d;
                            invertTeamWithBall();
                        }
                    }
                } else { //outras classes tentam passar
                    if (playerWithTheBall.speedCheck(p) < 10) {
                        if (!tryStealBall(p)) tryPass();
                        else {
                            playerWithTheBall = p;
                            invertTeamWithBall();
                        }
                    } else tryPass();
                }
            }// caso o jogador nao tenha oponente, simplesmente executa a acao sem skillcheck
            else{
                if(playerWithTheBall.getClass().equals(Striker.class)) Shoot();
                else Pass();
            }
        }
    }




    public boolean tryStealBall(FootballPlayer p){
        g.incTimerBy(quickAction);
        Random rand = new Random();
        if(playerWithTheBall.getClass().equals(Defender.class)){//se for defesa tem maior chance de manter a bola
            Defender d = (Defender) playerWithTheBall;
            if(rand.nextInt(101) <= d.getBallRetention()) return false;
            else {
                setPlayerWithTheBall(p,!getHome());
                return true;
            }
            }
        else if(p.getClass().equals(Midfielder.class)){ //melhorar, talvez dar 2 skill checks ao medio
            Midfielder m = (Midfielder) p;
            return rand.nextInt(101) <= m.getBallRecovery();
        }
        else{ //caso normal, verifica quem tem melhor destreza
            return playerWithTheBall.getDexterity() <= p.getDexterity();
        }
    }

    public void tryPass(){
        g.incTimerBy(quickAction);
        Random rand = new Random();
        if(rand.nextInt(101) <= playerWithTheBall.getPassing()){
            Pass();
        }
    }


    public boolean tryPass(FootballPlayer p){
        g.incTimerBy(quickAction);
        Random rand = new Random();
        if(rand.nextInt(101) <= playerWithTheBall.getPassing()){
            Pass();
            return true;
        }
        else {
            invertTeamWithBall();
            playerWithTheBall = p;
            return false;
        }
    }

    public void Pass(){
        g.incTimerBy(averageAction);
        FootballPlayer p;
        if(home){
            p = g.getHome().getPlaying().stream().filter(e -> !e.equals(playerWithTheBall)).findAny().get();
        }
        else{
            p = g.getAway().getPlaying().stream().filter(e -> !e.equals(playerWithTheBall)).findAny().get();
        }
        playerWithTheBall = p;
    }

    public boolean tryShoot(){
        g.incTimerBy(quickAction);
        Random rand = new Random();
        Striker s = (Striker) playerWithTheBall;
        if(rand.nextInt(101) <= s.getShooting()){
            Shoot();
            return true;
        }
        else return false;
    }

    public void Shoot(){
        g.incTimerBy(quickAction);
        Goalkeeper goalie;
        Striker s = (Striker) playerWithTheBall;
        Random rand = new Random();
        if(home){
            goalie = (Goalkeeper) g.getAway().getPlayer(Goalkeeper.class,true);
            if(goalie.getOverallSkill() - s.getOverallSkill() <= 10){
                if(rand.nextInt(101) <= s.getShooting()){ // se o chute for bem sucedido atualiza score
                    g.incPoints1();
                    playerWithTheBall = g.getAway().getPlayer(Midfielder.class,true);
                }
                else{ // se falhar o chute, a bola vai para um lateral da equipa adversaria
                    playerWithTheBall = g.getAway().getPlayer(Lateral.class,true);
                }
            }
            else{// se a skill do guarda-redes for muito superior a do avancado, fica com a bola
                playerWithTheBall = goalie;
            }
        }
        else{
            goalie = (Goalkeeper) g.getHome().getPlayer(Goalkeeper.class,true);
            if(Math.abs(goalie.getOverallSkill() - s.getOverallSkill()) <= 10){
                if(rand.nextInt(101) <= s.getShooting()){ // se o chute for bem sucedido atualiza score
                    g.incPoints2();
                    playerWithTheBall = g.getHome().getPlayer(Midfielder.class,true);
                }
                else{ // se falhar o chute, a bola vai para um lateral da equipa adversaria
                    playerWithTheBall = g.getHome().getPlayer(Lateral.class,true);
                }
            }
            else{// se a skill do guarda-redes for muito superior a do avancado, fica com a bola
                playerWithTheBall = goalie;
            }
        }
        invertTeamWithBall();
    }

    public void invertTeamWithBall(){
        home = !home;
    }


    public Class<?> getFootballPlayerOppositeClass(){
        if(playerWithTheBall.getClass().equals(Defender.class) || playerWithTheBall.getClass().equals(Lateral.class)) return Striker.class;
        if(playerWithTheBall.getClass().equals(Midfielder.class)) return Midfielder.class;
        if(playerWithTheBall.getClass().equals(Striker.class)) return Goalkeeper.class;
        else return null;
    }


    public void incTimer(double action){
        FootballGame game = getGame();
        game.incTimerBy(action);
        setGame(game);
    }



}


