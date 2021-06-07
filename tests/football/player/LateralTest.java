package football.player;

import model.football.player.Defender;
import model.football.player.Lateral;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class LateralTest {
    private Lateral l1;
    private Lateral l2;

    public LateralTest() {

    }

    @Before
    public void setUp() {
        l1 = new Lateral("Bernardo", 1, "Rio Ave", 43, 13, 32, -32, 78, 70, 41, 9842);
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

    @Test
    public void toCSVTest() {
        assertEquals("Equal string returned different", "Lateral: Bernardo;1;Rio Ave;;43;13;32;0;78;70;41;NONE;100\n", l1.toCSV());
        l1.addBackground("AYAYA");
        assertEquals("Equal string returned different", "Lateral: Bernardo;1;Rio Ave;AYAYA;43;13;32;0;78;70;41;NONE;100\n", l1.toCSV());
    }

    @Test
    public void loadSaveTest() throws IOException {
        String filePath = "lateral.csv";
        l1.save(filePath);
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String[] split = br.readLine().split(": ");
        Lateral lTest = Lateral.load(split[1], "Rio Ave", false);

        assertTrue("Different laterals", l1.equals(lTest));
    }
}
