package sportm.football.player;

public class Striker extends FootballPlayer {
    // TODO: Add instance variable for this class

    public Striker
            (
                    String name,
                    String team,
                    int speed,
                    int resistance,
                    int dexterity,
                    int implosion,
                    int headGame,
                    int kick,
                    int passing
            )
    {
        super(name, team, speed, resistance, dexterity, implosion, headGame, kick, passing);
        // TODO: Put changeVariableName() here
    }

    public Striker(Striker striker) {
        super(striker);
        // TODO: Also here
    }

    // TODO: Better balance the weights on the skills
    @Override
    public int calcOverallSkill() {
        return (int) (
                (this.getSpeed() * 0.15)      +
                (this.getResistance() * 0.15) +
                (this.getDexterity() * 0.10)  +
                (this.getImplosion() * 0.10)  +
                (this.getHeadGame() * 0.15)   +
                (this.getKick() * 0.25)       +
                (this.getPassing() * 0.10)
        );
    }

    public boolean equals(Striker striker) {
        boolean ret = false;

        if (this == striker)
            ret = true;
        else if (super.equals(striker))
            ret = true;

        return ret;
    }

    @Override
    public Striker clone() {
        return new Striker(this);
    }
}