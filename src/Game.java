import java.time.LocalDateTime;

public class Game
{
    private int points1;
    private int points2;
    //part
    private LocalDateTime timer;
    private String team1;
    private String team2;


    public Game(){
        points1 = 0;
        points2 = 0;
        timer = LocalDateTime.now();
        team1 = "";
        team2 = "";
    }
    
    public Game(int newP1; int newP2; LocalDateTime newTimer; String newTeam1; String newTeam2){
        points1 = newP1;
        points2 = newP2;
        timer = newTimer;
        team1 = newTeam1;
        team2 = newTeam2;
    }

    public Game(Game g){
        points1 = g.getPoints1();
        points2 = g.getPoints2();
        timer = g.getTimer();
        team1 = g.getTeam1();
        team2 = g.getTeam2();
    }

    public int getPoints1(){
        return points1;
    }

    public void setPoints1(int newPoints){
        points1 = newPoints;
    }

    public int getPoints2(){
        return points2;
    }

    public void setPoints2(int newPoints){
        points2 = newPoints;
    }

    public LocalDateTime getTimer() {
        return timer;
    }

    public void setTimer(LocalDateTime newTimer){
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
}