package model.football.player;

import model.football.foul.Card;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        assertEquals("Equal string returned different", "Lateral: Bernardo;1;Rio Ave;;45;43;13;32;0;78;70;41;NONE;100", l1.toCSV());
        l1.addBackground("AYAYA");
        assertEquals("Equal string returned different", "Lateral: Bernardo;1;Rio Ave;AYAYA;45;43;13;32;0;78;70;41;NONE;100", l1.toCSV());
    }

    @Test
    public void fromCSVTest() {
        l1.setCard(Card.RED);
        Lateral lTest;
        List<String> lines = new ArrayList<>();
        try {
            l1.savePlayer("DefenderTest.csv");
            lines = Files.readAllLines(Paths.get("DefenderTest.csv"), StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] test = lines.get(0).split(": ", 2);
        lTest = l1.fromCSV(test[1]);
        assertTrue(lTest.equals(l1));
    }

}
