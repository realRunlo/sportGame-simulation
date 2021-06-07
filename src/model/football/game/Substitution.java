package model.football.game;

import model.football.player.FootballPlayer;

public class Substitution {
    private FootballPlayer pOut;
    private FootballPlayer pIn;

    public Substitution(FootballPlayer pOut, FootballPlayer pIn) {
        this.setPOut(pOut);
        this.setPIn(pIn);
    }

    public Substitution(Substitution s) {
        this.setPOut(s.getPOut());
        this.setPIn(s.getPIn());
    }

    public void setPOut(FootballPlayer p) {
        this.pOut = p.clone();
    }
    public FootballPlayer getPOut() {
        return this.pOut.clone();
    }

    public void setPIn(FootballPlayer p) {
        this.pIn = p.clone();
    }
    public FootballPlayer getPIn() {
        return this.pIn.clone();
    }

    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || !this.getClass().equals(o.getClass()))
            return false;

        Substitution s = (Substitution) o;

        return this.getPOut().equals(s.getPOut()) &&
                this.getPIn().equals(s.getPIn());
    }

    @Override
    public Substitution clone() {
        return new Substitution(this);
    }

    @Override
    public String toString() {
        return this.getPIn().getName() + " -> " + this.getPOut().getName();
    }

    public String toCSV() {
        return this.getPIn().getNumber() + "->" + this.getPOut().getNumber();
    }

    @Override
    public int hashCode() {
        return this.getPOut().hashCode() * this.getPIn().hashCode();
    }
}
