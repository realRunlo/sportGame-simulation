package football.team.lineup;

import model.football.player.*;
import model.football.team.lineup.FootballLineup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class FootballLineupTest {
    private FootballLineup fl1;
    private FootballLineup fl2;

    private FootballPlayer p1;
    private FootballPlayer p2;
    private FootballPlayer p3;
    private FootballPlayer p4;
    private FootballPlayer p5;

    public FootballLineupTest() {

    }

    @Before
    public void setUp() {
        Set<FootballPlayer> playing = new HashSet<>();
        Set<FootballPlayer> subsitutes = new HashSet<>();

        p1 = new Goalkeeper("Ola", 1, "test", 23, 45, 76, 69, 42, 242, 12, 87);
        p2 = new Defender("def", 2, "test", 65, 487, 42, -31, 42, 86, 42, 49);
        p3 = new Lateral("lat", 3, "test", 32, 87, 42, 64, 56, 32, 46, 74);
        p4 = new Midfielder("mid", 4, "test", 65, 76, 31, 42, 53, 64, 87, 96);
        p5 = new Striker("striker", 5, "test", 52, 46, 24, 14, 6, 64, 46, 78);

        playing.add(p1);
        playing.add(p2);
        playing.add(p3);

        subsitutes.add(p4);
        subsitutes.add(p5);

        fl1 = new FootballLineup("test", 2, playing, subsitutes, new ArrayList<>());
        fl2 = new FootballLineup(fl1);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void addPlayerTest() {
        FootballPlayer newP = new Lateral("latTest", 1, "latTestTeam", 32, 35, 63, 64, 42, 77, 63, 64);
        FootballPlayer dummy1 = new Striker("142", 2, "$", 32, 53, 534, 35, 21, 42, 421, 32);
        FootballPlayer dummy2 = new Midfielder("sf", 3, "rwq", 24, 53, 14, 4, 8, 97, 60, 64);
        FootballPlayer dummy3 = new Midfielder("hrt", 4, "rqew", 67, 68, 24, 5, 86, 90, 50, 75);
        FootballPlayer dummy4 = new Midfielder("wqe", 5, "htr", 54, 97, 34, 46, 8, 79, 29, 86);
        FootballPlayer dummy5 = new Midfielder("tqwe", 6, "we", 643, 67, 44, 76, 64, 56, 27, 27);
        FootballPlayer dummy6 = new Midfielder("1ad", 7, "rwjytj", 42, 5, 61, 64, 66, 34, 76, 75);
        FootballPlayer dummy7 = new Midfielder("tij", 8, "ndq", 39, 49, 17, 19, 20, 47, 59, 49);
        FootballPlayer pFull = new Defender("ola", 9, "haha", 34, 32, 53, 95, 29, 37, 5, 59);

        assertFalse("Before: Player doesn't exist but returns true", fl1.getPlaying().contains(newP));
        fl1.addPlaying(newP);
        assertTrue("After: Player exists but returns false", fl1.getPlaying().contains(newP));

        assertEquals("Size should be 4 but is not", fl1.getPlaying().size(), 4);
        fl1.addPlaying(newP);
        assertEquals("Size should be 4 but is not", fl1.getPlaying().size(), 4);

        fl1.addPlaying(dummy1);
        fl1.addPlaying(dummy2);
        fl1.addPlaying(dummy3);
        fl1.addPlaying(dummy4);
        fl1.addPlaying(dummy5);
        fl1.addPlaying(dummy6);
        fl1.addPlaying(dummy7);

        assertFalse("Before: Player doesn't exist but returns true", fl1.getPlaying().contains(pFull));
        fl1.addPlaying(pFull);
        assertFalse("After: Player shouldn't exist but returns true", fl1.getPlaying().contains(pFull));
    }

    @Test
    public void addSubstituteTest() {
        FootballPlayer newP = new Goalkeeper("yay", 43, "bomdia", 24, 53, 6, 60, 49, 48, 31, 44);
        FootballPlayer newPCpy = new Goalkeeper((Goalkeeper) newP);

        assertFalse("Before: Player wasn't added but returns true", fl1.getSubstitutes().contains(newP));
        fl1.addSubstitute(newP);
        assertTrue("After: Player was added but returns false", fl1.getSubstitutes().contains(newP));

        fl1.addSubstitute(newPCpy);
        assertEquals("Duplicate element added", fl1.getSubstitutes().size(), 4);
    }

    @Test
    public void substitutePlayerTest() {
        assertTrue("Before: Player exists in playing but returns false", fl1.getPlaying().contains(p1));
        assertFalse("Before: Player doesn't exist in playing but returns true", fl1.getPlaying().contains(p5));
        assertTrue("Before: Player exists in playing but returns false", fl1.getSubstitutes().contains(p5));
        assertFalse("Before: Player doesn't exist in playing but returns true", fl1.getSubstitutes().contains(p1));
        fl1.substitutePlayer(p1, p5);
        assertFalse("After: Player exists in playing but returns false", fl1.getPlaying().contains(p1));
        assertTrue("After: Player doesn't exist in playing but returns true", fl1.getPlaying().contains(p5));
        assertFalse("After: Player exists in playing but returns false", fl1.getSubstitutes().contains(p5));
        assertTrue("After: Player doesn't exist in playing but returns true", fl1.getSubstitutes().contains(p1));
    }

    @Test
    public void calcGlobalSkillTest() {
        assertEquals("Equal value returns different", (int) 62, fl1.calcGlobalSkill());
        assertEquals("Equal value returns different", (int) 62, fl2.calcGlobalSkill());
    }
}