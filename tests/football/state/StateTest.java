package football.state;

import model.football.player.*;
import model.football.state.FootballState;
import model.football.team.FootballTeam;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StateTest {
    FootballState fs1;
    FootballState fs2;
    FootballState fs3;

    public StateTest() {}

    @Before
    public void setUp() {
        Map<String, FootballPlayer> players = new HashMap<>();
        Map<String, FootballTeam> teams = new HashMap<>();

        FootballPlayer dummy0 = new Lateral("latTest", 1, "t1", 32, 35, 63, 64, 42, 77, 63, 64);
        FootballPlayer dummy1 = new Striker("142", 2, "t1", 32, 53, 534, 35, 21, 42, 421, 32);
        FootballPlayer dummy2 = new Midfielder("sf", 3, "t1", 24, 53, 14, 4, 8, 97, 60, 64);
        FootballPlayer dummy3 = new Midfielder("hrt", 4, "t2", 67, 68, 24, 5, 86, 90, 50, 75);
        FootballPlayer dummy4 = new Midfielder("wqe", 5, "t2", 54, 97, 34, 46, 8, 79, 29, 86);
        FootballPlayer dummy5 = new Midfielder("tqwe", 6, "t2", 643, 67, 44, 76, 64, 56, 27, 27);
        FootballPlayer dummy6 = new Midfielder("1ad", 7, "t3", 42, 5, 61, 64, 66, 34, 76, 75);
        FootballPlayer dummy7 = new Midfielder("tij", 8, "t3", 39, 49, 17, 19, 20, 47, 59, 49);
        FootballPlayer dummy8 = new Defender("ola", 9, "t3", 34, 32, 53, 95, 29, 37, 5, 59);

        FootballTeam t1 = new FootballTeam("t1");
        FootballTeam t2 = new FootballTeam("t2");
        FootballTeam t3 = new FootballTeam("t3");

        players.put(dummy0.getName(), dummy0);
        players.put(dummy1.getName(), dummy1);
        players.put(dummy2.getName(), dummy2);
        players.put(dummy3.getName(), dummy3);
        players.put(dummy4.getName(), dummy4);
        players.put(dummy5.getName(), dummy5);
        players.put(dummy6.getName(), dummy6);
        players.put(dummy7.getName(), dummy7);
        players.put(dummy8.getName(), dummy8);

        t1.addPlayer(dummy0);
        t1.addPlayer(dummy1);
        t1.addPlayer(dummy2);
        t2.addPlayer(dummy3);
        t2.addPlayer(dummy4);
        t2.addPlayer(dummy5);
        t3.addPlayer(dummy6);
        t3.addPlayer(dummy7);
        t3.addPlayer(dummy8);

        teams.put(t1.getName(), t1);
        teams.put(t2.getName(), t2);
        teams.put(t3.getName(), t3);

        fs1 = new FootballState();
        fs2 = new FootballState(players, teams, 9, 3, 23);
        fs3 = fs2.clone();
    }

    @After
    public void tearDown() {}

    @Test
    public void toCSVTest() {
        assertEquals("Different csvs", "FootballState: 23\n", fs2.toCSV());
    }

    @Test
    public void saveLoadTest() throws IOException {
        String filePath = "footballstate.csv";

        fs2.save(filePath);

        FootballState fTest = FootballState.load(filePath);

        assertTrue("Different FootballStates", fs2.equals(fTest));
    }
}
