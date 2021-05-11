package model.football.team;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FootballTeamTest {
    FootballTeam t1;
    FootballTeam t2;

    public FootballTeamTest() {}

    @Before
    public void SetUp() {
        t1 = new FootballTeam("Benfica", 76, new HashMap<>(), 0);
        t2 = new FootballTeam(t1);
    }

    @After
    public void tearDown() {}

    @Test
    public void toCSVTest() {
        assertEquals("Same players returned different", "Benfica;76;0", t1.toCSV());
    }

    @Test
    public void saveTeamTest() {
        List<String> lines;
        try {
            t1.saveTeam("FootballTeam.csv");
            lines = Files.readAllLines(Paths.get("FootballTeam.csv"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        FootballTeam tTest = FootballTeam.fromCSV(lines.get(0));
        assertEquals("Same class returned not equals", t1, tTest);
    }
}
