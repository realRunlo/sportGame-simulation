package generic.Game;

import football.team.FootballTeam;
import generic.team.Team;

import java.time.LocalDateTime;

public abstract class Game {
    private boolean g; // information if there is or not a game
    private int points1;
    private int points2;
    private Team t1;
    private Team t2;
    private LocalDateTime timer;


    public Game() {
        g = false;
        points1 = 0;
        points2 = 0;
        timer = LocalDateTime.now();
    }

    public Game(boolean b, int newP1, int newP2, LocalDateTime newTimer, Team newt1, Team newt2) {
        g = b;
        points1 = newP1;
        points2 = newP2;
        t1 = newt1.clone();
        t2 = newt2.clone();
        timer = newTimer;

    }

    public Game(Game newGame) {
        g = newGame.getBol();
        points1 = newGame.getPoints1();
        points2 = newGame.getPoints2();
        t1 = newGame.getT1();
        t2 = newGame.getT2();
        timer = newGame.getTimer();
    }

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

    public Team getT1(){return t1.clone();};

    public Team getT2(){return t2.clone();};

    public void setPoints2(int newPoints) {
        points2 = newPoints;
    }

    public LocalDateTime getTimer() {
        return timer;
    }

    public void setTimer(LocalDateTime newTimer) {
        timer = newTimer;
    }



    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;

        return g == game.getBol() &&
                game.getPoints1() == points1 &&
                game.getPoints2() == points2 &&
                game.getTimer().equals(timer);
    }
}
