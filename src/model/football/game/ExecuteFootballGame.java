package model.football.game;

import model.exceptions.PlayerDoenstExist;
import model.football.player.*;
import model.football.team.lineup.FootballLineup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ExecuteFootballGame {
    private FootballGame g;
    private FootballPlayer playerWithTheBall; //jogador com a bola neste momento
    private boolean home; //team with the ball
    private List<String> gameReport;


    private static final double quickAction = 0.5;
    private static final double averageAction = 1;
    private static final double slowAction = 1.5;
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";


    public ExecuteFootballGame(FootballGame g){
        Random rand = new Random();
        setGame(g);
        gameReport = new ArrayList<>();
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

    public List<String> getGameReport(){
        return gameReport;
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

    public void ExecutePlay() throws PlayerDoenstExist {
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
        incTimer(quickAction);
        registerAction(p.getName() + " tries to steal the ball!");
        Random rand = new Random();
        boolean steal = false;
        if(playerWithTheBall.getClass().equals(Defender.class)){//se for defesa tem maior chance de manter a bola
            Defender d = (Defender) playerWithTheBall;
            if(rand.nextInt(101) <= d.getBallRetention());
            else {
                setPlayerWithTheBall(p,!getHome());
                steal = true;
            }
            }
        else if(p.getClass().equals(Midfielder.class)){ //melhorar, talvez dar 2 skill checks ao medio
            Midfielder m = (Midfielder) p;
            steal = rand.nextInt(101) <= m.getBallRecovery();
        }
        else{ //caso normal, verifica quem tem melhor destreza
            steal = playerWithTheBall.getDexterity() <= p.getDexterity();
        }
        if(steal) registerAction("And he's got it!");
        else registerAction("But fails...");
        return steal;
    }

    public void tryPass() throws PlayerDoenstExist {
        registerAction(playerWithTheBall.getName() + " tries to pass the ball.");
        incTimer(quickAction);
        Random rand = new Random();
        if(rand.nextInt(101) <= playerWithTheBall.getPassing()){
            Pass();
        }
        else registerAction("But decides otherwise.");
    }


    public boolean tryPass(FootballPlayer p) throws PlayerDoenstExist {
        registerAction(playerWithTheBall.getName() + " tries to pass the ball.");
        incTimer(quickAction);
        Random rand = new Random();
        if(rand.nextInt(101) <= playerWithTheBall.getPassing()){
            Pass();
            return true;
        }
        else {
            registerAction("But " + p.getName() + "intercepts ball!");
            invertTeamWithBall();
            playerWithTheBall = p;
            return false;
        }
    }

    public void Pass() throws PlayerDoenstExist {
        incTimer(averageAction);
        FootballPlayer p;
        if(home){
            p = g.getHome().getPlayer(higherPositionClass(),true);
        }
        else{
            p = g.getAway().getPlayer(higherPositionClass(),true);
        }
        registerAction(p.getName() + " now has the ball.");
        playerWithTheBall = p;
    }

    public boolean tryShoot() throws PlayerDoenstExist{
        registerAction(YELLOW + playerWithTheBall.getName() + " tries to score!!!!!" + RESET);
        incTimer(quickAction);
        Random rand = new Random();
        Striker s = (Striker) playerWithTheBall;
        if(rand.nextInt(101) <= s.getShooting()){
            Shoot();
            return true;
        }
        else{
            registerAction(RED + "Fate did not feel the same way..." + RESET);
            return false;
        }
    }

    public void Shoot() throws PlayerDoenstExist{
        registerAction("He shoots!!!");
        incTimer(quickAction);
        Goalkeeper goalie;
        Striker s = (Striker) playerWithTheBall;
        Random rand = new Random();
        if(home){
            goalie = (Goalkeeper) g.getAway().getPlayer(Goalkeeper.class,true);
            if(goalie.getOverallSkill() - s.getOverallSkill() <= 10){
                if(rand.nextInt(101) <= s.getShooting()){ // se o chute for bem sucedido atualiza score
                    registerAction(GREEN + "And he Scores!\nGOOOOOOOOOOOOOOOOOOOOOOOOOaaaaaaalllllllllll!!!!!!!!!" + RESET);
                    incTimer(slowAction);
                    g.incPoints1();
                    playerWithTheBall = g.getAway().getPlayer(Midfielder.class,true);
                }
                else{ // se falhar o chute, a bola vai para um lateral da equipa adversaria
                    registerAction(RED + "Swing and a miss" + RESET);
                    playerWithTheBall = g.getAway().getPlayer(Lateral.class,true);
                }
            }
            else{// se a skill do guarda-redes for muito superior a do avancado, fica com a bola
                registerAction(RED + "But the goalkeeper doens't let that slide!" + RED);
                playerWithTheBall = goalie;
            }
        }
        else{
            goalie = (Goalkeeper) g.getHome().getPlayer(Goalkeeper.class,true);
            if(goalie.getOverallSkill() - s.getOverallSkill() <= 10){
                if(rand.nextInt(101) <= s.getShooting()){ // se o chute for bem sucedido atualiza score
                    registerAction(GREEN + "And he Scores!\nGOOOOOOOOOOOOOOOOOOOOOOOOOaaaaaaalllllllllll!!!!!!!!!" + RESET);
                    incTimer(slowAction);
                    g.incPoints2();
                    playerWithTheBall = g.getHome().getPlayer(Midfielder.class,true);
                }
                else{ // se falhar o chute, a bola vai para um lateral da equipa adversaria
                    registerAction(RED + "Swing and a miss" + RESET);
                    playerWithTheBall = g.getHome().getPlayer(Lateral.class,true);
                }
            }
            else{// se a skill do guarda-redes for muito superior a do avancado, fica com a bola
                registerAction(RED + "But the goalkeeper doens't let that slide!" + RED);
                playerWithTheBall = goalie;
            }
        }
        invertTeamWithBall();
    }

    public void invertTeamWithBall(){
        home = !home;
    }


    public Class<? extends FootballPlayer> getFootballPlayerOppositeClass(){
        if(playerWithTheBall.getClass().equals(Defender.class) || playerWithTheBall.getClass().equals(Lateral.class)) return Striker.class;
        if(playerWithTheBall.getClass().equals(Midfielder.class)) return Midfielder.class;
        if(playerWithTheBall.getClass().equals(Striker.class)) return Goalkeeper.class;
        else return null;
    }

    public Class<? extends FootballPlayer> higherPositionClass(){
        if(playerWithTheBall.getClass().equals(Defender.class)) return Midfielder.class;
        if(playerWithTheBall.getClass().equals(Midfielder.class)|| playerWithTheBall.getClass().equals(Lateral.class)) return Striker.class;
        if(playerWithTheBall.getClass().equals(Striker.class)) return Striker.class;
        if(playerWithTheBall.getClass().equals(Goalkeeper.class)) return Lateral.class;
        else return null;
    }


    public void incTimer(double action){
        g.incTimerBy(action);
    }


    public void registerAction(String message){
        gameReport.add(message);
    }

    public double getTimer(){
        return g.getTimer();
    }

    public void setLineup(FootballLineup l, boolean home){
        if(l!=null) {
            if (home) g.setHome(l);
            else g.setAway(l);
        }
    }

}


