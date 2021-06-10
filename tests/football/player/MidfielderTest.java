package football.player;

import model.football.player.Midfielder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class MidfielderTest {
    private Midfielder m1;
    private Midfielder m2;

    public MidfielderTest() {

    }

    @Before
    public void setUp() {
        m1 = new Midfielder("OLA", 1, "ADEUS", 32, -13, 440, 78, 89,13, 89, 87);
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

    @Test
    public void toCSVTest() {
        assertEquals("Equal players returned different", "Medio:OLA;1;ADEUS;;32;0;100;78;89;13;89;NONE;87\n", m1.toCSV());
    }

    @Test
    public void loadSaveTest() throws IOException {
        String filePath = "midfielder.csv";
        m1.save(filePath);
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String[] split = br.readLine().split(":");
        Midfielder mTest = Midfielder.load(split[1], "ADEUS", false);

        assertTrue("Different midfielders", m1.equals(mTest));
    }
}
