package football.player;

import model.football.player.Defender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
        d1 = new Defender("António", 1, "Chelsea", 42, 12, 144, -10, 32, 144, 42, 999);
        d2 = new Defender(d1);
        d3 = new Defender("", 0, "", 0, 0, 0, 0, 0, 0, 0, 0);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void calcOverallSkill() {
        assertEquals("Wrong overallSkill", (int) 57, d1.calcOverallSkill());
        assertEquals("Wrong overallSkill", (int) 57, d2.calcOverallSkill());
    }

    @Test
    public void equalsTest() {
        assertTrue("Different defenders", d1.equals(d2));
    }

    @Test
    public void toCSVTest() {
        assertEquals("Equal players returned different", "Defesa:António;1;Chelsea;;42;12;100;0;32;100;42;NONE;100\n", d1.toCSV());
    }

    @Test
    public void loadSaveTest() throws IOException {
        String filePath = "defender.csv";
        d1.save(filePath);
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String[] split = br.readLine().split(":");
        Defender dTest = Defender.load(split[1], "Chelsea", false);

        assertTrue("Different defenders", d1.equals(dTest));
    }
}
