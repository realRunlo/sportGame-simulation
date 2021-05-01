package sportm.football.player;

import sportm.football.foul.Card;
import sportm.generic.player.Player;

import java.util.List;

public abstract class FootballPlayer extends Player {
    int speed;
    int resistance;
    int dexterity;
    int implosion;
    int headGame;
    int kick;
    int passing;
    Card card;

    public FootballPlayer(String name, String team) {
        super(name, team);
        this.setSpeed(0);
        this.setResistance(0);
        this.setDexterity(0);
        this.setImplosion(0);
        this.setHeadGame(0);
        this.setKick(0);
        this.setPassing(0);
        this.setCard(Card.NONE);
    }

    public FootballPlayer(String name,
                          String team,
                          int speed,
                          int resistance,
                          int dexterity,
                          int implosion,
                          int headGame,
                          int kick,
                          int passing) {
        super(name, team);
        this.changeSpeed(speed);
        this.changeResistance(resistance);
        this.changeDexterity(dexterity);
        this.changeImplosion(implosion);
        this.changeHeadGame(headGame);
        this.changeKick(kick);
        this.changePassing(passing);
        this.setCard(Card.NONE);
    }

    public FootballPlayer(String name,
                          String team,
                          List<String> background,
                          int speed,
                          int resistance,
                          int dexterity,
                          int implosion,
                          int headGame,
                          int kick,
                          int passing) {
        super(name, team, background);
        this.changeSpeed(speed);
        this.changeResistance(resistance);
        this.changeDexterity(dexterity);
        this.changeImplosion(implosion);
        this.changeHeadGame(headGame);
        this.changeKick(kick);
        this.changePassing(passing);
        this.setCard(Card.NONE);
    }

    public FootballPlayer(FootballPlayer player) {
        super(player);
        this.changeSpeed(player.getSpeed());
        this.changeResistance(player.getResistance());
        this.changeDexterity(player.getDexterity());
        this.changeImplosion(player.getImplosion());
        this.changeHeadGame(player.getHeadGame());
        this.changeKick(player.getKick());
        this.changePassing(player.getPassing());
        this.setCard(player.getCard());
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getSpeed() {
        return this.speed;
    }
    public void changeSpeed(int speed) {
        this.setSpeed(speed);
        this.updateOverallSkill();
    }

    public void setResistance(int resistance) {
        this.resistance = resistance;
    }
    public int getResistance() {
        return this.resistance;
    }
    public void changeResistance(int resistance) {
        this.setResistance(resistance);
        this.updateOverallSkill();
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }
    public int getDexterity() {
        return dexterity;
    }
    public void changeDexterity(int dexterity) {
        this.setDexterity(dexterity);
        this.updateOverallSkill();
    }

    public void setImplosion(int implosion) {
        this.implosion = implosion;
    }
    public int getImplosion() {
        return implosion;
    }
    public void changeImplosion(int implosion) {
        this.setImplosion(implosion);
        this.updateOverallSkill();
    }

    public void setHeadGame(int headGame) {
        this.headGame = headGame;
    }
    public int getHeadGame() {
        return headGame;
    }
    public void changeHeadGame(int headGame) {
        this.setHeadGame(headGame);
        this.updateOverallSkill();
    }

    public void setKick(int kick) {
        this.kick = kick;
    }
    public int getKick() {
        return kick;
    }
    public void changeKick(int kick) {
        this.setKick(kick);
        this.updateOverallSkill();
    }

    public void setPassing(int passing) {
        this.passing = passing;
    }
    public int getPassing() {
        return passing;
    }
    public void changePassing(int passing) {
        this.setPassing(passing);
        this.updateOverallSkill();
    }

    public void setCard(Card card) {
        this.card = card;
    }
    public Card getCard() {
        return card;
    }

    public boolean equals(FootballPlayer player) {
        boolean ret = false;

        if(this == player)
            ret = true;
        else if
        (
                super.equals(player)                           &&
                this.getSpeed()      == player.getSpeed()      &&
                this.getResistance() == player.getResistance() &&
                this.getDexterity()  == player.getDexterity()  &&
                this.getImplosion()  == player.getImplosion()  &&
                this.getHeadGame()   == player.getHeadGame()   &&
                this.getKick()       == player.getKick()       &&
                this.getPassing()    == player.getPassing()    &&
                this.getCard()       == player.getCard()
        ) ret = true;

        return ret;
    }

}
