package model.football.player;

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

public class GoalkeeperTest {
    private Goalkeeper g1;
    private Goalkeeper g2;

    public GoalkeeperTest() {

    }

    @Before
    public void setUp() {
        g1 = new Goalkeeper("Maria", 1, "Porto", 76, 32, 5564, 54, 78, 98, 54, 12);
        g2 = new Goalkeeper(g1);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void getElasticityTest() {
        int el = 12;
        assertEquals("Same value returned different", el, g1.getElasticity());
        assertEquals("Same value returned different", el, g2.getElasticity());
    }

    @Test
    public void changeElasticityTest() {
        int el = 99;
        g1.changeElasticity(el);
        assertEquals(g1.getElasticity(), el);
    }
    
    @Test
    public void calcOverallSkillTest() {
        assertEquals((int) 44.5, g1.calcOverallSkill());
        assertEquals((int) 44.5, g2.calcOverallSkill());
    }

    @Test
    public void toCSVTest() {
        assertEquals("Different strings","Goalkeeper: Maria;1;Porto;;44;76;32;100;54;78;98;54;NONE;12", g1.toCSV());
    }

    @Test
    public void fromCSVTest() {
        List<String> lines = new ArrayList<>();
        Goalkeeper gTest;

        try {
            g1.save("GoalkeeperTest.csv");
            lines = Files.readAllLines(Paths.get("GoalkeeperTest.csv"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] test = lines.get(0).split(": ", 2);
        gTest = Goalkeeper.fromCSV(test[1]);
        assertTrue(gTest.equals(g1));

    }
}