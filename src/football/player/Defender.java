package football.player;

public class Defender extends FootballPlayer {
    private int ballRetention;

    public Defender
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
                    int ballRetention
            ) {
        super(name, team, speed, resistance, dexterity, implosion, headGame, kick, passing);
        this.ballRetention = ballRetention;
    }

    public Defender(Defender defender) {
        super(defender);
        ballRetention = defender.getBallRetention();
    }

    // TODO: Better balance the weights on the skills
    @Override
    public int calcOverallSkill() {
        return (int) (
                (this.getSpeed() * 0.15) +
                        (this.getResistance() * 0.15) +
                        (this.getDexterity() * 0.10) +
                        (this.getImplosion() * 0.05) +
                        (this.getHeadGame() * 0.15) +
                        (this.getKick() * 0.10) +
                        (this.getPassing() * 0.30)
        );
    }

    public int getBallRetention(){
        return ballRetention;
    }

    public void setBallRetention(int x){
        ballRetention = x;
    }

    public boolean equals(Defender defender) {
        boolean ret = false;

        if (this == defender)
            ret = true;
        else if (super.equals(defender))
            ret = true;

        return ret;
    }

    @Override
    public Defender clone() {
        return new Defender(this);
    }
}
