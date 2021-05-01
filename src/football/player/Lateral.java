package football.player;

public class Lateral extends FootballPlayer {
    private int crossing;

    public Lateral
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
                    int crossing
            )
    {
        super(name, team, speed, resistance, dexterity, implosion, headGame, kick, passing);
        changeCrossing(crossing);
    }

    public Lateral(Lateral lateral) {
        super(lateral);
        changeCrossing(lateral.getCrossing());
    }

    private void setCrossing(int crossing) {
        if(crossing > 100) crossing = 100;
        else if(crossing < 0) crossing = 0;
        this.crossing = crossing;
    }
    public int getCrossing() {
        return this.crossing;
    }
    public void changeCrossing(int crossing) {
       setCrossing(crossing);
       updateOverallSkill();
    }

    @Override
    public int calcOverallSkill() {
        return (int) ((this.getSpeed() * 0.15)      +
                (this.getResistance() * 0.15) +
                (this.getDexterity() * 0.10)  +
                (this.getImplosion() * 0.10)  +
                (this.getHeadGame() * 0.05)   +
                (this.getKick() * 0.10)       +
                (this.getPassing() * 0.20)    +
                (this.getCrossing() * 0.15));
    }

    public boolean equals(Lateral lateral) {

        boolean ret = false;

        if(this == lateral)
            ret = true;
        else if(super.equals(lateral) && this.getCrossing() == lateral.getCrossing())
            ret = true;

        return ret;
    }

    @Override
    public Lateral clone() {
        return new Lateral(this);
    }
}
