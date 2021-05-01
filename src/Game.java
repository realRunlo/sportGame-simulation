import java.time.LocalDateTime;
import java.util.Objects;

public class Game {
    private boolean g; // information if there is or not a game
    private int points1;
    private int points2;
    //part
    private LocalDateTime timer;
    private String team1;
    private String team2;


    public Game() {
        points1 = 0;
        points2 = 0;
        timer = LocalDateTime.now();
        team1 = "Nao adicionado";
        team2 = "Nao adicionado";
    }

    public Game(int newP1, int newP2, LocalDateTime newTimer, String newTeam1, String newTeam2) {
        points1 = newP1;
        points2 = newP2;
        timer = newTimer;
        team1 = newTeam1;
        team2 = newTeam2;
    }

    public Game(Game g) {
        points1 = g.getPoints1();
        points2 = g.getPoints2();
        timer = g.getTimer();
        team1 = g.getTeam1();
        team2 = g.getTeam2();
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

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String newTeam) {
        team1 = newTeam;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String newTeam) {
        team2 = newTeam;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;

        return game.getPoints1() == points1 &&
                game.getPoints2() == points2 &&
                game.getTimer().equals(timer) &&
                game.getTeam1().equals(team1) &&
                game.getTeam2().equals(team2);
    }

    @Override
    public Game clone() {
        return new Game(this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Game: ")
                .append(team1).append("vs").append(team2)
                .append("(").append(points1).append(",").append(points2).append(")\n")
                .append(timer.toString()).append("\n");
        return sb.toString();
    }
}
