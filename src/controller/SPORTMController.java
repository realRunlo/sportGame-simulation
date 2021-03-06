package controller;
import model.exceptions.PlayerDoenstExist;
import model.football.foul.Card;
import model.football.game.ExecuteFootballGame;
import model.football.game.FootballGame;
import model.football.player.*;
import model.football.state.FootballState;
import model.football.team.FootballTeam;
import model.football.team.lineup.FootballLineup;
import viewer.SPORTMViewer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public class SPORTMController{
    private FootballState footballState;
    private final Scanner scanner;
    private final SPORTMViewer messages = new SPORTMViewer(new String[]{});
    private static final int MaxNumberOfPlayers = 22;
    private static final long quickSleep = 500;
    private static final long averageSleep = 1000;
    private static final long slowSleep = 1500;
    private static final int Max_Substitutions = 3;
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
            "Save Team",
            "AutoFill Team"
    };
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

    /**
     * Construtor do controller
     */
    public SPORTMController(){
        footballState = new FootballState();
        scanner = new Scanner(System.in);
    }


    /**
     * Unico metodo publico da classe, coloca a correr o programa
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void run() throws IOException, ClassNotFoundException {
        showWelcome();
        SPORTMViewer initialMenu = new SPORTMViewer(InitialMenu);
        initialMenu.setHandler(1,()->loadState());
        initialMenu.setHandler(2,()->EnterState());
        initialMenu.setHandler(3,()->terminate());
        initialMenu.SimpleRun();
    }


    /**------------------------LOAD STATE-------------------------------**/

    /**
     * Menu que permite a leitura de um estado previamente gravado
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void loadState() throws IOException, ClassNotFoundException {
        SPORTMViewer loadMenu = new SPORTMViewer(LoadStateMenu);
        loadMenu.setHandler(1,()-> defaultState(loadMenu));
        loadMenu.setHandler(2,()-> costumState(loadMenu));
        loadMenu.SimpleRun();
    }

    /**
     * Opcao que le um estado escolhido por default
     * @param viewer menu de load de estado
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void defaultState(SPORTMViewer viewer) throws IOException, ClassNotFoundException {
        try {
            this.footballState = FootballState.load("logs.txt");
        } catch(FileNotFoundException e){
            messages.errorMessage("File not found");
        }
        catch(IOException e){
            messages.errorMessage("Error accessing the file");
        }
        viewer.returnMenu();
    }

    /**
     * Opcao que le um estado de um ficheiro escolhido pelo utilizador
     * @param viewer menu de load de estado
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void costumState(SPORTMViewer viewer) throws IOException, ClassNotFoundException {
        try {
            this.footballState = FootballState.load(getName());
        }
        catch (FileNotFoundException e){
            messages.errorMessage("File not found");
        }
        catch (IOException e){
            messages.errorMessage("Error accessing the file");
        }
        viewer.returnMenu();
    }

    /**------------------------ENTER STATE-------------------------------**/

    /**
     * Menu principal do jogo onde se pode adicionar/editar jogadores e equipas, vizualizar as entidades
     * do programa, criar novas partidas, gravar o estado e realizar transferencias entre equipas
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void EnterState() throws IOException, ClassNotFoundException {
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
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        enterState.setHandler(3,()-> enterState.showInfo(this.footballState.printGameHistory()));
        enterState.setHandler(4,()-> addOrUpdateTeam(false));
        enterState.setHandler(5,()-> addOrUpdatePlayer(null,true,true));
        enterState.setHandler(6,()-> addOrUpdateTeam(true));
        enterState.setHandler(7,()-> addOrUpdatePlayer(null,true,false));
        enterState.setHandler(8,()-> transferPlayer());
        enterState.setHandler(9,()-> removeTeam());
        enterState.setHandler(10,()-> removePlayer());
        enterState.setHandler(11,()-> footballState.save(getName()));
        enterState.SimpleRun();
    }

    /**
     * Menu de update/criacao de uma equipa, com opcoes de editar os jogadores dentro dessa equipa
     * @param update booleano que indica se a equipa esta a ser criada ou atualizada
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void addOrUpdateTeam(boolean update) throws IOException, ClassNotFoundException {
        FootballTeam team;
        if(update) team = chooseTeam("Insert a team to update");
        else team = new FootballTeam();
        if(team != null) {
            SPORTMViewer teamMenu = new SPORTMViewer(TeamMenu);
            //caso seja so um update, nao permite mudar o nome da equipa
            if (update) teamMenu.setPreCondition(1, () -> false);
            else teamMenu.setPreCondition(1, () -> team.getName().equals(" "));
            teamMenu.setSamePreCondition(new int[]{2, 6}, () -> !team.getName().equals(" "));
            teamMenu.setSamePreCondition(new int[]{3, 7}, () -> !team.getName().equals(" ") && team.getNPlayers() < MaxNumberOfPlayers);
            teamMenu.setSamePreCondition(new int[]{4, 5}, () -> team.getNPlayers() > 0);



            teamMenu.setHandler(1, () -> changeTeamName(team));
            teamMenu.setHandler(2, () -> teamMenu.showInfo(team));
            teamMenu.setHandler(3, () -> addOrUpdatePlayer(team, false,true));
            teamMenu.setHandler(4, () -> addOrUpdatePlayer(team, false,false));
            teamMenu.setHandler(5, () -> removePlayerTeam(team));
            teamMenu.setHandler(6, () -> updateTeamState(team, !update, teamMenu));
            teamMenu.setHandler(7, () -> autoFillTeam(team));
            teamMenu.SimpleRun();
        }
    }

    /**
     * Metodo que recebe o nome dado por um utilizador para uma dada equipa
     * @param t equipa a mudar o nome
     */
    private void changeTeamName(FootballTeam t){
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

    /**
     * Metodo que automaticamente cria os jogadores restantes, nas posicoes necessarias,
     * em uma equipa
     * @param team equipa a adicionar os jogadores
     */
    private void autoFillTeam(FootballTeam team){
        String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
        Random rand = new Random();

        while(team.getNPlayers() < 22){
            AtomicInteger[] atributes = new AtomicInteger[]
                    {
                            new AtomicInteger(rand.nextInt(101)),//0 - speed
                            new AtomicInteger(rand.nextInt(101)),//1 - resistance
                            new AtomicInteger(rand.nextInt(101)),//2 - dexterity
                            new AtomicInteger(rand.nextInt(101)),//3 - implosion
                            new AtomicInteger(rand.nextInt(101)),//4 - headGame
                            new AtomicInteger(rand.nextInt(101)),//5 - kick
                            new AtomicInteger(rand.nextInt(101)),//6 - passing
                            new AtomicInteger(rand.nextInt(101)),//7 - typeSpecificSkill
                            new AtomicInteger(-1),//8 - type
                            new AtomicInteger(-1)//9 - shirt
                    };
            StringBuilder newName = new StringBuilder();
            for(int i = 0; i < 10; i++) {
                newName.append(lexicon.charAt(rand.nextInt(37)));
            }
            String name = newName.toString();
            atributes[8].set(team.typeNeeded());
            atributes[9].set(footballState.findAvailableShirt(name,team));
            updatePlayerState(playerPrototype(Arrays.stream(atributes).mapToInt(e->e.get()).toArray(),name,team,new ArrayList<String>()),team,null,true,false);
        }
    }

    /**
     * Menu de update/criacao de um jogador
     * @param t equipa onde adicionar o jogador, caso seja nula ele nao tera equipa
     * @param updateState booleano que indica se o jogador vai ser diretamente adicionado no estado
     * @param newPlayer booleano que indica se o jogador esta a ser criado ou atualizado
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void addOrUpdatePlayer(FootballTeam t,boolean updateState,boolean newPlayer) throws IOException, ClassNotFoundException {
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
        AtomicReference<FootballTeam> team = new AtomicReference<>();
        team.set(t);
        FootballPlayer p = null;
        AtomicReference<List<String>> background = new AtomicReference<>();
        background.set(new ArrayList<>());
        if(!newPlayer) {
            p = choosePlayerToUpdate(t,true);
            if(p!=null) {
                name.set(p.getName());
                background.set(p.getBackground()) ;
                if (t == null && footballState.existsTeam(p.getCurTeam())) {
                    team.set(footballState.getTeam(p.getCurTeam()));
                }

                atributes[0].set(p.getSpeed());//0 - speed
                atributes[1].set(p.getResistance());//1 - resistance
                atributes[2].set(p.getDexterity());//2 - dexterity
                atributes[3].set(p.getImplosion());//3 - implosion
                atributes[4].set(p.getHeadGame());//4 - headGame
                atributes[5].set(p.getKick());//5 - kick
                atributes[6].set(p.getPassing());//6 - passing
                atributes[7].set(getSpecialSkill(p));//7 - typeSpecificSkill
                atributes[8].set(typeOfPlayer(p));//8 - type
                atributes[9].set(p.getNumber());//9 - shirt
            }
        }
        if(p!=null || newPlayer) {
            SPORTMViewer playerMenu = new SPORTMViewer(PlayerMenu);
            //apenas pode mudar a camisola e o tipo de jogador se ja lhe tiver dado um nome
            playerMenu.setSamePreCondition(new int[]{3, 4}, () -> !name.get().equals(""));

            if(!newPlayer) playerMenu.setSamePreCondition(new int[]{1,4},()->false);

            playerMenu.setPreCondition(13, () -> !name.get().equals("") && atributes[9].get() != -1);
            playerMenu.setPreCondition(2, () -> atributes[8].get() != -1);
            //apenas pode mudar as skills do jogador,ver informacao sobre ele, se ja tiver escolhido o tipo de jogador
            playerMenu.setSamePreCondition(new int[]{2, 5, 6, 7, 8, 9, 10, 11, 12, 14}, () -> !name.get().equals("") && atributes[8].get() != -1 && atributes[9].get() != -1);

            playerMenu.setHandler(1, () -> name.set(getName()));
            playerMenu.setHandler(2, () -> playerMenu.showInfo(playerPrototype(Arrays.stream(atributes).mapToInt(e -> e.get()).toArray(), name.get(), team.get(), background.get())));
            playerMenu.setHandler(3, () -> atributes[8].set(playerMenu.readOptionBetween(0, 4, new String[]{"Goalkeeper", "Defender", "Lateral", "Midfielder", "Striker"})));
            playerMenu.setHandler(4, () -> atributes[9].set(getShirtNumber(name.get(), t)));
            playerMenu.setHandler(14, () -> randomizeStats(atributes));
            //handlers dos atributos de um jogador
            for (int i = 0, j = 5; i < 8; i++, j++) {
                int finalI = i;
                playerMenu.setHandler(j, () -> atributes[finalI].set(getSkillValue(playerMenu, "Current Value: " + atributes[finalI].get())));
            }
            //gravar o jogador
            playerMenu.setHandler(13, () -> updatePlayerState(playerPrototype(Arrays.stream(atributes).mapToInt(e -> e.get()).toArray(), name.get(), team.get(), background.get()), team.get(), playerMenu, newPlayer, updateState));
            playerMenu.SimpleRun();
        }
    }


    /**
     * Metodo para o utilizador escolher uma equipa
     * @param message mensagem que muda dependendo do menu que chama este metodo
     * @return equipa escolhida
     */
    private FootballTeam chooseTeam(String message) {
        FootballTeam t = null;
        String name = "";
        boolean valid = false;
        while (!valid && !name.equals("-1")) {
            messages.normalMessage(footballState.printTeams());
            messages.informationMessage("Write -1 to return");
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

    /**
     * Metodo que executa a opcao de transferencia de um jogador entre equipas
     */
    private void transferPlayer(){
        FootballPlayer p = choosePlayerToUpdate(null,true);
        if(p!=null) {
            FootballTeam teamToTransfer = chooseTeam(
                    "Insert a team to transfer the player to\n" +
                            "Write -1 if you just wish to remove him from his team"
            );
            if(footballState.transferPlayer(p,teamToTransfer)) messages.confirmationMessage("Transfer successful");
            else messages.errorMessage("Can't transfer the player to that team");
        }
    }

    /**
     * Menu de remocao de uma equipa do estado, permite remover apenas a equipa
     * ou remover tambem os jogadores nela incluidos
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void removeTeam() throws IOException, ClassNotFoundException {
        SPORTMViewer menu = new SPORTMViewer(RemoveTeamMenu);
        FootballTeam t = chooseTeam("Choose the Team to remove");
        if(t!=null) {
            menu.setHandler(1, () -> removeTeam(menu,t,false));
            menu.setHandler(2, () -> removeTeam(menu,t,true));
            menu.SimpleRun();
        }
    }

    /**
     * Metodo de remocao de equipa
     * @param menu  menu de remocao de equipa
     * @param t equipa a remover
     * @param removeAll booleano que indica se foi escolhido a remocao dos jogadores do estado
     */
    private void removeTeam(SPORTMViewer menu, FootballTeam t,boolean removeAll){
        if(removeAll) footballState.removeTeam(t.getName());
        else footballState.removeOnlyTeam(t.getName());
        menu.returnMenu();
    }

    /**
     * Metodo de remocao de um jogador
     */
    private void removePlayer(){
        FootballPlayer p = choosePlayerToUpdate(null,true);
        if(p!=null) footballState.removePlayer(p.getName(),p.getNumber(),p.getCurTeam());
    }


    /**
     * Metodo que executa uma nova partida
     * @throws PlayerDoenstExist
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    private void createGame() throws PlayerDoenstExist, IOException, ClassNotFoundException, InterruptedException {
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
            messages.titleMessage("********************************\n" +
                    "\t\t"+homeTeam.getName()+" VS "+visitingTeam.getName());

            FootballLineup homeLineup = makeLineup(homeTeam,true);
            if(homeLineup != null && homeLineup.readyToPlay()){
                FootballLineup visitorLineup = makeLineup(visitingTeam,false);
                if(visitorLineup != null && visitorLineup.readyToPlay()){
                    FootballGame g = new FootballGame(homeTeam,visitingTeam,homeLineup,visitorLineup);
                    ExecuteFootballGame play = new ExecuteFootballGame(g);

                    messages.informationMessage("Choose the speed of the game");
                    int speed = messages.readOptionBetween(0,3,new String[]{"Slow","Medium","Fast","No Report"});
                    int messageIndex = 0;
                    List<String> gameReport = new ArrayList<>();
                    String message;
                    boolean halfTime = false;

                    while(play.getTimer() <= 90) {
                        play.ExecutePlay();
                        gameReport = play.getGameReport();
                        if (speed != 3) {
                            while (messageIndex < gameReport.size()) {
                                message = gameReport.get(messageIndex);
                                messages.normalMessage(play.getGame().getTimer() + "': "+ message);
                                sleepDependingOnMessage(message,speed);
                                messageIndex++;
                            }
                        }
                        //intervalo para permitir fazer substituicoes
                        if(play.getTimer() >= 45 && !halfTime){
                            messages.titleMessage("\n\t\t\tHalf Time\n\t\t\t  "+
                                    play.getGame().getPoints1() +" - "+play.getGame().getPoints2());
                            play.setLineup(substitute(homeLineup,true),true);
                            play.setLineup(substitute(visitorLineup,false),false);
                            halfTime = true;
                        }
                    }

                    messages.normalMessage("-------------\n"+play.getGame().toString()+"\n-----------------\n");
                    messages.informationMessage("Press enter to proceed");
                    scanner.nextLine();
                    play.getGame().setBol(false);
                    footballState.addGame(play.getGame());
                }else messages.errorMessage("Error with lineup, make sure there is at least 1 player of each type in the team");
            }else messages.errorMessage("Error with lineup, make sure there is at least 1 player of each type in the team");
        }
        messages.titleMessage(
                "--------------------------------------\n" +
                "---------------Returning--------------\n" +
                "--------------------------------------\n"
        );
    }

    /**
     * Metodo usado para criar pausas dependendo das acoes retornadas pelo jogo a decorrer
     * @param message
     * @param speed
     * @throws InterruptedException
     */
    private void sleepDependingOnMessage(String message, int speed) throws InterruptedException {
        if (message.contains("steal")
                || message.contains("pass")
                || message.contains("score")
                || message.contains("shoots")
        ) Thread.sleep(quickSleep / (speed + 1));
        else if (message.contains("now has"))
            Thread.sleep(averageSleep / (speed + 1));
        else if (message.contains("GOOOOOOO"))
            Thread.sleep(slowSleep / (speed + 1));
    }


    /**
     * Menu de criacao de um plantel
     * @param t equipa cujo plantel esta a ser criado
     * @param home  booleano que indica se se trata da equipa que joga em casa
     * @return a lienup criada
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private FootballLineup makeLineup(FootballTeam t,boolean home) throws IOException, ClassNotFoundException {
        SPORTMViewer lineupMenu = new SPORTMViewer(LineupMenu);

        if(home) lineupMenu.titleMessage("Home Team");
        else lineupMenu.titleMessage("Visitor Team");

        AtomicInteger strategy = new AtomicInteger(-1);
        AtomicReference<FootballLineup> protoLineup = new AtomicReference<>(new FootballLineup());
        FootballLineup lineup = null;
        AtomicBoolean saved = new AtomicBoolean(false);

        lineupMenu.setSamePreCondition(new int[]{2,3},()->strategy.get()!=-1);
        lineupMenu.setSamePreCondition(new int[]{4},()-> protoLineup.get().getPlaying().size() > 0);
        lineupMenu.setPreCondition(5,()-> protoLineup.get().readyToPlay());
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


    /**
     * Metodo que permite ao utilizador escolher os titulares de um plantel
     * @param proto plantel escolhida no momento
     * @param t equipa correspondente ao plantel
     */
    private void setPlaying(AtomicReference<FootballLineup> proto, FootballTeam t){
        boolean saved = false;
        String line = "";
        int shirt = -1;
        FootballLineup l = proto.get();

        while(!saved && !line.equals("-1")){
            messages.normalMessage(t.printPlayers());
            messages.informationMessage("Write -1 to return, 's' to save\nWrite 'a' to add a player and 'r' to remove");
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
                    } catch (NumberFormatException e) { // N??o foi inscrito um int
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

    /**
     * Metodo que permite ao utilizador escolher os suplentes
     * @param proto plantel escolhido ate ao momento
     * @param t equipa correspondente ao plantel
     */
    private void setSubstitutes(AtomicReference<FootballLineup> proto, FootballTeam t){
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
                    } catch (NumberFormatException e) { // N??o foi inscrito um int
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

    /**
     * Metodo que permite ao utilizador realizar uma substituicao em um plantel
     * @param lineup plantel em jogo
     * @param home booleano que indica se o plantel corresponde a equipa da casa ou visitante
     * @return plantel modificado
     */
    private FootballLineup substitute(FootballLineup lineup,boolean home){
        String line = "";
        while(!line.equals("y") && !line.equals("n")) {
            if (home) messages.informationMessage("Home Team, do you wish to make susbtitutions?[y/n]");
            else messages.informationMessage("Visitor Team, do you wish to make susbtitutions?[y/n]");
            line = scanner.nextLine();
        }
        if(line.equals("y")){
            boolean finish = false;
            int subs = 0;
            int playing = -1, substitute = -1;
            while(!line.equals("-1") && !finish && subs < Max_Substitutions && lineup.numberOf(false) > 0) {
                messages.normalMessage(lineup.printPlaying());
                messages.normalMessage(lineup.printSubstitutes());
                messages.informationMessage("Write -1 to return or 's' to save");
                messages.informationMessage("You have " + (Max_Substitutions - subs) + " left");
                messages.informationMessage("Choose a playing player to substitute");
                try {
                    line = scanner.nextLine();
                    playing = Integer.parseInt(line);
                } catch (NumberFormatException e) { // N??o foi inscrito um int
                    playing = -1;
                }
                if(line.equals("s")) finish = true;
                if(!finish && playing >=0){
                    if( lineup.existsPlayer(playing,true)){
                        messages.informationMessage("Choose the substitute");
                        try {
                            line = scanner.nextLine();
                            substitute = Integer.parseInt(line);
                        } catch (NumberFormatException e) { // N??o foi inscrito um int
                            substitute = -1;
                        }
                        if(substitute >= 0 && substitute != playing && lineup.existsPlayer(substitute,false)){
                            if(lineup.substitutePlayer(playing,substitute))
                            subs++;
                            else messages.errorMessage("Invalid substitution");
                        }else messages.errorMessage("Invalid shirt");
                    }else messages.errorMessage("Invalid shirt");
                }
            }
            if(finish || subs == Max_Substitutions) return lineup;
        }
        return null;
    }

    /**
     * Metodo que indica que foi escolhido a gravacao da lineup
     * @param saved booleano que indica se foi escolhido para gravar
     * @param menu menu de criacao de lineup
     */
    private void saveLineup(AtomicBoolean saved ,SPORTMViewer menu){
        saved.set(true);
        menu.returnMenu();
    }



    /**------------------------General methods-----------------------------**/

    /**
     * Metodo que pede ao utilizador que indique um nome
     * @return nome escolhido pelo utilizador
     */
    private String getName(){
        messages.informationMessage("Insert a name");
        return scanner.nextLine();
    }

    /**
     * Metodo que pede ao utilizador que indique um numero de camisola para um dado jogador
     * @param name nome do jogador em questao
     * @param t equipa, caso exista do jogador
     * @return numero de camisola
     */
    private Integer getShirtNumber(String name, FootballTeam t){
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

    /**
     * Opcao de modificacao de uma habilidade de um jogador
     * @param menu menu de edicao de um jogador
     * @param currentValue valor atual da habilidade para comparacao
     * @return novo valor, escolhido pelo utilizador
     */
    private int getSkillValue(SPORTMViewer menu,String currentValue){
        messages.informationMessage(currentValue);
        return menu.readOptionBetween(0, 100, null);
    }

    /**
     * Metodo que atualiza ou grava um jogador no estado/equipa
     * @param p jogador a gravar/atualizar
     * @param t equipa do jogador
     * @param viewer menu de edicao do jogador
     * @param newPlayer booleano que indica se este e um jogador novo
     * @param updateState booleano que indica se apenas se trata de uma atualizacao
     */
    private void updatePlayerState(FootballPlayer p,FootballTeam t, SPORTMViewer viewer,boolean newPlayer,boolean updateState){
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

    /**
     * Metodo de atualizacao/criacao de uma equipa
     * @param t equipa a gravar/atualizar
     * @param newTeam booleano que indica se se trata de uma nova equipa
     * @param viewer menu de edicao de equipa
     */
    private void updateTeamState(FootballTeam t, boolean newTeam,SPORTMViewer viewer){
        if(newTeam) footballState.addTeam(t);
        else footballState.updateTeam(t);
        if(viewer != null) viewer.returnMenu();
    }

    /**
     * Metodo que permite a remocao de um jogador de uma equipa
     * @param t equipa do jogador
     */
    private void removePlayerTeam(FootballTeam t){
        boolean removed = false; int op = -2;
        while(!removed && op!=-1){
            messages.normalMessage(t.toString());
            messages.informationMessage("Write '-1' to return\nWrite the player's shirt to remove him from the team");
            try {
                String line = scanner.nextLine();
                op = Integer.parseInt(line);
            }
            catch (NumberFormatException e) { // N??o foi inscrito um int
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


    /**
     * Metodo de escolha de um jogador para atualizar
     * @param t equipa de onde se escolhe um jogador
     * @param showTeam booleano que indica se juntamente ao nome do jogador, se deve mostrar a equipa
     *                 a que pertence
     * @return jogador escolhido pelo utilizador
     */
    private FootballPlayer choosePlayerToUpdate(FootballTeam t,boolean showTeam){
        int shirtNumber = -2;
        String name = "";
        boolean valid = false;
        FootballPlayer p = null;
        while(!valid && shirtNumber != -1){
            // caso de update atravez da equipa
            if(t!=null){
                messages.normalMessage(t.printPlayers());
                messages.informationMessage("Write -1 to return");
                messages.informationMessage("Write the shirt of the player to update");
            }
            //caso de update diretamente no estado
            else{
                messages.normalMessage(footballState.printPlayers(showTeam));
                messages.informationMessage("Write -1 to return");
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
                    messages.normalMessage(footballState.printPlayersWithShirt(shirtNumber,true));
                    messages.informationMessage("Insert the name of the player");
                    name = scanner.nextLine();
                    if(footballState.existsPlayer(name,shirtNumber)){
                        valid = true;
                        p = footballState.getPlayer(name,shirtNumber);
                    }
                    else messages.errorMessage("Invalid player");
                }

            }
        }
        return p;
    }

    /**
     * Metodo que permite criar um jogador com as opcoes escolhidas ate ao momento pelo utilizador
     * @param atributes atributos como definidos ate ao momento
     * @param name nome do jogador
     * @param t equipa do jogador
     * @param background lista de equipas por onde o jogador passou
     * @return novo jogador conforme os atributos escolhidos
     */
    private FootballPlayer playerPrototype(int [] atributes, String name, FootballTeam t,List<String> background)
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

    /**
     * Metodo utilizador para descobrir o tipo de jogador
     * @param p jogador a analisar
     * @return inteiro correspondente ao tipo de jogador
     */
    int typeOfPlayer(FootballPlayer p){
        int type = -1;
        if(p instanceof Goalkeeper) type = 0;
        if(p instanceof Defender) type = 1;
        if(p instanceof Lateral) type = 2;
        if(p instanceof Midfielder) type = 3;
        if(p instanceof Striker) type = 4;
        return type;
    }

    /**
     * Metodo que retorna a habilidade especial dependendo do tipo de jogador
     * @param p jogador cuja habilidade especial se pretende obter
     * @return valor da habilidade
     */
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

    /**
     * Metodo que randomiza todos os atributos de um jogador
     * @param atributes lista dos atributos a randomizar
     */
    private void randomizeStats(AtomicInteger[] atributes){
        Random random = new Random();
        int i = 0;
           for(;i<8;i++){
               atributes[i].set(random.nextInt(101));
    }

    }

    /**------------------------SIMPLE PRINTS-------------------------------**/
    /**
     * Metodo que imprime uma mensagem de despedida e termina o programa
     */
    private void terminate(){
        showTermination();
        System.exit(0);
    }

    /**
     * Metodo que mostra uma mensagem de saudacao na entrada do programa
     */
    private void showWelcome(){
        messages.titleMessage("Welcome to SPORTM!");
        messages.titleMessage("""
                Made by:
                ->Group 68
                ->Goncalo Braz Afonso a93178
                ->Goncalo Jose Teixeira Pereira a93168
                ->Marco Andre Pereira da Costa a93283
                """);

    }

    /**
     * Mensagem de despedida
     */
    private void showTermination(){
        messages.titleMessage("Closing the Game\nWe hope you've enjoyed your time.");
    }

}
