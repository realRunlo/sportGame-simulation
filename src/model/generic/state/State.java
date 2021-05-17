package model.generic.state;

import model.Saveable;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public abstract class State implements Saveable {
    private int numPlayers;
    private int numTeams;
    private int day;

    public State() {
        this.setNumPlayers(0);
        this.setNumTeams(0);
        this.setDay(0);
    }

    public State(int numPlayers, int numTeams, int day) {
        this.setNumPlayers(numPlayers);
        this.setNumTeams(numTeams);
        this.setDay(day);
    }

    public State(State state) {
        this.setNumPlayers(state.getNumPlayers());
        this.setNumTeams(state.getNumTeams());
        this.setDay(state.getDay());
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }
    public int getNumPlayers() {
        return this.numPlayers;
    }

    public void setNumTeams(int numTeams) {
        this.numTeams = numTeams;
    }
    public int getNumTeams() {
        return numTeams;
    }

    public void incNPlayers(){
        this.setNumPlayers(this.getNumPlayers()+1);
    }

    public void decNPlayers(){
        this.setNumPlayers(this.getNumPlayers()-1);
    }

    public void incNTeams(){
        this.setNumTeams(this.getNumTeams()+1);
    }

    public void decNTeams(){
        this.setNumTeams(this.getNumTeams()-1);
    }

    public void setDay(int day) {
        this.day = day;
    }
    public int getDay() {
        return day;
    }

    public String toCSV() {
        return "" + this.getDay();
    }

    @Override
    public void save(String filePath) throws FileNotFoundException {
        PrintWriter file = new PrintWriter(filePath);

        file.println(this.toCSV());
        file.flush();
        file.close();
    }
}
