package football.player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sportm.football.player.Defender;

import static org.junit.Assert.assertEquals;

public class DefenderTest {
    Defender d1;
    Defender d2;

    public DefenderTest() {

    }

    @Before
    public void setUp() {
        d1 = new Defender("António", "Chelsea", 42, 12, 144, -10, 32, 144, 42);
        d2 = new Defender(d1);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void calcOverallSkill() {
        assertEquals("Wrong overallSkill", (int) 45.5, d1.calcOverallSkill());
        assertEquals("Wrong overallSkill", (int) 45.5, d2.calcOverallSkill());
    }
}
