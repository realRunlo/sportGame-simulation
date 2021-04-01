public class state
{
    private int numbPlayers;
    private int numbTeams;
    private int day;
    //falta <Game>
    public state(){
        numbPlayers = 0;
        numbTeams = 0;
        day = 0;
    }
    public state(int newPlayers, int newTeams, int newDay){
        numbPlayers = newPlayers;
        numbTeams = newTeams;
        day = newDay;
    }
}
