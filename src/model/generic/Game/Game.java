package model.generic.Game;

import model.generic.team.Team;

import java.io.Serializable;

public abstract class Game implements Serializable {
    private boolean g; // information if there is or not a game
    private int points1;
    private int points2;
    private Team t1;
    private Team t2;
    private double timer;

    //----------------------------------Construtores------------------------------------------------
    public Game(Team t1, Team t2) {
        this.setBol(false);
        this.setPoints2(0);
        this.setPoints2(0);
        this.setTimer(0);
        this.setT1(t1.clone());
        this.setT2(t2.clone());
    }

    public Game(boolean b, int newP1, int newP2, int newTimer, Team newt1, Team newt2) {
        this.setBol(b);
        this.setPoints1(newP1);
        this.setPoints2(newP2);
        this.setT1(newt1.clone());
        this.setT2(newt2.clone());
        this.setTimer(newTimer);
    }

    public Game(Game newGame) {
        this.setBol(newGame.getBol());
        this.setPoints1(newGame.getPoints1());
        this.setPoints2(newGame.getPoints2());
        this.setT1(newGame.getT1().clone());
        this.setT2(newGame.getT2().clone());
        this.setTimer(newGame.getTimer());
    }

    //----------------------------------Getters e Setters----------------------------------------------------------
    public boolean getBol(){
        return g;
    }

    public void setBol(boolean b){
        g = b;
    }

    public int getPoints1() {
        return points1;
    }

    public void setPoints1(int newPoints) {
        points1 = newPoints;
    }

    public int getPoints2() {
        return points2;
    }

    public void setT1(Team t1) {
        this.t1 = t1.clone();
    }
    public Team getT1(){return t1.clone();};

    public void setT2(Team t2) {
        this.t2 = t2.clone();
    }
    public Team getT2(){return t2.clone();};

    public void setPoints2(int newPoints) {
        points2 = newPoints;
    }

    public double getTimer() {
        return timer;
    }

    public void setTimer(double newTimer) {
        timer = newTimer;
    }

    public void incTimerBy(double time){
        timer += time;
    }

    public void incPoints1(){
        points1++;
    }

    public void incPoints2(){
        points2++;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;

        return g == game.getBol() &&
                game.getPoints1() == points1 &&
                game.getPoints2() == points2 &&
                game.getTimer() == (timer);
    }
}
