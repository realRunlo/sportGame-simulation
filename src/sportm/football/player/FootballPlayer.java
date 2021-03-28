package sportm.football.player;

import sportm.football.foul.Card;
import sportm.generic.player.Player;

public class FootballPlayer extends Player {
    Position position;
    int speed;
    int resistance;
    int dexterity;
    int implosion;
    int headGame;
    int kick;
    int passing;
    int elasticidade;
    Card card;

    public FootballPlayer(String name,
                          String team,
                          Position position,
                          int speed,
                          int resistance,
                          int dexterity,
                          int implosion,
                          int headGame,
                          int kick,
                          int passing,
                          Card card) {
        super(name, team);
        this.setPosition(position);
        this.setSpeed(speed);
        this.setResistance(resistance);
        this.setDexterity(dexterity);
        this.setImplosion(implosion);
        this.setHeadGame(headGame);
        this.setKick(kick);
        this.setPassing(passing);
        this.setCard(card);
        this.setOverallSkill(calcGlobalSkill());
    
    }

    public FootballPlayer(String name,
                          Position position,
                          int speed,
                          int resistance,
                          int dexterity,
                          int implosion,
                          int headGame,
                          int kick,
                          int passing,
                          Card card) {
        super(name);
        this.setPosition(position);
        this.setSpeed(speed);
        this.setResistance(resistance);
        this.setDexterity(dexterity);
        this.setImplosion(implosion);
        this.setHeadGame(headGame);
        this.setKick(kick);
        this.setPassing(passing);
        this.setCard(card);
        this.setOverallSkill(calcGlobalSkill());
    }

    public void setPosition(Position position) {
        this.position = position;
    }
    public Position getPosition() {
        return this.position;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getSpeed() {
        return this.speed;
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }
    public int getResistance() {
        return this.resistance;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }
    public int getDexterity() {
        return dexterity;
    }

    public void setImplosion(int implosion) {
        this.implosion = implosion;
    }
    public int getImplosion() {
        return implosion;
    }

    public void setHeadGame(int headGame) {
        this.headGame = headGame;
    }
    public int getHeadGame() {
        return headGame;
    }

    public void setKick(int kick) {
        this.kick = kick;
    }
    public int getKick() {
        return kick;
    }

    public void setPassing(int passing) {
        this.passing = passing;
    }
    public int getPassing() {
        return passing;
    }

    public void setCard(Card card) {
        this.card = card;
    }
    public Card getCard() {
        return card;
    }

    private int calcGlobalSkill() {
        int skill = this.getSpeed()      +
                    this.getResistance() +
                    this.getDexterity()  +
                    this.getImplosion()  +
                    this.getHeadGame()   +
                    this.getKick()       +
                    this.getPassing();
        skill /= 7;
        return skill;
    }
}
