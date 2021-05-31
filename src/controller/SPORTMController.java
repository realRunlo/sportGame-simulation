package controller;
import model.football.player.FootballPlayer;
import model.football.state.FootballState;
import model.football.team.FootballTeam;
import readFile.readFile;
import viewer.SPORTMViewer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SPORTMController implements Observer {
    private FootballState footballState;
    private final Scanner scanner;

    private final String[] InitialMenu   = new String[]{
            "Initial Menu",
            "Load State",
            "Enter Game",
            "Quit"};
    private final String[] LoadStateMenu = new String[]{
            "State Menu",
            "Default State",
            "Costum State"};
    private final String[] GameMenu = new String[]{
            "Game Menu",
            "State information",
            "Create Game",
            "Game History",
            "Add Team",
            "Add Player"};
    private final String[] TeamMenu = new String[]{
            "Team Menu",
            "Set Name",
            "Team Info",
            "Add Player",
            "Overall Skill",
            "Save Team"};
    private final String[] PlayerMenu = new String[]{
            "Player Menu",
            "Set Name",
            "Player Info",
            "Change Player Type",
            "Change Shirt",
            "Change Speed",
            "Change Resistance",
            "Change Dexterity",
            "Change Implosion",
            "Change HeadGame",
            "Change Kick",
            "Change Passing",
            "Change Type Specific Skill",
            "Overall Skill",
            "Save Player"};
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
        initialMenu.SimpleRun();
    }


    /**------------------------LOAD STATE-------------------------------**/

    public void loadState() throws IOException, ClassNotFoundException {
        SPORTMViewer loadMenu = new SPORTMViewer(LoadStateMenu);
        loadMenu.setHandler(1,()-> defaultState());
        loadMenu.setHandler(2,()-> costumState());
        loadMenu.SimpleRun();
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
        enterState.SimpleRun();
    }

    public void addTeam() throws IOException, ClassNotFoundException {
        FootballTeam newTeam = new FootballTeam();
        SPORTMViewer teamMenu = new SPORTMViewer(TeamMenu);
        teamMenu.setSamePreCondition(new int[]{2,3,4,5},()->!newTeam.getName().equals(" "));
        teamMenu.setHandler(1,()-> changeTeamName(newTeam));
        teamMenu.setHandler(2,()->teamMenu.showInfo(newTeam));
        teamMenu.setHandler(3,()-> newPlayer(newTeam));
        teamMenu.SimpleRun();
    }

    public void changeTeamName(FootballTeam t){
        String newName;
        int valido = 0;
        do{
            newName = getName();
            if(footballState.getTeams().containsKey(newName)){
                if(footballState.getTeam(newName).equals(t)){
                    valido = 1;
                }
            }
            else valido = 1;
            if(valido == 0) System.out.println("Name already in use");
        }while(valido == 0);
        t.setName(newName);
    }

    public void newPlayer(FootballTeam t) throws IOException, ClassNotFoundException {
        AtomicReference<String> name = new AtomicReference<>("");
        AtomicInteger speed = new AtomicInteger(0),
                resistance =  new AtomicInteger(0), dexterity =  new AtomicInteger(0),
                implosion =  new AtomicInteger(0), headGame =  new AtomicInteger(0),
                kick =  new AtomicInteger(0), passing =  new AtomicInteger(0),
                type =  new AtomicInteger(-1);
        SPORTMViewer playerMenu = new SPORTMViewer(PlayerMenu);
        //apenas pode mudar a camisola e o tipo de jogador se ja lhe tiver dado um nome
        playerMenu.setSamePreCondition(new int[]{3,4},()->!name.get().equals(""));
        //apenas pode mudar as skills do jogador,ver informacao sobre ele e gravar, se ja tiver escolhido o tipo de jogador
        playerMenu.setSamePreCondition(new int[]{2,5,6,7,8,9,10,11,12,13,14},()->!name.get().equals("") && type.get() != -1);
        playerMenu.setHandler(1, ()->  name.set(getName()));
        playerMenu.SimpleRun();
    }




    /**------------------------General methods-----------------------------**/

    public String getName(){
        System.out.println("Insert a name");
        return scanner.nextLine();
    }

    public int getSkillValue(){
        System.out.println("Insert a skill value");
        return scanner.nextInt();
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
