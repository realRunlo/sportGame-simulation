package football;

import model.football.game.FootballGame;
import model.football.player.*;
import model.football.team.FootballTeam;
import model.football.team.lineup.FootballLineup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FootballGameTest {
    private FootballGame fg1;
    private FootballGame fg2;
    private FootballGame fg3;
    private FootballLineup fl1;
    private FootballLineup fl2;

    public FootballGameTest() {

    }

    @Before
    public void setUp() {
        fl1 = new FootballLineup();
        fl2 = new FootballLineup();

        FootballPlayer p1 = new Goalkeeper("Ola", 1, "test", 23, 45, 76, 69, 42, 242, 12, 87);
        FootballPlayer p2 = new Defender("def", 2, "defTeam", 65, 487, 42, -31, 42, 86, 42, -32);
        FootballPlayer p3 = new Lateral("lat", 3, "latTeam", 32, 87, 42, 64, 56, 32, 46, 74);
        FootballPlayer p4 = new Midfielder("mid", 4, "midTeam", 65, 76, 31, 42, 53, 64, 87, 96);
        FootballPlayer p5 = new Striker("striker", 5, "strikeTeam", 52, 46, 24, 14, 6, 64, 46, 1032);

        fl1.addPlaying(p1);
        fl1.addPlaying(p2);
        fl1.addPlaying(p3);
        fl1.addSubstitute(p4);
        fl1.addSubstitute(p5);

        fl2.addPlaying(p5);
        fl2.addPlaying(p4);
        fl2.addSubstitute(p3);
        fl2.addSubstitute(p2);
        fl2.addSubstitute(p1);

        fg1 = new FootballGame(new FootballTeam(), new FootballTeam());
        fg2 = new FootballGame(new FootballTeam(),new FootballTeam(), fl1, fl2);
        fg3 = new FootballGame(fg2);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void equalsTest() {
        FootballGame fgT = new FootballGame(new FootballTeam(), new FootballTeam());
        assertTrue(fg1.equals(fgT));
        assertFalse(fg2.equals(fgT));
    }

    @Test
    public void setHomeTest() {
        assertFalse("Before: Elements were supposed to be different but came out true", fg1.getHome().equals(fl1));
        fg1.setHome(fl1);
        assertTrue("After: Elements were supposed to be the same but came out false", fg1.getHome().equals(fl1));
    }

    @Test
    public void getHomeTest() {
        FootballLineup fl1 = new FootballLineup();

        assertFalse("Before: Elements were supposed to be different but came out true", fg2.getHome().equals(fl1));
        fl1 = fg2.getHome();
        assertTrue("After: Elements were supposed to be the same but came out false", fg2.getHome().equals(fl1));
    }

    @Test
    public void setAwayTest() {
        assertFalse("Before: Elements were supposed to be different but came out true", fg1.getAway().equals(fl1));
        fg1.setAway(fl1);
        assertTrue("After: Elements were supposed to be the same but came out false", fg1.getAway().equals(fl1));
    }

    @Test
    public void getAwayTest() {
        FootballLineup fl1 = new FootballLineup();

        assertFalse("Before: Elements were supposed to be different but came out true", fg2.getAway().equals(fl1));
        fl1 = fg2.getAway();
        assertTrue("After: Elements were supposed to be the same but came out false", fg2.getAway().equals(fl1));
    }
}
