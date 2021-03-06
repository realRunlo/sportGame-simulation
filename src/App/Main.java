package App;

import controller.SPORTMController;
import model.exceptions.PlayerDoenstExist;
import java.io.IOException;
import java.util.Random;

public class Main {

    public static int getRand(){
        Random rand = new Random();
        return rand.nextInt(101);
    }



    public static void main(String[] args) throws IOException, ClassNotFoundException, PlayerDoenstExist, InterruptedException {
        SPORTMController controller = new SPORTMController();
        controller.run();
        //Goalkeeper g = new Goalkeeper(" ",0,"",0,0,0,0,0,0,0,0);
        //Striker s = new Striker(" ",0,"",0,0,0,0,0,0,0,0);
/*
        //System.out.println(g.getClass().getSimpleName().equals(s.getClass().getSimpleName()));


        // SPORTMViewer viewer = new SPORTMViewer();
        //viewer.showWelcome();
        FootballState s = new FootballState();
        FootballTeam t1 = new FootballTeam();
        FootballTeam t2 = new FootballTeam();
        t1.setName("Team1");
        t2.setName("Team2");
        int j = 0;
        int k = 1;
        for(int i = 0; i<10; i++,k++){
            if(i>=5) j = 1;
            Goalkeeper g = new Goalkeeper("Goalie" + i, i+k, "Team" + j, getRand(),getRand(),getRand(),getRand(),getRand(),getRand(),getRand(),getRand());
            s.addPlayer(g);
            if(i>=5) t2.addPlayer(g);
            else t1.addPlayer(g);
        }
        j = 0;
        for(int i = 0; i<10; i++,k++){
            if(i>=5) j = 1;
            Lateral l = new Lateral("Lateral" + i, i+k, "Team" + j,getRand(),getRand(),getRand(),getRand(),getRand(),getRand(),getRand(),getRand());
            s.addPlayer(l);
            if(i>=5) t2.addPlayer(l);
            else t1.addPlayer(l);
        }
        j = 0;
        for(int i = 0; i<10; i++,k++){
            if(i>=5) j = 1;
            Defender d = new Defender("Defender" + i, i+k, "Team" + j,getRand(),getRand(),getRand(),getRand(),getRand(),getRand(),getRand(),getRand());
            s.addPlayer(d);
            if(i>=5) t2.addPlayer(d);
            else t1.addPlayer(d);
        }
        for(int i = 0; i<10; i++,k++){
            if(i>=5) j = 1;
            Midfielder m = new Midfielder("Midfielder" + i, i+k, "Team" + j,getRand(),getRand(),getRand(),getRand(),getRand(),getRand(),getRand(),getRand());
            s.addPlayer(m);
            if(i>=5) t2.addPlayer(m);
            else t1.addPlayer(m);
        }
        for(int i = 0; i<10; i++,k++){
            if(i>=5) j = 1;
            Striker st = new Striker("Striker" + i, i+k, "Team" + j,getRand(),getRand(),getRand(),getRand(),getRand(),getRand(),getRand(),getRand());
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
             FootballState s2 = r.readState("estado.txt");
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

        ExecuteFootballGame footballGame = new ExecuteFootballGame(game);
        int messageIndex = 0;
        List<String> gameReport = new ArrayList<>();
        String message;
        int speed = 2;
        while(footballGame.getGame().getTimer() < 90){
            footballGame.ExecutePlay();
            gameReport = footballGame.getGameReport();
            if(speed != 3) {
                while (messageIndex < gameReport.size()) {
                    message = gameReport.get(messageIndex);
                    System.out.println(message);
                    messageIndex++;
                }
            }
        }
        System.out.println("-------------\n"+s.getTeam("Team1").getPlayer(1).toString()+"\n-----------------\n");

        System.out.println("-------------\n"+footballGame.getGame().toString()+"\n-----------------\n");


        //viewer.showTermination();

*/


        }

}
interface sleeping {
    public default void sleepDependingOnMessage(String message, int speed) throws InterruptedException {
        if (message.contains("steal")
                || message.contains("pass")
                || message.contains("score")
                || message.contains("shoots")
        ) Thread.sleep(500 / (speed + 1));
        else if (message.contains("now has"))
            Thread.sleep(1000 / (speed + 1));
        else if (message.contains("GOOOOOOO"))
            Thread.sleep(1500 / (speed + 1));
    }
}