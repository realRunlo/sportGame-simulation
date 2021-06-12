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
    private int cicle = 0; //used to stop cicles

    private static final double quickAction = 0.5;
    private static final double averageAction = 1;
    private static final double slowAction = 1.5;
    private static final String RESET = "\u001B[0m";
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";


    /**
     * Contrutor de classe
     * @param g jogo a ser executado
     */
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

    /**
     * Metodo que define o jogador com a bola
     * @param p jogador a fichar com a posse da bola
     * @param home equipa do jogador
     */
    private void setPlayerWithTheBall(FootballPlayer p, boolean home){
        playerWithTheBall = p;
        //caso o novo jogador seja da equipa que antes nao tinha posse da bola, inverte a posse
        if(home != getHome()) invertTeamWithBall();
    }

    /**
     * Metodo que devolve o relatorio do jogo
     * @return relatorio do jogo
     */
    public List<String> getGameReport(){
        return new ArrayList<String>(gameReport);
    }

    /**
     * Metodo que indica se a equipa com posse de bola e a da casa ou visitante
     * @return booleano
     */
    private boolean getHome(){
        return home;
    }

    /**
     * Metodo para definir a equipa com posse de bola
     * @param homeOrAway booleano que indica se a equipa com posse de bola ser a
     *                   da casa ou a visitante
     */
    private void setHome(boolean homeOrAway){
        home = homeOrAway;
    }

    /**
     * Metodo que devolve o jogo a ser executado
     * @return jogo
     */
    public FootballGame getGame(){
        return g.clone();
    }

    /**
     * Metodo que permite definir/atualizar o jogo a ser executado
     * @param g jogo atualizado
     */
    public void setGame(FootballGame g){
        this.g = g.clone();
    }

    /**
     * Metodo principal que simula uma acao no jogo
     * @throws PlayerDoenstExist
     */
    public void ExecutePlay() throws PlayerDoenstExist {
        FootballGame g = getGame();
        FootballLineup playing = new FootballLineup();
        FootballLineup adversary = new FootballLineup();
        if(getHome()){
            playing = g.getHome();
            adversary = g.getAway();
        }
        else{
            playing = g.getAway();
            adversary = g.getHome();
        }
        FootballPlayer p;
        p = adversary.getPlayer(getFootballPlayerOppositeClass(),true);

        if(p != null && g.getBol() == true) {
            if (playerWithTheBall.getClass().equals(Striker.class)) { // se for um avancado tenta rematar
                if(speedCheck(p) > 10) {
                    if (tryShoot());
                    else {
                        playerWithTheBall = p;
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
                        playerWithTheBall = p;
                        invertTeamWithBall();
                    }
                }
            } else { //outras classes tentam passar
                if (speedCheck(p) < 10) {
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


    /**
     * Metodo auxiliar que simula uma tentativa de roubo de bola
     * @param p jogador que tenta roubar a bola
     * @return booleano que indica se o jogador foi sucedido
     */
    private boolean tryStealBall(FootballPlayer p){
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

    /**
     * Metodo que simula a tentativa de passe de bola
     * @throws PlayerDoenstExist
     */
    private void tryPass() throws PlayerDoenstExist {
        registerAction(playerWithTheBall.getName() + " tries to pass the ball.");
        incTimer(quickAction);
        Random rand = new Random();
        if(rand.nextInt(101) <= playerWithTheBall.getPassing() || cicle > 2){
            cicle = 0;
            Pass();
        }
        else{
            cicle++;
            registerAction("But decides otherwise.");
        }
    }

    /**
     * Metodo opcional de simulacao de tentativa de passe caso haja intersecao
     * da bola no caso de falha de passe
     * @param p jogador que intercetara a bola
     * @return booleano sobre o sucesso do passe
     * @throws PlayerDoenstExist
     */
    private boolean tryPass(FootballPlayer p) throws PlayerDoenstExist {
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

    /**
     * Metodo que simula um passe
     * @throws PlayerDoenstExist
     */
    private void Pass() throws PlayerDoenstExist {
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

    /**
     * Metodo que simula a tentativa de um remate
     * @return booleano que indica o sucesso da tentativa de remate
     * @throws PlayerDoenstExist
     */
    private boolean tryShoot() throws PlayerDoenstExist{
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

    /**
     * Metodo que simula um remate
     * @throws PlayerDoenstExist
     */
    private void Shoot() throws PlayerDoenstExist{
        registerAction("He shoots!!!");
        incTimer(quickAction);
        Goalkeeper goalie;
        Striker s = (Striker) playerWithTheBall;
        Random rand = new Random();
        FootballLineup playing = new FootballLineup();
        FootballLineup adversary = new FootballLineup();

        if(getHome()){
            playing = g.getHome();
            adversary = g.getAway();
        }
        else{
            playing = g.getAway();
            adversary = g.getHome();
        }

        goalie = (Goalkeeper) adversary.getPlayer(Goalkeeper.class,true);
        if(goalie.getOverallSkill() - s.getOverallSkill() <= 10){
            if(rand.nextInt(101) <= s.getShooting()){ // se o chute for bem sucedido atualiza score
                registerAction(GREEN + "And he Scores!\nGOOOOOOOOOOOOOOOOOOOOOOOOOaaaaaaalllllllllll!!!!!!!!!" + RESET);
                incTimer(slowAction);
                if(home) g.incPoints1();
                else g.incPoints2();

                playerWithTheBall = adversary.getPlayer(Midfielder.class,true);
                registerAction("Waiting for the kick off." );
                registerAction(playerWithTheBall.getName() + " kicks the ball." );
            }
            else{ // se falhar o chute, a bola vai para um lateral da equipa adversaria
                registerAction(RED + "Swing and a miss" + RESET);
                playerWithTheBall = adversary.getPlayer(Lateral.class,true);
            }
        }
        else{// se a skill do guarda-redes for muito superior a do avancado, fica com a bola
            registerAction(RED + "But the goalkeeper doens't let that slide!" + RESET);
            playerWithTheBall = goalie;
        }

        invertTeamWithBall();
    }

    /**
     * Metodo que inverte a equipa com a posse de bola
     */
    private void invertTeamWithBall(){
        home = !home;
    }


    /**
     * Metodo que devolve a classe adversaria do jogador com posse de bola
     * @return classe adversaria
     */
    private Class<? extends FootballPlayer> getFootballPlayerOppositeClass(){
        if(playerWithTheBall.getClass().equals(Defender.class) || playerWithTheBall.getClass().equals(Lateral.class)) return Striker.class;
        if(playerWithTheBall.getClass().equals(Midfielder.class)) return Midfielder.class;
        if(playerWithTheBall.getClass().equals(Striker.class)) return Defender.class;
        else return null;
    }

    /**
     * Metodo que devolve a classe de nivel superior ao jogador atual
     * @return classe de nivel superior
     */
    private Class<? extends FootballPlayer> higherPositionClass(){
        if(playerWithTheBall.getClass().equals(Defender.class)) return Midfielder.class;
        if(playerWithTheBall.getClass().equals(Midfielder.class)|| playerWithTheBall.getClass().equals(Lateral.class)) return Striker.class;
        if(playerWithTheBall.getClass().equals(Striker.class)) return Striker.class;
        if(playerWithTheBall.getClass().equals(Goalkeeper.class)) return Lateral.class;
        else return null;
    }


    /**
     * Metodo que incrementa o contador do jogo
     * @param action tempo a icrementar no contador
     */
    private void incTimer(double action){
        g.incTimerBy(action);
    }


    /**
     * Regista uma mensagem no relatorio
     * @param message mensagem a registar
     */
    private void registerAction(String message){
        gameReport.add(message);
    }

    /**
     * Devolve o contador do jogo
     * @return contador do jogo
     */
    public double getTimer(){
        return g.getTimer();
    }

    /**
     * Atualiza o plantel de uma das equipas
     * @param l novo plantel
     * @param home equipa a atualizar
     */
    public void setLineup(FootballLineup l, boolean home){
        if(l!=null) {
            if (home) g.setHome(l);
            else g.setAway(l);
        }
    }

    /**
     * Metodo que realizar um speedCheck entre o jogador atual e o adversario
     * @param adversary jogador adversario a comprar a velocidade
     * @return resultado da comparacao
     */
    private int speedCheck(FootballPlayer adversary){
        return (playerWithTheBall.getSpeed() - adversary.getSpeed());
    }


}


