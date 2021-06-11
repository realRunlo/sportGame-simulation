package model.generic.game;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import model.generic.team.Team;
import model.interfaces.Saveable;

public abstract class Game implements Saveable,Serializable {
    private boolean ended; // information if there is or not a game
    private int points1;
    private int points2;
    private Team t1;
    private Team t2;
    private LocalDate date;
    private double timer;

    //----------------------------------Construtores------------------------------------------------

    /**
     * Construtor de Game
     * @param t1 equipa da casa
     * @param t2 equipa visitante
     */
    public Game(Team t1, Team t2) {
        this.setBol(true);
        this.setPoints2(0);
        this.setPoints2(0);
        this.setT1(t1.clone());
        this.setT2(t2.clone());
        this.setDate(LocalDate.now());
        this.setTimer(0);
    }

    /**
     * Construtor de Game
     * @param b booleano indicador se o jogo ainda pode ser executado
     * @param newP1 score da casa
     * @param newP2 score visitante
     * @param date data da criacao do jogo
     * @param newTimer contador do jogo
     * @param newt1 equipa da casa
     * @param newt2 equipa visitante
     */
    public Game(boolean b, int newP1, int newP2, LocalDate date, int newTimer, Team newt1, Team newt2) {
        this.setBol(b);
        this.setPoints1(newP1);
        this.setPoints2(newP2);
        this.setT1(newt1.clone());
        this.setT2(newt2.clone());
        this.setDate(date);
        this.setTimer(newTimer);
    }

    /**
     * Construtor de Game
     * @param newGame jogo a copiar
     */
    public Game(Game newGame) {
        this.setBol(newGame.getBol());
        this.setPoints1(newGame.getPoints1());
        this.setPoints2(newGame.getPoints2());
        this.setT1(newGame.getT1().clone());
        this.setT2(newGame.getT2().clone());
        this.setDate(newGame.getDate());
        this.setTimer(newGame.getTimer());
    }

    /**
     * Construtor de Game
     * @param teamName1 nome da equipa da casa
     * @param teamName2 nome da equipa visitante
     * @param points1 score da casa
     * @param points2 score visitante
     * @param date data de criacao do jogo
     */
    public Game(String teamName1, String teamName2, int points1, int points2, LocalDate date) {
        this.setBol(false);
        this.setPoints1(points1);
        this.setPoints2(points2);
        this.setDate(date);
        this.t1 = null;
        this.t2 = null;
    }

    //----------------------------------Getters e Setters----------------------------------------------------------

    /**
     * Getter de boleano de estado do jogo
     * @return estado do jogo
     */
    public boolean getBol(){
        return ended;
    }

    /**
     * Setter do boleano do estado do jogo
     * @param b novo estado do jogo
     */
    public void setBol(boolean b){
        ended = b;
    }

    /**
     * Getter do score da casa
     * @return score da casa
     */
    public int getPoints1() {
        return points1;
    }

    /**
     * Setter do score da casa
     * @param newPoints novo score da casa
     */
    public void setPoints1(int newPoints) {
        points1 = newPoints;
    }

    /**
     * Getter do score visitante
     * @return score visitante
     */
    public int getPoints2() {
        return points2;
    }

    /**
     * Setter da equipa da casa
     * @param t1 nova equipa da casa
     */
    public void setT1(Team t1) {
        this.t1 = t1.clone();
    }

    /**
     * Getter da equipa da casa
     * @return equipa da casa
     */
    public Team getT1(){return t1.clone();};

    /**
     * Setter da equipa visitante
     * @param t2 nova equipa visitante
     */
    public void setT2(Team t2) {
        this.t2 = t2.clone();
    }

    /**
     * Getter da equipa visitante
     * @return equipa visitante
     */
    public Team getT2(){return t2.clone();};

    /**
     * Setter do score visitante
     * @param newPoints novo score visitante
     */
    public void setPoints2(int newPoints) {
        points2 = newPoints;
    }

    /**
     * Setter da data de criacao do jogo
     * @param date nova data
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Getter da data de criacao do jogo
     * @return data
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Getter do contador do jogo
     * @return contador do jogo
     */
    public double getTimer() {
        return timer;
    }

    /**
     * Setter do contador do jogo
     * @param newTimer novo contador do jogo
     */
    public void setTimer(double newTimer) {
        timer = newTimer;
    }

    /**
     * Incrimenta o contador do jogo
     * @param time valor a incrementar
     */
    public void incTimerBy(double time){
        timer += time;
    }

    /**
     * Incrimenta o score da casa
     */
    public void incPoints1(){
        points1++;
    }

    /**
     * Incrementa o score visitante
     */
    public void incPoints2(){
        points2++;
    }

    /**
     * Equals da classe Game
     * @param o objeto a comparar
     * @return resultado da comparacao
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;

        return ended == game.getBol() &&
                game.getPoints1() == points1 &&
                game.getPoints2() == points2 &&
                game.getTimer() == (timer);
    }

    /**
     * ToString da classe Game
     * @return Game em formato string
     */
    public String toCSV() {
        return this.getT1().getName()  + "," +
                this.getT2().getName() + "," +
                this.getPoints1()      + "," +
                this.getPoints2()      + "," +
                this.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Grava o jogo num ficheiro
     * @param filePath ficheiro onde guardar
     * @throws IOException
     */
    public void save(String filePath) throws IOException {
        BufferedWriter br = new BufferedWriter(new FileWriter(filePath, true));

        br.write(this.toCSV());
        br.flush();
        br.close();
    }
}
