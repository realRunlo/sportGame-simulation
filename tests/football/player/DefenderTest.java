package football.player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefenderTest {
    private Defender d1;
    private Defender d2;
    private Defender d3;

    public DefenderTest() {

    }

    @Before
    public void setUp() {
        d1 = new Defender("António", "Chelsea", 42, 12, 144, -10, 32, 144, 42);
        d2 = new Defender(d1);
        d3 = new Defender("", "", 0, 0, 0, 0, 0, 0, 0);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void calcOverallSkill() {
        assertEquals("Wrong overallSkill", (int) 45.5, d1.calcOverallSkill());
        assertEquals("Wrong overallSkill", (int) 45.5, d2.calcOverallSkill());
    }
/*
    @Test
    public void loadPlayerTest() {
        d3.loadPlayer();
        assertTrue("Different players", d3.equals(d1));
    }*/
}
