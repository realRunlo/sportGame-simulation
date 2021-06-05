package controller;
import model.exceptions.PlayerDoenstExist;
import model.football.foul.Card;
import model.football.game.ExecuteFootballGame;
import model.football.game.FootballGame;
import model.football.player.*;
import model.football.state.FootballState;
import model.football.team.FootballTeam;
import model.football.team.lineup.FootballLineup;
import readFile.readFile;
import viewer.SPORTMViewer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class SPORTMController{
    private FootballState footballState;
    private final Scanner scanner;
    private final SPORTMViewer messages = new SPORTMViewer(new String[]{});
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
            "Create Match",
            "Match History",
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
            "Save Player",
            "Randomize Stats"
    };
    private final String[] RemoveTeamMenu = new String[]{
            "Remove Team Menu",
            "Remove only Team",
            "Remove Team and Players"};
    private final String[] LineupMenu = new String[]{ //4-4-2 e 4-3-3
            "Lineup Menu",
            "Choose strategy",
            "Auto assign players",
            "Assign playing",
            "Assign substitutes",
            "Save lineup"
    };
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
            messages.errorMessage("Error class not found");
        }
        catch(FileNotFoundException e){
            messages.errorMessage("File not found");
        }
        catch(IOException e){
            messages.errorMessage("Error accessing the file");
        }
        viewer.returnMenu();
    }


    public void costumState(SPORTMViewer viewer) throws IOException, ClassNotFoundException {
        readFile r = new readFile();
        try {
            this.footballState = r.readState(getName());
        }
        catch (FileNotFoundException e){
            messages.errorMessage("File not found");
        }
        catch (IOException e){
            messages.errorMessage("Error accessing the file");
        }
        catch (ClassNotFoundException e){
            messages.errorMessage("Error class not found");
        }
        viewer.returnMenu();
    }

    /**------------------------ENTER STATE-------------------------------**/

    public void EnterState() throws IOException, ClassNotFoundException {
        SPORTMViewer enterState = new SPORTMViewer(GameMenu);
        enterState.setPreCondition(2,()-> footballState.getNTeams() >= 2);
        enterState.setSamePreCondition(new int[]{7,10},()->footballState.getNPlayers()>0);
        enterState.setSamePreCondition(new int[]{6,8,9},()->footballState.getNTeams()>0);

        enterState.setHandler(1,()-> enterState.showInfo(this.footballState));
        enterState.setHandler(2,()-> {
            try {
                createGame();
            } catch (PlayerDoenstExist playerDoenstExist) {
                playerDoenstExist.printStackTrace();
            }
        });
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
            if(valido == 0) messages.errorMessage("Name already in use");
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
        playerMenu.setSamePreCondition(new int[]{2,5,6,7,8,9,10,11,12,14},()->!name.get().equals("") && atributes[8].get() != -1 && atributes[9].get() != -1);

        playerMenu.setHandler(1, ()->  name.set(getName()));
        playerMenu.setHandler(2, ()->  playerMenu.showInfo(playerPrototype(Arrays.stream(atributes).mapToInt(e->e.get()).toArray(),name.get(),t,new ArrayList<String>())));
        playerMenu.setHandler(3, ()->  atributes[8].set(playerMenu.readOptionBetween(0,4,new String[]{"Goalkeeper","Defender","Lateral","Midfielder","Striker"})));
        playerMenu.setHandler(4, ()->  atributes[9].set(getShirtNumber(name.get(),t)));
        playerMenu.setHandler(14, ()->  randomizeStats(atributes));
        //handlers dos atributos de um jogador
        for(int i = 0,j = 5; i<8;i++,j++) {
            int finalI = i;
            playerMenu.setHandler(j, () -> atributes[finalI].set(getSkillValue(playerMenu,"Current Value: " + atributes[finalI].get())));
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
            updatePlayer.setSamePreCondition(new int[]{2,5,6,7,8,9,10,11,12,14},()->!p.getName().equals("") && atributes[8].get() != -1 && atributes[9].get() != -1);

            updatePlayer.setHandler(2, ()->  updatePlayer.showInfo(playerPrototype(Arrays.stream(atributes).mapToInt(AtomicInteger::get).toArray(),p.getName(),t,p.getBackground())));
            updatePlayer.setHandler(3, ()->  atributes[8].set(updatePlayer.readOptionBetween(0,4,new String[]{"Goalkeeper","Defender","Lateral","Midfielder","Striker"})));
            updatePlayer.setHandler(14, ()->  randomizeStats(atributes));
            //handlers dos atributos de um jogador
            for(int i = 0,j = 5; i<8;i++,j++) {
                int finalI = i;
                updatePlayer.setHandler(j, () -> atributes[finalI].set(getSkillValue(updatePlayer,"Current Value: " + atributes[finalI].get())));
            }
            //gravar o jogador
            updatePlayer.setHandler(13, ()-> updatePlayerState(playerPrototype(Arrays.stream(atributes).mapToInt(e->e.get()).toArray(),p.getName(),team.get(),p.getBackground()),team.get(),updatePlayer,false,updateState));

            updatePlayer.SimpleRun();
        }
    }



    public FootballTeam chooseTeam(String message) {
        FootballTeam t = null;
        String name = "";
        boolean valid = false;
        while (!valid && !name.equals("-1")) {
            messages.informationMessage("Write -1 to return");
            messages.normalMessage(footballState.printTeams());
            messages.informationMessage(message);
            name = scanner.nextLine();
            if(!name.equals("-1")) {
                if (footballState.existsTeam(name)) {
                    valid = true;
                    t = footballState.getTeam(name);
                }
                else messages.errorMessage("Choose a valid team");
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



    public void createGame() throws PlayerDoenstExist, IOException, ClassNotFoundException {
        messages.titleMessage(
                "--------------------------------------\n" +
                "---------------New Match--------------\n" +
                "--------------------------------------\n"
        );
        FootballTeam homeTeam = null;
        boolean validHome = false, validVisitor = false, leave = false;
        while(!validHome && !leave){
            homeTeam = chooseTeam("Choose the home team");
            if(homeTeam == null) leave = true;
            else if(homeTeam.getNPlayers() > 0) validHome = true;
                 else messages.errorMessage("Team doesn't have enough players to play");
        }

        FootballTeam visitingTeam = null;
        while(!validVisitor && !leave){
            visitingTeam = chooseTeam("Choose the visiting team");
            if(visitingTeam == null) leave = true;
            else if(visitingTeam.equals(homeTeam))
                messages.errorMessage("A team can't play against itself");
                else if(visitingTeam.getNPlayers() > 0) validVisitor = true;
                     else messages.errorMessage("Team doesn't have enough players to play");
        }
        if(!leave){
            messages.titleMessage("\t\t"+homeTeam.getName()+" VS "+visitingTeam.getName());

            FootballLineup homeLineup = makeLineup(homeTeam,true);
            if(homeLineup != null && homeLineup.readyToPlay()){
                FootballLineup visitorLineup = makeLineup(visitingTeam,false);
                if(visitorLineup != null && visitorLineup.readyToPlay()){
                    FootballGame g = new FootballGame(homeTeam,visitingTeam,homeLineup,visitorLineup);
                    ExecuteFootballGame play = new ExecuteFootballGame(g);
                    while(play.getGame().getTimer() <= 90){
                        play.ExecutePlay();
                    }



                    System.out.println("-------------\n"+play.getGame().toString()+"\n-----------------\n");




                    footballState.addGame(g);
                }else messages.errorMessage("Error with lineup, make sure there is at least 1 player of each type in the team");
            }else messages.errorMessage("Error with lineup, make sure there is at least 1 player of each type in the team");
        }
        messages.titleMessage(
                "--------------------------------------\n" +
                "---------------Returning--------------\n" +
                "--------------------------------------\n"
        );
    }

    public FootballLineup makeLineup(FootballTeam t,boolean home) throws IOException, ClassNotFoundException {
        SPORTMViewer lineupMenu = new SPORTMViewer(LineupMenu);

        if(home) lineupMenu.titleMessage("Home Team");
        else lineupMenu.titleMessage("Visitor Team");

        AtomicInteger strategy = new AtomicInteger(-1);
        AtomicReference<FootballLineup> protoLineup = new AtomicReference<>(new FootballLineup());
        FootballLineup lineup = null;
        AtomicBoolean saved = new AtomicBoolean(false);

        lineupMenu.setSamePreCondition(new int[]{2,3},()->strategy.get()!=-1);
        lineupMenu.setSamePreCondition(new int[]{4,5},()-> protoLineup.get().getPlaying().size() > 0);

        lineupMenu.setHandler(1,()->
                strategy.set(1 + lineupMenu.readOptionBetween(0,1,new String[]{"4-4-2","4-3-3"})));
        lineupMenu.setHandler(2,()-> protoLineup.set(new FootballLineup(t,strategy.get())));
        lineupMenu.setHandler(3,()-> setPlaying(protoLineup,t));
        lineupMenu.setHandler(4,()-> setSubstitutes(protoLineup,t));
        lineupMenu.setHandler(5,()-> saveLineup(saved,lineupMenu));
        lineupMenu.SimpleRun();
        if(saved.get()) lineup = protoLineup.get();
        return lineup;
    }


    public void setPlaying(AtomicReference<FootballLineup> proto, FootballTeam t){
        boolean saved = false;
        String line = "";
        int shirt = -1;
        FootballLineup l = proto.get();

        while(!saved && !line.equals("-1")){
            messages.normalMessage(t.printPlayers());
            messages.informationMessage("Write -1 to return, 's' to save\n Write 'a' to add a player and 'r' to remove");
            messages.normalMessage(l.printPlaying());

            line = scanner.nextLine();
            if(line.equals("s") || line.equals("save")){
                if(l.getPlaying().size() > 0){
                    saved = true;
                    proto.set(l);
                }
            }
            if(!line.equals("-1") && !saved){
                if(line.equals("a") || line.equals("r")){
                    try {
                        shirt = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) { // Não foi inscrito um int
                        shirt = -1;
                    }
                    if(shirt >= 0){
                        if(line.equals("a")) {
                            if (l.getPlaying().size() < 12) {
                                if(!l.addPlaying(t.getPlayer(shirt)))
                                    messages.errorMessage("Can't add that player");
                            }
                            else messages.errorMessage("Playing full");
                        }
                        else{
                            if(l.getPlaying().size() > 0){
                                l.remPlaying(shirt);
                            }
                            else messages.errorMessage("Playing empty");
                        }
                    }
                    else messages.errorMessage("Insert a valid shirt");
                }
                else messages.errorMessage("Invalid command");
            }

        }
    }

    public void setSubstitutes(AtomicReference<FootballLineup> proto, FootballTeam t){
        boolean saved = false;
        String line = "";
        int shirt = -1;
        FootballLineup l = proto.get();
        messages.informationMessage("Write -1 to return, 's' to save\n Write 'a' to add a player and 'r' to remove");

        while(!saved && !line.equals("-1")){
            messages.normalMessage(l.printSubstitutes());

            line = scanner.nextLine();
            if(line.equals("s") || line.equals("save")){
                if(l.getSubstitutes().size() > 0){
                    saved = true;
                    proto.set(l);
                }
            }
            if(!line.equals("-1") && !saved){
                if(line.equals("a") || line.equals("r")){
                    try {
                        shirt = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) { // Não foi inscrito um int
                        shirt = -1;
                    }
                    if(shirt >= 0){
                        if(line.equals("a")) {
                            if (l.getSubstitutes().size() < 12) {
                                if(!l.addSubstitute(t.getPlayer(shirt))) messages.errorMessage("Can't add that player");
                            }
                            else messages.errorMessage("Substitutes full");
                        }
                        else{
                            if(l.getSubstitutes().size() > 0){
                                l.remSubstitute(shirt);
                            }
                            else messages.errorMessage("Substitutes empty");
                        }
                    }
                    else messages.errorMessage("Insert a valid shirt");
                }
                else messages.errorMessage("Invalid command");
            }

        }
    }

    public void saveLineup(AtomicBoolean saved ,SPORTMViewer menu){
        saved.set(true);
        menu.returnMenu();
    }



    /**------------------------General methods-----------------------------**/

    public String getName(){
        messages.informationMessage("Insert a name");
        return scanner.nextLine();
    }

    public Integer getShirtNumber(String name, FootballTeam t){
        //caso de apenas adicionar jogador ao estado,
        //nao pode adicionar jogadores com o mesmo numero e nome
        boolean valid = false;
        int shirtNumber = -1;
        while(!valid){
            messages.informationMessage("Insert the number of the shirt");
            try{
                shirtNumber = Integer.parseInt(scanner.nextLine());
            }
            catch(NumberFormatException e){
                shirtNumber = -1;
                messages.errorMessage("Insert a valid number");
            }
            if(shirtNumber >= 0) {
                if (t == null){
                    if (footballState.existsPlayer(name,shirtNumber))
                        messages.errorMessage("Player with the same name and shirt already exists");
                    else valid = true;
                }
                else{
                    valid = !t.existsShirtNumber(shirtNumber);
                    if (!valid)
                        messages.errorMessage("Team already has a players with that shirt");
                }
            }
        }
        return shirtNumber;
    }

    public int getSkillValue(SPORTMViewer menu,String currentValue){
        messages.informationMessage(currentValue);
        return menu.readOptionBetween(0, 100, null);
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
        while(!removed && op!=-1){
            messages.normalMessage(t.toString());
            messages.informationMessage("Write '-1' to return\nWrite the player's shirt to remove him from the team");
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
                else messages.errorMessage("Team doesnt have that player");
            }
        }
    }


    public FootballPlayer choosePlayerToUpdate(FootballTeam t){
        int shirtNumber = -2;
        String name = "";
        boolean valid = false;
        FootballPlayer p = null;
        while(!valid && shirtNumber != -1){
            messages.informationMessage("Write -1 to return");
            // caso de update atravez da equipa
            if(t!=null){
                messages.normalMessage(t.printPlayers());
                messages.informationMessage("Write the shirt of the player to update");
            }
            //caso de update diretamente no estado
            else{
                messages.normalMessage(footballState.printPlayers());
                messages.informationMessage("Write the name and shirt of the player to update\nInsert the number of the shirt: ");
            }


            try{//pede pela camisola
                shirtNumber = Integer.parseInt(scanner.nextLine());
            }
            catch(NumberFormatException e){
                shirtNumber = -2;
                messages.errorMessage("Insert a valid number");
            }

            if(t!=null){
                if(shirtNumber >=0){
                    if(t.existsShirtNumber(shirtNumber)){
                        valid = true;
                        p = t.getPlayer(shirtNumber);
                    }
                    else messages.errorMessage("Insert a valid player");
                }
            }
            else{
                if(shirtNumber >=0){
                    messages.normalMessage(footballState.printPlayersWithShirt(shirtNumber));
                    messages.informationMessage("Insert the name of the player");
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

    public void randomizeStats(AtomicInteger[] atributes){
        Random random = new Random();
        int i = 0;
           for(;i<8;i++){
               atributes[i].set(random.nextInt(101));
    }

    }

    /**------------------------SIMPLE PRINTS-------------------------------**/
    public void terminate(){
        showTermination();
        System.exit(0);
    }

    public void showWelcome(){
        messages.titleMessage("Welcome to SPORTM!");
        messages.titleMessage("""
                Made by:
                ->Group 68
                ->Goncalo Braz Afonso a93178
                ->Goncalo Jose Teixeira Pereira a93168
                ->Marco Andre Pereira da Costa a93283
                """);

    }

    public void showTermination(){
        messages.titleMessage("Closing the Game\nWe hope you've enjoyed your time.");
    }

}
