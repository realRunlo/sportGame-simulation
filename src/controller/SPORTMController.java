package controller;
import model.football.state.FootballState;
import model.football.team.FootballTeam;
import readFile.readFile;
import viewer.SPORTMViewer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class SPORTMController implements Observer {
    private FootballState footballState;
    private final Scanner scanner;

    private final String[] InitialMenu   = new String[]{"Initial Menu","Load State","Enter Game","Quit"};
    private final String[] LoadStateMenu = new String[]{"State Menu","Default State","Costum State"};
    private final String[] GameMenu = new String[]{"Game Menu","State information","Create Game","Game History","Add Team","Add Player"};
    private final String[] TeamMenu = new String[]{"Team Menu","Set Name","Check Players","Add Player","Overall Skill"};

    public SPORTMController(){
        footballState = new FootballState();
        scanner = new Scanner(System.in);
    }

    public void setState(FootballState s){
        footballState = s.clone();
    }


    public void run() throws IOException, ClassNotFoundException {
        showWelcome();
        SPORTMViewer initialMenu = new SPORTMViewer(InitialMenu);
        initialMenu.setHandler(1,()->loadState());
        initialMenu.setHandler(2,()->EnterState());
        initialMenu.setHandler(3,()->terminate());
        initialMenu.run();
    }


    /**------------------------LOAD STATE-------------------------------**/

    public void loadState() throws IOException, ClassNotFoundException {
        SPORTMViewer loadMenu = new SPORTMViewer(LoadStateMenu);
        loadMenu.setHandler(1,()-> defaultState());
        loadMenu.setHandler(2,()-> costumState());
        loadMenu.run();
    }

    public void defaultState() throws IOException, ClassNotFoundException {
        readFile r = new readFile();
        try {
            this.footballState = r.readState("estado.txt");
        }
        catch(ClassNotFoundException e){
            System.out.println("Error class not found");
        }
        catch(FileNotFoundException e){
            System.out.println("File not found.");
        }
        catch(IOException e){
            System.out.println("Error accessing the file");
            e.printStackTrace();
        }

    }
    public void costumState() throws IOException, ClassNotFoundException {
        readFile r = new readFile();
        String s = new String();
        s = scanner.nextLine();
        this.footballState = r.readState(s);
    }

    /**------------------------ENTER STATE-------------------------------**/

    public void EnterState() throws IOException, ClassNotFoundException {
        SPORTMViewer enterState = new SPORTMViewer(GameMenu);
        enterState.setHandler(1,()-> enterState.showInfo(this.footballState));
        enterState.setPreCondition(2,()-> false);
        enterState.setHandler(3,()-> enterState.showInfo(this.footballState.getGameHistory()));
        enterState.setHandler(4,()-> addTeam());
        enterState.setPreCondition(5,()-> false);
        enterState.run();
    }

    public void addTeam() throws IOException, ClassNotFoundException {
        FootballTeam newTeam = new FootballTeam();
        SPORTMViewer teamMenu = new SPORTMViewer(TeamMenu);
        teamMenu.setPreCondition(2,()->!newTeam.getName().equals(" "));
        teamMenu.setPreCondition(3,()->!newTeam.getName().equals(" "));
        teamMenu.setPreCondition(4,()->!newTeam.getName().equals(" "));
        teamMenu.setHandler(1,()->);
        teamMenu.run();
    }




    /**------------------------SIMPLE PRINTS-------------------------------**/
    public void terminate(){
        clearScreen();
        showTermination();
        System.exit(0);
    }

    public void showWelcome(){
        clearScreen();
        System.out.println("Welcome to SPORTM!");
        System.out.println("""
                Made by:
                ->Group 68
                ->Goncalo Braz Afonso a93178
                ->Goncalo Jose Teixeira Pereira a93168
                ->Marco Andre Pereira da Costa a93283
                """);

    }

    public void showTermination(){
        System.out.println("Closing the Game\nWe hope you've enjoyed your time.");
    }

    public void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public void update(Observable o, Object s) {
        setState((FootballState) s);
    }
}
