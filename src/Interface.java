import football.Game.FootballGame;
import football.player.Defender;
import football.player.Goalkeeper;
import football.player.Midfielder;
import football.player.Striker;
import football.team.FootballTeam;
import generic.Game.Game;
import viewer.SPORTMViewer;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Interface {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SPORTMViewer viewer = new SPORTMViewer();
        viewer.showWelcome();
        State s = new State();
        FootballTeam t1 = new FootballTeam();
        FootballTeam t2 = new FootballTeam();
        t1.setName("Team1");
        t2.setName("Team2");
        int j = 0;
        for(int i = 0; i<10; i++){
            if(i>=5) j = 1;
            Goalkeeper g = new Goalkeeper("Goalie" + i, "Team" + j, 1,1,1,1,1,1,1,1);
            s.addPlayer(g);
            if(i>=5) t2.addPlayer(g);
            else t1.addPlayer(g);
        }
        j = 0;
        for(int i = 0; i<10; i++){
            if(i>=5) j = 1;
            Defender d = new Defender("Defender" + i, "Team" + j,1,1,1,1,1,1,1);
            s.addPlayer(d);
            if(i>=5) t2.addPlayer(d);
            else t1.addPlayer(d);
        }
        for(int i = 0; i<10; i++){
            if(i>=5) j = 1;
            Midfielder m = new Midfielder("Midfielder" + i, "Team" + j,1,1,1,1,1,1,1,1);
            s.addPlayer(m);
            if(i>=5) t2.addPlayer(m);
            else t1.addPlayer(m);
        }
        for(int i = 0; i<10; i++){
            if(i>=5) j = 1;
            Striker st = new Striker("Striker" + i, "Team" + j,1,1,1,1,1,1,1);
            s.addPlayer(st);
            if(i>=5) t2.addPlayer(st);
            else t1.addPlayer(st);
        }
        s.addTeam(t1);s.addTeam(t2);
        FootballGame game = new FootballGame(s.getTeam("Team1"),1,s.getTeam("Team2"),2);
        System.out.println(s.toString());
        System.out.println(game.toString());
        s.addGame(game);
        try{
            s.saveState("estado.txt");
        }
        catch(FileNotFoundException e){
            System.out.println("File not found.");
        }
        catch(IOException e){
            System.out.println("Error accessing the file");
        }

        System.out.println("Reading State file...\nNew State:\n");
        readFile r = new readFile();
        try{
             State s2 = r.readState("estado.txt");
             System.out.println(s2.toString());
             System.out.println(s2.getTeam("Team1").toString());
             System.out.println(s2.getTeam("Team2").toString());
            System.out.println(s2.getGameHistory().get(s2.getGameHistory().size()-1).toString());
        }
        catch(ClassNotFoundException e){
            System.out.println("Class not found");
        }
        catch(IOException e){
            System.out.println("Error accessing the file");
            e.printStackTrace();
        }


        viewer.showTermination();

        }
}
