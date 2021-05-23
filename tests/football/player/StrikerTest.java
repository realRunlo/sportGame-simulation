package football.player;

import model.football.player.Defender;
import model.football.player.Striker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StrikerTest {
    private Striker s1;
    private Striker s2;

    public StrikerTest() {

    }

    @Before
    public void setUp() {
        s1 = new Striker("João", 1, "Trofense", 321, -12, 76, 45, 56, 32, 76, 32);
        s2 = new Striker(s1);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void calcOverallSkillTest() {
        assertEquals("Same value returned not equals", (int) 77, s1.calcOverallSkill());
        assertEquals("Same value returned not equals", (int) 77, s2.calcOverallSkill());
    }
    @Test
    public void toCSVTest() {
        assertEquals("Equal players returned different", "Striker: João;1;Trofense;;100;0;76;45;56;32;76;NONE;32", s1.toCSV());
    }

    @Test
    public void loadSaveTest() throws IOException {
        String filePath = "striker.csv";
        s1.save(filePath);
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String[] split = br.readLine().split(": ");
        Striker sTest = Striker.load(split[1]);

        assertTrue("Different defenders", s1.equals(sTest));
    }
}
