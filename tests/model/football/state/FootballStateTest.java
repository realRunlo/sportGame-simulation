package model.football.state;

import controller.errors.NoFootballStateFoundException;
import model.ReadFile;
import model.football.Game.FootballGame;
import model.football.player.Defender;
import model.football.player.FootballPlayer;
import model.football.player.Goalkeeper;
import model.football.team.FootballTeam;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FootballStateTest {
    private FootballState fs1;
    private FootballState fs2;
    private FootballState fs3;

    public FootballStateTest() {}

    @Before
    public void setUp() {
        Map<String, FootballPlayer> players = new HashMap<>();
        Map<String, FootballTeam> teams = new HashMap<>();
        List<FootballGame> games = new ArrayList<>();

        FootballPlayer p1 = new Defender("ola", 23, "adeus", 45, 1, 87, 43, 54, 32, 54);
        FootballPlayer p2 = new Goalkeeper("weq", 87, "lol", 32, 42, 12, 45, 65, 56, 34, 43);

        players.put(p1.getName(), p1.clone());
        Map<String, FootballPlayer> adeus = new HashMap<>();
        Map<String, FootballPlayer> lol = new HashMap<>();
        adeus.put(p1.getName(), p1);
        lol.put(p2.getName(), p2);
        players.put(p2.getName(), p2.clone());

        FootballTeam t1 = new FootballTeam("adeus", 23, adeus, 1);
        FootballTeam t2 = new FootballTeam("lol", 87, lol, 1);

        teams.put(t1.getName(), t1);
        teams.put(t2.getName(), t2);

        fs1 = new FootballState();
        fs2 = new FootballState(players, teams, games, 2, 2, 0);
        fs3 = new FootballState(fs2);
    }

    @After
    public void tearDown() {}

    @Test
    public void toCSVTest() {
        assertEquals
                (
                        "Different CSVs",
                        """
                                FootballState: 2;2;0
                                FootballTeam: adeus;23;1
                                FootballTeam: lol;87;1
                                Goalkeeper: weq;87;lol;;37;32;42;12;45;65;56;34;NONE;43
                                Defender: ola;23;adeus;;45;45;1;87;43;54;32;54;NONE
                                """,
                        fs2.toCSV()
                );
    }

    @Test
    public void saveLoadFileTest() {
        FootballState fTest = new FootballState();
        try {
            ReadFile.saveState(fs2, "fs2.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fTest = ReadFile.loadFile("fs2.txt");
        } catch (NoFootballStateFoundException e) {
            e.printStackTrace();
        }
        assertTrue("Different States", fTest.equals(fs2));
    }
}
