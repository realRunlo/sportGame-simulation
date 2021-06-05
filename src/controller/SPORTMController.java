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
    private static final int MaxNumberOfPlayers = 23;

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
            "Add Player",
            "Update Team",
            "Update Player",
            "Transfer Player",
            "Remove Team",
            "Remove Player",
            "Save State"
    };
    private final String[] TeamMenu = new String[]{
            "Team Menu",
            "Set Name",
            "Team Info",
            "Add Player",
            "Update Player",
            "Remove Player",
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
    private final String[] RemoveTeamMenu = new String[]{
            "Remove Team Menu",
            "Remove only Team",
            "Remove Team and Players"};
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
        loadMenu.setHandler(1,()-> defaultState(loadMenu));
        loadMenu.setHandler(2,()-> costumState(loadMenu));
        loadMenu.SimpleRun();
    }

    public void defaultState(SPORTMViewer viewer) throws IOException, ClassNotFoundException {
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
        viewer.returnMenu();
    }


    public void costumState(SPORTMViewer viewer) throws IOException, ClassNotFoundException {
        readFile r = new readFile();
        try {
            this.footballState = r.readState(getName());
        }
        catch (FileNotFoundException e){
            System.out.println(RED +"File not found" + RESET);
        }
        catch (IOException e){
            System.out.println(RED +"Error loading the file"+ RESET);
        }
        catch (ClassNotFoundException e){
            System.out.println(RED +"Invalid class loaded"+ RESET);
        }
        viewer.returnMenu();
    }

    /**------------------------ENTER STATE-------------------------------**/

    public void EnterState() throws IOException, ClassNotFoundException {
        SPORTMViewer enterState = new SPORTMViewer(GameMenu);
        enterState.setPreCondition(2,()-> false);
        enterState.setSamePreCondition(new int[]{7,10},()->footballState.getNPlayers()>0);
        enterState.setSamePreCondition(new int[]{6,8,9},()->footballState.getNTeams()>0);

        enterState.setHandler(1,()-> enterState.showInfo(this.footballState));
        enterState.setHandler(3,()-> enterState.showInfo(this.footballState.getGameHistory()));
        enterState.setHandler(4,()-> addOrUpdateTeam(false));
        enterState.setHandler(5,()-> newPlayer(null,true));
        enterState.setHandler(6,()-> addOrUpdateTeam(true));
        enterState.setHandler(7,()-> updatePlayer(null,true));
        enterState.setHandler(8,()-> transferPlayer());
        enterState.setHandler(9,()-> removeTeam());
        enterState.setHandler(10,()-> removePlayer());
        enterState.setHandler(11,()-> footballState.saveState(getName()));
        enterState.SimpleRun();
    }

    public void addOrUpdateTeam(boolean update) throws IOException, ClassNotFoundException {
        FootballTeam team;
        if(update) team = chooseTeam("Insert a team to update");
        else team = new FootballTeam();
        if(team != null) {
            SPORTMViewer teamMenu = new SPORTMViewer(TeamMenu);
            //caso seja so um update, nao permite mudar o nome da equipa
            if (update) teamMenu.setPreCondition(1, () -> false);
            teamMenu.setSamePreCondition(new int[]{2, 6}, () -> !team.getName().equals(" "));
            teamMenu.setPreCondition(3, () -> !team.getName().equals(" ") && team.getNPlayers() < MaxNumberOfPlayers);
            teamMenu.setSamePreCondition(new int[]{4, 5}, () -> team.getNPlayers() > 0);


            teamMenu.setHandler(1, () -> changeTeamName(team));
            teamMenu.setHandler(2, () -> teamMenu.showInfo(team));
            teamMenu.setHandler(3, () -> newPlayer(team, false));
            teamMenu.setHandler(4, () -> updatePlayer(team, false));
            teamMenu.setHandler(5, () -> removePlayerTeam(team));
            teamMenu.setHandler(6, () -> updateTeamState(team, !update, teamMenu));
            teamMenu.SimpleRun();
        }
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

    public void newPlayer(FootballTeam t,boolean updateState) throws IOException, ClassNotFoundException {
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
        playerMenu.setHandler(2, ()->  playerMenu.showInfo(playerPrototype(Arrays.stream(atributes).mapToInt(e->e.get()).toArray(),name.get(),t,new ArrayList<String>())));
        playerMenu.setHandler(3, ()->  atributes[8].set(playerMenu.readOptionBetween(0,4,new String[]{"Goalkeeper","Defender","Lateral","Midfielder","Striker"})));
        playerMenu.setHandler(4, ()->  atributes[9].set(getShirtNumber(name.get(),t)));

        //handlers dos atributos de um jogador
        for(int i = 0,j = 5; i<8;i++,j++) {
            int finalI = i;
            playerMenu.setHandler(j, () -> atributes[finalI].set(playerMenu.readOptionBetween(0, 100, null)));
        }
        //gravar o jogador
        playerMenu.setHandler(13, ()-> updatePlayerState(playerPrototype(Arrays.stream(atributes).mapToInt(e->e.get()).toArray(),name.get(),t,new ArrayList<String>()),t,playerMenu,true,updateState));
        playerMenu.SimpleRun();
    }


    public void updatePlayer(FootballTeam t,boolean updateState) throws IOException, ClassNotFoundException {
        FootballPlayer p = choosePlayerToUpdate(t);
        AtomicReference<FootballTeam> team = new AtomicReference<>();
        team.set(t);
        if(p!= null){
            //caso de update diretamente no estado, pode ser necessario dar update na equipa se o jogador tiver uma
            if(t == null & footballState.existsTeam(p.getCurTeam())) team.set(footballState.getTeam(p.getCurTeam()));
            AtomicInteger[] atributes = new AtomicInteger[]
                    {
                            new AtomicInteger(p.getSpeed()),//0 - speed
                            new AtomicInteger(p.getSpeed()),//1 - resistance
                            new AtomicInteger(p.getSpeed()),//2 - dexterity
                            new AtomicInteger(p.getSpeed()),//3 - implosion
                            new AtomicInteger(p.getSpeed()),//4 - headGame
                            new AtomicInteger(p.getSpeed()),//5 - kick
                            new AtomicInteger(p.getSpeed()),//6 - passing
                            new AtomicInteger(getSpecialSkill(p)),//7 - typeSpecificSkill
                            new AtomicInteger(typeOfPlayer(p)),//8 - type
                            new AtomicInteger(p.getNumber())//9 - shirt
                    };
            SPORTMViewer updatePlayer = new SPORTMViewer(PlayerMenu);
            //nao permite que se mude o nome e numero do jogador
            updatePlayer.setSamePreCondition(new int[]{1,4},()->false);
            //apenas pode mudar a camisola e o tipo de jogador se ja lhe tiver dado um nome
            updatePlayer.setPreCondition(3,()->!p.getName().equals(""));
            updatePlayer.setPreCondition(13,()->!p.getName().equals("") && atributes[9].get()!=-1);
            updatePlayer.setPreCondition(2,()-> atributes[8].get() != -1);
            //apenas pode mudar as skills do jogador,ver informacao sobre ele, se ja tiver escolhido o tipo de jogador
            updatePlayer.setSamePreCondition(new int[]{2,5,6,7,8,9,10,11,12},()->!p.getName().equals("") && atributes[8].get() != -1 && atributes[9].get() != -1);

            updatePlayer.setHandler(2, ()->  updatePlayer.showInfo(playerPrototype(Arrays.stream(atributes).mapToInt(AtomicInteger::get).toArray(),p.getName(),t,p.getBackground())));
            updatePlayer.setHandler(3, ()->  atributes[8].set(updatePlayer.readOptionBetween(0,4,new String[]{"Goalkeeper","Defender","Lateral","Midfielder","Striker"})));

            //handlers dos atributos de um jogador
            for(int i = 0,j = 5; i<8;i++,j++) {
                int finalI = i;
                updatePlayer.setHandler(j, () -> atributes[finalI].set(updatePlayer.readOptionBetween(0, 100, null)));
            }
            //gravar o jogador
            updatePlayer.setHandler(13, ()-> updatePlayerState(playerPrototype(Arrays.stream(atributes).mapToInt(e->e.get()).toArray(),p.getName(),team.get(),p.getBackground()),team.get(),updatePlayer,false,updateState));

            updatePlayer.SimpleRun();
        }
    }



    public FootballTeam chooseTeam(String message) {
        FootballTeam t = null;
        System.out.println("Write -1 to return");
        String name = "";
        boolean valid = false;
        while (!valid && !name.equals("-1")) {
            System.out.println(footballState.printTeams());
            System.out.println(message);
            name = scanner.nextLine();
            if(!name.equals("-1")) {
                if (footballState.existsTeam(name)) {
                    valid = true;
                    t = footballState.getTeam(name);
                }
                else System.out.println(RED + "Choose a valid team" + RESET);
            }
        }
        return t;
    }

    public void transferPlayer(){
        FootballPlayer p = choosePlayerToUpdate(null);
        if(p!=null) {
            FootballTeam teamToTransfer = chooseTeam(
                    "Insert  a team to transfer the player to\n" +
                            "Write -1 if you just wish to remove him from his team"
            );
            footballState.transferPlayer(p,teamToTransfer);
        }
    }

    public void removeTeam() throws IOException, ClassNotFoundException {
        SPORTMViewer menu = new SPORTMViewer(RemoveTeamMenu);
        FootballTeam t = chooseTeam("Choose the Team to remove");
        if(t!=null) {
            menu.setHandler(1, () -> removeTeam(menu,t,false));
            menu.setHandler(2, () -> removeTeam(menu,t,true));
            menu.SimpleRun();
        }
    }

    public void removeTeam(SPORTMViewer menu, FootballTeam t,boolean removeAll){
        if(removeAll) footballState.removeTeam(t.getName());
        else footballState.removeOnlyTeam(t.getName());
        menu.returnMenu();
    }

    public void removePlayer(){
        FootballPlayer p = choosePlayerToUpdate(null);
        if(p!=null) footballState.removePlayer(p.getName(),p.getNumber(),p.getCurTeam());
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
            if(shirtNumber >= 0) {
                if (t == null){
                    if (footballState.existsPlayer(name,shirtNumber)) System.out.println(RED+"Player with the same name and shirt already exists"+RESET);
                    else valid = true;
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

    public void removePlayerTeam(FootballTeam t){
        boolean removed = false; int op = -2;
        System.out.println(t.toString()+"\nWrite '-1' to return");
        while(!removed && op!=-1){
            System.out.println("Write the player's shirt to remove him from the team");
            try {
                String line = scanner.nextLine();
                op = Integer.parseInt(line);
            }
            catch (NumberFormatException e) { // Não foi inscrito um int
                op = -2;
            }
            if(op>=0){
                if(t.existsShirtNumber(op)){
                    removed = true;
                    t.removePlayer(op);
                }
                else System.out.println(RED + "Team doesnt have that player" + RESET);
            }
        }
    }


    public FootballPlayer choosePlayerToUpdate(FootballTeam t){
        System.out.println("Write -1 to return");
        int shirtNumber = -2;
        String name = "";
        boolean valid = false;
        FootballPlayer p = null;
        while(!valid && shirtNumber != -1){
            // caso de update atravez da equipa
            if(t!=null){
                System.out.println(t.printPlayers());
                System.out.println("Write the shirt of the player to update");
            }
            //caso de update diretamente no estado
            else{
                System.out.println(footballState.printPlayers());
                System.out.println("Write the name and shirt of the player to update\nInsert the number of the shirt: ");
            }


            try{//pede pela camisola
                shirtNumber = Integer.parseInt(scanner.nextLine());
            }
            catch(NumberFormatException e){
                shirtNumber = -2;
                System.out.println(RED +"Insert a valid number"+ RESET);
            }

            if(t!=null){
                if(shirtNumber >=0){
                    if(t.existsShirtNumber(shirtNumber)){
                        valid = true;
                        p = t.getPlayer(shirtNumber);
                    }
                    else System.out.println(RED +"Insert a valid player"+ RESET);
                }
            }
            else{
                if(shirtNumber >=0){
                    System.out.println("Insert the name of the player");
                    name = scanner.nextLine();
                    if(footballState.existsPlayer(name,shirtNumber)){
                        valid = true;
                        p = footballState.getPlayer(name,shirtNumber);
                    }
                }

            }
        }
        return p;
    }

    public FootballPlayer playerPrototype(int [] atributes, String name, FootballTeam t,List<String> background)
    {
        FootballPlayer p;
        String team = "None";
        if(t!= null) team = t.getName();
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

    int typeOfPlayer(FootballPlayer p){
        int type = -1;
        if(p instanceof Goalkeeper) type = 0;
        if(p instanceof Defender) type = 1;
        if(p instanceof Lateral) type = 2;
        if(p instanceof Midfielder) type = 3;
        if(p instanceof Striker) type = 4;
        return type;
    }

    int getSpecialSkill(FootballPlayer p){
        int type = typeOfPlayer(p);
        int skill = 0;
        switch(type){
            case 0: Goalkeeper g = (Goalkeeper) p;
                    skill = g.getElasticity();break;
            case 1: Defender d = (Defender) p;
                skill = d.getBallRetention();break;
            case 2: Lateral l = (Lateral) p;
                skill = l.getCrossing();break;
            case 3: Midfielder m = (Midfielder) p;
                skill = m.getBallRecovery();break;
            case 4: Striker s = (Striker) p;
                skill = s.getShooting();break;
        }
        return skill;
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
