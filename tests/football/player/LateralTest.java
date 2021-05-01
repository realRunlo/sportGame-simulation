package football.player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class LateralTest {
    private Lateral l1;
    private Lateral l2;

    public LateralTest() {

    }

    @Before
    public void setUp() {
        l1 = new Lateral("Bernardo", "Rio Ave", 43, 13, 32, -32, 78, 70, 41, 9842);
        l2 = new Lateral(l1);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void getCrossingTest() {
        assertEquals("Same value returned different", 100, l1.getCrossing());
        assertEquals("Same value returned different", 100, l2.getCrossing());
    }

    @Test
    public void changeCrossingTest() {
        int crossing = 69;
        assertNotEquals("Different value returned equals", crossing, l1.getCrossing());
        l1.changeCrossing(crossing);
        assertEquals("Same value returned different", crossing, l1.getCrossing());
    }

    @Test
    public void calcOverallSkillTest() {
        assertEquals("Same value returned not equals", (int) 45.7, l1.calcOverallSkill());
        assertEquals("Same value returned not equals", (int) 45.7, l2.calcOverallSkill());
    }
}
