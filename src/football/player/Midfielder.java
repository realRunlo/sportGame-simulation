package football.player;

public class Midfielder extends FootballPlayer {
    private int ballRecovery;

    public Midfielder
            (
                    String name,
                    String team,
                    int speed,
                    int resistance,
                    int dexterity,
                    int implosion,
                    int headGame,
                    int kick,
                    int passing,
                    int ballRecovery
            )
    {
        super(name, team, speed, resistance, dexterity, implosion, headGame, kick, passing);
        this.changeBallRecovery(ballRecovery);
    }

    public Midfielder(Midfielder midfielder) {
        super(midfielder);
        this.changeBallRecovery(midfielder.getBallRecovery());
    }

    public void changeBallRecovery(int ballRecovery) {
        this.setBallRecovery(ballRecovery);
        updateOverallSkill();
    }

    private void setBallRecovery(int ballRecovery) {
        if(ballRecovery > 100) ballRecovery = 100;
        else if(ballRecovery < 0) ballRecovery = 0;
        this.ballRecovery = ballRecovery;
    }

    public int getBallRecovery() {
        return this.ballRecovery;
    }

    // TODO: Better balance the weights on the skills
    @Override
    public int calcOverallSkill() {
        return (int) ((this.getSpeed() * 0.20) +
                (this.getResistance() * 0.15) +
                (this.getDexterity() * 0.05) +
                (this.getImplosion() * 0.05) +
                (this.getHeadGame() * 0.10) +
                (this.getKick() * 0.10) +
                (this.getPassing() * 0.15) +
                (this.getBallRecovery() * 0.20));
    }

    public boolean equals(Midfielder midfielder) {
        boolean ret = false;

        if (this == midfielder)
            ret = true;
        else if (super.equals(midfielder) && this.getBallRecovery() == midfielder.getBallRecovery())
            ret = true;

        return ret;
    }

    @Override
    public Midfielder clone() {
        return new Midfielder(this);
    }
}
