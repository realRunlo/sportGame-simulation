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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DefenderTest {
    private Defender d1;
    private Defender d2;
    private Defender d3;

    public DefenderTest() {

    }

    @Before
    public void setUp() {
        d1 = new Defender("António", 1, "Chelsea", 42, 12, 144, -10, 32, 144, 42);
        d2 = new Defender(d1);
        d3 = new Defender("", 2, "",  0, 0, 0, 0, 0, 0, 0);
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

    @Test
    public void toCSVTest() {
        assertEquals("Equal players returned different", "Defender: António;1;Chelsea;;45;42;12;0;100;32;100;42;NONE;", d1.toCSV());
    }

    @Test
    public void fromCSVTest() {
        d1.setCard(Card.RED);
        Defender dTest;
        List<String> lines = new ArrayList<>();
        try {
            d1.savePlayer("DefenderTest.csv");
            lines = Files.readAllLines(Paths.get("DefenderTest.csv"), StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] test = lines.get(0).split(": ", 2);
        dTest = d1.fromCSV(test[1]);
        assertTrue("Same defender returned not equal", dTest.equals(d1));
    }
}
