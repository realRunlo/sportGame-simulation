package football.player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StrikerTest {
    private Striker s1;
    private Striker s2;

    public StrikerTest() {

    }

    @Before
    public void setUp() {
        s1 = new Striker("João", "Trofense", 321, -12, 76, 45, 56, 32, 76);
        s2 = new Striker(s1);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void calcOverallSkillTest() {
        assertEquals("Same value returned not equals", (int) 51.1, s1.calcOverallSkill());
        assertEquals("Same value returned not equals", (int) 51.1, s2.calcOverallSkill());
    }
}
