package controller;
import model.football.foul.Card;
import model.football.player.*;
import model.football.state.FootballState;
import model.football.team.FootballTeam;
import readFile.readFile;
import viewer.SPORTMViewer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class SPORTMController implements Observer {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
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
            "Update Player",
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
        teamMenu.setSamePreCondition(new int[]{2,3},()->!newTeam.getName().equals(" "));
        teamMenu.setSamePreCondition(new int[]{4,5},()->newTeam.getNPlayers() > 0);
        teamMenu.setHandler(1,()-> changeTeamName(newTeam));
        teamMenu.setHandler(2,()-> teamMenu.showInfo(newTeam));
        teamMenu.setHandler(3,()-> newPlayer(newTeam));

        teamMenu.setHandler(5,()->updateTeamState(newTeam,true,teamMenu));
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
        AtomicInteger[] atributes = new AtomicInteger[]
                {
                        new AtomicInteger(0),//0 - speed
                        new AtomicInteger(0),//1 - resistance
                        new AtomicInteger(0),//2 - dexterity
                        new AtomicInteger(0),//3 - implosion
                        new AtomicInteger(0),//4 - headGame
                        new AtomicInteger(0),//5 - kick
                        new AtomicInteger(0),//6 - passing
                        new AtomicInteger(0),//7 - typeSpecificSkill
                        new AtomicInteger(-1),//8 - type
                        new AtomicInteger(-1)//9 - shirt
                };
        SPORTMViewer playerMenu = new SPORTMViewer(PlayerMenu);

        //apenas pode mudar a camisola e o tipo de jogador se ja lhe tiver dado um nome
        playerMenu.setSamePreCondition(new int[]{3,4},()->!name.get().equals(""));
        playerMenu.setPreCondition(13,()->!name.get().equals("") && atributes[9].get()!=-1);
        playerMenu.setPreCondition(2,()-> atributes[8].get() != -1);
        //apenas pode mudar as skills do jogador,ver informacao sobre ele, se ja tiver escolhido o tipo de jogador
        playerMenu.setSamePreCondition(new int[]{2,5,6,7,8,9,10,11,12},()->!name.get().equals("") && atributes[8].get() != -1 && atributes[9].get() != -1);

        playerMenu.setHandler(1, ()->  name.set(getName()));
        playerMenu.setHandler(2, ()->  playerMenu.showInfo(playerPrototype(Arrays.stream(atributes).mapToInt(e->e.get()).toArray(),name.get(),t.getName(),new ArrayList<String>())));
        playerMenu.setHandler(3, ()->  atributes[8].set(playerMenu.readOptionBetween(0,4,new String[]{"Goalkeeper","Defender","Lateral","Midfielder","Striker"})));
        playerMenu.setHandler(4, ()->  atributes[9].set(getShirtNumber(name.get(),t)));

        //handlers dos atributos de um jogador
        for(int i = 0,j = 5; i<8;i++,j++) {
            int finalI = i;
            playerMenu.setHandler(j, () -> atributes[finalI].set(playerMenu.readOptionBetween(0, 100, null)));
        }
        //gravar o jogador
        playerMenu.setHandler(13, ()-> updatePlayerState(playerPrototype(Arrays.stream(atributes).mapToInt(e->e.get()).toArray(),name.get(),t.getName(),new ArrayList<String>()),t,playerMenu,true,false));
        playerMenu.SimpleRun();
    }



    public FootballPlayer playerPrototype(int [] atributes, String name, String team,List<String> background)
    {
        FootballPlayer p;
        switch(atributes[8]){
            case 0:p = new Goalkeeper(name,atributes[9],team,background,atributes[0],atributes[1],atributes[2],atributes[3],atributes[4],atributes[5],atributes[6], Card.NONE,atributes[7]); break;
            case 1:p = new Defender(name,atributes[9],team,background,atributes[0],atributes[1],atributes[2],atributes[3],atributes[4],atributes[5],atributes[6], Card.NONE,atributes[7]); break;
            case 2:p = new Lateral(name,atributes[9],team,background,atributes[0],atributes[1],atributes[2],atributes[3],atributes[4],atributes[5],atributes[6], Card.NONE,atributes[7]); break;
            case 3:p = new Midfielder(name,atributes[9],team,background,atributes[0],atributes[1],atributes[2],atributes[3],atributes[4],atributes[5],atributes[6], Card.NONE,atributes[7]); break;
            case 4:p = new Striker(name,atributes[9],team,background,atributes[0],atributes[1],atributes[2],atributes[3],atributes[4],atributes[5],atributes[6], Card.NONE,atributes[7]); break;
            default: p = null;
        }
        return p;
    }




    /**------------------------General methods-----------------------------**/

    public String getName(){
        System.out.println("Insert a name");
        return scanner.nextLine();
    }

    public Integer getShirtNumber(String name, FootballTeam t){
        //caso de apenas adicionar jogador ao estado,
        //nao pode adicionar jogadores com o mesmo numero e nome
        boolean valid = false;
        int shirtNumber = -1;
        while(!valid){
            System.out.println("Insert the number of the shirt");
            try{
                shirtNumber = Integer.parseInt(scanner.nextLine());
            }
            catch(NumberFormatException e){
                shirtNumber = -1;
                System.out.println(RED +"Insert a valid number"+ RESET);
            }
            if(shirtNumber!=-1) {
                if (t == null){
                    valid = footballState.existsPlayer(name,shirtNumber);
                    if (!valid) System.out.println(RED+"Player with the same name and shirt already exists"+RESET);
                }
                else{
                    valid = !t.existsShirtNumber(shirtNumber);
                    if (!valid) System.out.println(RED+"Team already has a players with that shirt" + RESET);
                }
            }
        }
        return shirtNumber;
    }

    public int getSkillValue(){
        System.out.println("Insert a skill value");
        return scanner.nextInt();
    }

    public void updatePlayerState(FootballPlayer p,FootballTeam t, SPORTMViewer viewer,boolean newPlayer,boolean updateState){
        if(!newPlayer){
            if(t != null) t.updatePlayer(p); //atualiza na equipa a ser modificada atualmente (apenas menu)
            if(updateState) footballState.updatePlayer(p); //atualiza no estado por completo
        }
        else{
            if(t != null) t.addPlayer(p);
            if(updateState) footballState.addPlayer(p);
        }
        if(viewer != null) viewer.returnMenu();
    }

    public void updateTeamState(FootballTeam t, boolean newTeam,SPORTMViewer viewer){
        if(newTeam) footballState.addTeam(t);
        else footballState.updateTeam(t);
        if(viewer != null) viewer.returnMenu();
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
