package model.football.player;

import model.football.foul.Card;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        assertEquals("Equal players returned different", "Midfielder: OLA;1;ADEUS;;56;32;0;100;78;89;13;89;NONE;87", m1.toCSV());
    }

    @Test
    public void fromCSVTest() {
        m1.setCard(Card.RED);
        Midfielder mTest;
        List<String> lines = new ArrayList<>();
        try {
            m1.save("DefenderTest.csv");
            lines = Files.readAllLines(Paths.get("DefenderTest.csv"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] test = lines.get(0).split(": ", 2);
        mTest = Midfielder.fromCSV(test[1]);
        assertTrue("Same midfielder returned not equal", mTest.equals(m1));
    }
}
