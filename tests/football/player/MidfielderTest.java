package football.player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MidfielderTest {
    Midfielder m1;
    Midfielder m2;

    public MidfielderTest() {

    }

    @Before
    public void setUp() {
        m1 = new Midfielder("OLA", "ADEUS", 32, -13, 440, 78, 89,13, 89, 87);
        m2 = new Midfielder(m1);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void getBallRecoveryTest() {
        assertEquals("Same value returned not equals", 87, m1.getBallRecovery());
        assertNotEquals("Diferent value returned equals", 2, m1.getBallRecovery());
    }

    @Test
    public void changeBallRecoveryTest() {
        int recovery = 55;
        assertNotEquals("Wrong value returned equals before change", recovery, m1.getBallRecovery());
        m1.changeBallRecovery(recovery);
        assertEquals("Value returned different after change", recovery, m1.getBallRecovery());
    }

    @Test
    public void calcOverallSkillTest() {
        assertEquals("Equal value returned different", (int) 56.25, m1.calcOverallSkill());
    }
}
