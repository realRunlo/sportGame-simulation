import football.team.FootballTeam;

import java.time.LocalDateTime;
import java.util.Objects;

public class Game {
    private boolean g; // information if there is or not a game
    private int points1;
    private int points2;
    //part
    private LocalDateTime timer;
    private FootballTeam t1;
    private FootballTeam t2;


    public Game() {
        g = false;
        points1 = 0;
        points2 = 0;
        timer = LocalDateTime.now();
        t1 = new FootballTeam();
        t2 = new FootballTeam();
    }

    public Game(boolean b,int newP1, int newP2, LocalDateTime newTimer, FootballTeam newt1, FootballTeam newt2) {
        g = b;
        points1 = newP1;
        points2 = newP2;
        timer = newTimer;
        t1 = newt1.clone();
        t2 = newt2.clone();
    }

    public Game(Game newGame) {
        g = newGame.getBol();
        points1 = newGame.getPoints1();
        points2 = newGame.getPoints2();
        timer = newGame.getTimer();
        t1 = newGame.getTeam1();
        t2 = newGame.getTeam2();
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

    public void setPoints2(int newPoints) {
        points2 = newPoints;
    }

    public LocalDateTime getTimer() {
        return timer;
    }

    public void setTimer(LocalDateTime newTimer) {
        timer = newTimer;
    }

    public FootballTeam getTeam1() {
        return t1.clone();
    }

    public void setTeam1(FootballTeam newTeam) {
        t1 = newTeam.clone();
    }

    public FootballTeam getTeam2() {
        return t2.clone();
    }

    public void setTeam2(FootballTeam newTeam) {
        t2 = newTeam.clone();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;

        return g == game.getBol() &&
                game.getPoints1() == points1 &&
                game.getPoints2() == points2 &&
                game.getTimer().equals(timer) &&
                game.getTeam1().equals(t1) &&
                game.getTeam2().equals(t2);
    }

    @Override
    public Game clone() {
        return new Game(this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Game: ")
                .append(t1.getName()).append("vs").append(t2.getName())
                .append("(").append(points1).append(",").append(points2).append(")\n")
                .append(timer.toString()).append("\n");
        return sb.toString();
    }
}
