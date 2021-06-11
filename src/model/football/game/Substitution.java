package model.football.game;

import model.football.player.FootballPlayer;

public class Substitution {
    private FootballPlayer pOut;
    private FootballPlayer pIn;

    /**
     * Construtor de Substitution
     * @param pOut jogador a ir para o banco
     * @param pIn jogador a ir jogar
     */
    public Substitution(FootballPlayer pOut, FootballPlayer pIn) {
        this.setPOut(pOut);
        this.setPIn(pIn);
    }

    /**
     * Construtor de Substitution
     * @param s Substitution a copiar
     */
    public Substitution(Substitution s) {
        this.setPOut(s.getPOut());
        this.setPIn(s.getPIn());
    }

    /**
     * Setter do jogador a ir para o banco
     * @param p jogador em questao
     */
    public void setPOut(FootballPlayer p) {
        this.pOut = p.clone();
    }

    /**
     * Getter do jogador a ir para o banco
     * @return jogador que vai para o banco
     */
    public FootballPlayer getPOut() {
        return this.pOut.clone();
    }

    /**
     * Setter do jogador que vai jogar
     * @param p jogador que vai jogar
     */
    public void setPIn(FootballPlayer p) {
        this.pIn = p.clone();
    }

    /**
     * Getter do jogador que vai jogar
     * @return jogador que vai jogar
     */
    public FootballPlayer getPIn() {
        return this.pIn.clone();
    }

    /**
     * Equals da classe Substitution
     * @param o objeto a comparar
     * @return resultado da comparacao
     */
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
    /**
     * Cloner da classe Substitution
     */
    public Substitution clone() {
        return new Substitution(this);
    }

    @Override
    /**
     * Metodo toString da classe Substitution
     */
    public String toString() {
        return this.getPIn().getName() + " -> " + this.getPOut().getName();
    }

    /**
     * Metodo que torna substitution em uma string gravavel
     * @return string gravavel
     */
    public String toCSV() {
        return this.getPIn().getNumber() + "->" + this.getPOut().getNumber();
    }

    @Override
    public int hashCode() {
        return this.getPOut().hashCode() * this.getPIn().hashCode();
    }
}
