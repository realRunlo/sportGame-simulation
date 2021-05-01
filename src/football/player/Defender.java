package football.player;

public class Defender extends FootballPlayer {
    // Don't know what skill to add here

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
                    int passing
            ) {
        super(name, team, speed, resistance, dexterity, implosion, headGame, kick, passing);
    }

    public Defender(Defender defender) {
        super(defender);
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
