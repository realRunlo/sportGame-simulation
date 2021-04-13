public class State {
    private int numbPlayers;
    private int numbTeams;
    private int day;
    private Game g;

    public State(){
        numbPlayers = 0;
        numbTeams = 0;
        day = 0;
        g = new Game();
    }
    public State(int newPlayers, int newTeams, int newDay, Game newGame){
        numbPlayers = newPlayers;
        numbTeams = newTeams;
        day = newDay;
        g = newGame.clone();
    }

    public State(State s){

    }
}
