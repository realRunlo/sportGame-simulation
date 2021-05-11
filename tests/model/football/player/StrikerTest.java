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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StrikerTest {
    private Striker s1;
    private Striker s2;

    public StrikerTest() {

    }

    @Before
    public void setUp() {
        s1 = new Striker("João", 1, "Trofense", 321, -12, 76, 45, 56, 32, 76);
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

    @Test
    public void toCSVTest() {
        assertEquals("Equal players returned different", "Striker: João;1;Trofense;;51;100;0;76;45;56;32;76;NONE", s1.toCSV());
    }

    @Test
    public void fromCSVTest() {
        s1.setCard(Card.RED);
        Striker sTest;
        List<String> lines = new ArrayList<>();
        try {
            s1.save("DefenderTest.csv");
            lines = Files.readAllLines(Paths.get("DefenderTest.csv"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] test = lines.get(0).split(": ", 2);
        sTest = Striker.fromCSV(test[1]);
        assertTrue("Same striker returned not equal", sTest.equals(s1));
    }
}
