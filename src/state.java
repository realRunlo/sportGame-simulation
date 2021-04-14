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
        numbPlayers = s.getNPlayers();
        numbTeams = s.getNTeams();
        day = s.getDay();
        g = s.getGame();
    }

    public int getNPlayers(){
        return numbPlayers;
    }

    public void setNumbPlayers(int nP){
        numbPlayers = nP;
    }

    public int getNTeams(){
        return numbTeams;
    }

    public void setNTeams(int nTeams){
        numbTeams = nTeams;
    }

    public int getDay(){
        return day;
    }

    public void setDay(int nDay){
        day = nDay;
    }

    public Game getGame(){
        return g.clone();
    }

    public void setGame(Game newG){
        g = new Game(newG);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State s = (State) o;

        return numbPlayers == s.getNPlayers() &&
                numbTeams == s.getNTeams() &&
                day == s.getDay() &&
                g.equals(s.getGame());
    }

    public State clone(){
        return new State(this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Estado\n").append("Numero de jogadores: ").append(getNPlayers())
                .append("\nNumero de equipas: ").append(getNTeams())
                .append("\nDias: ").append(getDay())
                .append("\n").append(g.toString());

        return sb.toString();
    }
}
