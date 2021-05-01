package football.player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GoalkeeperTest {
    private Goalkeeper g1;
    private Goalkeeper g2;

    public GoalkeeperTest() {

    }

    @Before
    public void setUp() {
        g1 = new Goalkeeper("Maria", "Porto", 76, 32, 5564, 54, 78, 98, 54, 12);
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
}