package sportm.football.player;

public class Goalkeeper extends FootballPlayer{
    private int elasticity;

    public Goalkeeper
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
                    int elasticity
            )
    {
        super(name, team, speed, resistance, dexterity, implosion, headGame, kick, passing);
        this.changeElasticity(elasticity);
    }

    public Goalkeeper(Goalkeeper goalkeeper) {
        super(goalkeeper);
        this.changeElasticity(goalkeeper.getElasticity());
    }

    public void changeElasticity(int elasticity) {
        setElasticity(elasticity);
        updateOverallSkill();
    }
    private void setElasticity(int elasticity) {
        this.elasticity = elasticity;
    }
    public int getElasticity() {
        return this.elasticity;
    }

    // TODO: Better balance the weights on the skills
    @Override
    public int calcOverallSkill() {
        return (int) ((this.getSpeed() * 0.05)      +
                      (this.getResistance() * 0.15) +
                      (this.getDexterity() * 0.10)  +
                      (this.getImplosion() * 0.05)  +
                      (this.getHeadGame() * 0.05)   +
                      (this.getKick() * 0.05)       +
                      (this.getPassing() * 0.20)    +
                      (this.getElasticity() * 0.30));
    }

    public boolean equals(Goalkeeper goalkeeper) {
        boolean ret = false;

        if(this == goalkeeper)
            ret = true;
        else if(super.equals(goalkeeper) && this.getElasticity() == goalkeeper.getElasticity())
            ret = true;

        return ret;
    }

    @Override
    public Goalkeeper clone() {
        return new Goalkeeper(this);
    }
}
