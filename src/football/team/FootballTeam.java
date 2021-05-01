package football.team;
import football.player.FootballPlayer;
import generic.team.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FootballTeam extends Team{
    private Map<String, FootballPlayer> players;

    public FootballTeam(){
        super();
        players = new HashMap<>();
    }

    public FootballTeam(String name, int GlobalSkill, Map<String, FootballPlayer> newPlayers){
        super(name,GlobalSkill);
        players = newPlayers.entrySet().stream().collect(Collectors.toMap(e->getKey(),e->e.getValue().clone()));
    }
}
