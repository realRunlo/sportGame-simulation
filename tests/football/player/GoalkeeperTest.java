package football.player;

import model.football.player.Defender;
import model.football.player.Goalkeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
        assertEquals("Different strings","Goalkeeper: Maria;1;Porto;;76;32;100;54;78;98;54;NONE;12\n", g1.toCSV());
    }

    @Test
    public void loadSaveTest() throws IOException {
        String filePath = "goalkeeper.csv";
        g1.save(filePath);
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String[] split = br.readLine().split(": ");
        Goalkeeper gTest = Goalkeeper.load(split[1]);

        assertTrue("Different goalkeepers", g1.equals(gTest));
    }
}