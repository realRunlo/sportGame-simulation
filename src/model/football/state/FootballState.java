package model.football.state;
import model.football.game.FootballGame;
import model.football.player.*;
import model.football.team.FootballTeam;
import model.generic.player.Player;
import model.interfaces.Saveable;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;



public class FootballState implements Saveable,Serializable{
    private Map<String, FootballPlayer> playersList; //a key sera um concat do nome do jogador com a sua camisola
    private Map<String, FootballTeam> teams;
    private List<FootballGame> gameHistory;
    private int numbPlayers;
    private int numbTeams;
    private int day;

    private static final int MAX_PLAYER_TEAM = 23;

    /**
     * Construtor de FootballState
     */
    public FootballState(){
        setPlayersList(new HashMap<>());
        setTeams(new HashMap<>());
        setGameHistory(new ArrayList<>());
        setNumbPlayers(0);
        setNTeams(0);
        setDay(0);
    }

    /**
     * Construtor de FootballState
     * @param day dia de criacao do estado
     */
    public FootballState(int day) {
        this.setPlayersList(new HashMap<>());
        this.setTeams(new HashMap<>());
        this.setGameHistory(new ArrayList<>());
        this.setNumbPlayers(0);
        this.setNTeams(0);
        this.setDay(day);
    }

    /**
     * Construtor de FootballState
     * @param newList jogadores disponiveis
     * @param newTeams equipas disponiveis
     * @param newPlayers quantidade de jogadores
     * @param newNumbTeams quantidade de equipas
     * @param newDay dia de criacao do estado
     */
    public FootballState(Map<String, FootballPlayer> newList, Map<String, FootballTeam> newTeams, int newPlayers, int newNumbTeams, int newDay){
        setPlayersList(newList);
        setTeams(newTeams);
        setGameHistory(new ArrayList<>());
        setNumbPlayers(newPlayers);
        setNTeams(newNumbTeams);
        setDay(newDay);
    }

    /**
     * Construtor de FootballState
     * @param s FootballState a copiar
     */
    public FootballState(FootballState s){
        setPlayersList(s.getPlayersList());
        setTeams(s.getTeams());
        setGameHistory(s.getGameHistory());
        setNumbPlayers(s.getNPlayers());
        setNTeams(s.getNTeams());
        setDay(s.getDay());
    }

    /**
     * Getter da lista de jogadores
     * @return lista dos jogadores
     */
    public Map<String, FootballPlayer> getPlayersList(){
        return playersList.entrySet().stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue().clone()));
    }

    /**
     * Setter da lista de jogadores
     * @param newList nova lista de jogadores
     */
    public void setPlayersList(Map<String, FootballPlayer> newList){
        playersList = newList.entrySet().stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue().clone()));
        setNumbPlayers(playersList.size());
    }

    /**
     * Getter da lista de equipas
     * @return lista de equipas
     */
    public Map<String, FootballTeam> getTeams(){
        return teams.entrySet().stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue().clone()));
    }

    /**
     * Rertorna uma Team com o nome indicado
     * @param teamName nome da equipa
     * @return Team
     */
    public FootballTeam getTeam(String teamName){
        return getTeams().get(teamName).clone();
    }

    /**
     * Setter da lista de equipas
     * @param newTeams nova lista de equipas
     */
    public void setTeams(Map<String, FootballTeam> newTeams){
        teams = newTeams.entrySet().stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue().clone()));
        setNTeams(teams.size());
    }

    /**
     * Getter do historico de partidas
     * @return historico de partidas
     */
    public List<FootballGame> getGameHistory(){
        return new ArrayList<>(gameHistory);
    }

    /**
     * Setter do historico de partidas
     * @param newHistory novo historico de partidas
     */
    public void setGameHistory(List<FootballGame> newHistory){
        gameHistory = new ArrayList<>(newHistory);
    }

    /**
     * Getter do numero de jogadores
     * @return numero de jogadores
     */
    public int getNPlayers(){
        return numbPlayers;
    }

    /**
     * Setter do numero de jogadores
     * @param nP novo numero de jogadores
     */
    public void setNumbPlayers(int nP){
        numbPlayers = nP;
    }

    /**
     * Getter do numero de equipas
     * @return numero de equipas
     */
    public int getNTeams(){
        return numbTeams;
    }

    /**
     * Setter do numero de equipas
     * @param nTeams novo numero de equipas
     */
    public void setNTeams(int nTeams){
        numbTeams = nTeams;
    }

    /**
     * Getter do dia atual do estado
     * @return dia atual do estado
     */
    public int getDay(){
        return day;
    }

    /**
     * Setter do dia
     * @param nDay novo dia
     */
    public void setDay(int nDay){
        day = nDay;
    }

    /**
     * Procura um jogador com um certo nome e camisola
     * @param name nome do jogador
     * @param shirt camisola
     * @return FootballPlayer com o nome e camisola indicados
     */
    public FootballPlayer getPlayer(String name, Integer shirt){
        if(playersList.containsKey(name+shirt)){
            return playersList.get(name+shirt).clone();
        }
        return null;
    }

    /**
     * Adiciona um novo jogo no historico
     * @param g jogo a adicionar
     */
    public void addGame(FootballGame g){
        List<FootballGame> gHistory = getGameHistory();
        gHistory.add(g.clone());
        setGameHistory(gHistory);
        day++;
    }

    /**
     * Remove um jogo do historico
     * @param i posicao no historico a remover
     */
    public void removeIndexGame(int i){
        List<FootballGame> gHistory = getGameHistory();
        gHistory.remove(Math.min(i, gHistory.size() - 1));
        setGameHistory(gHistory);
    }

    /**
     * Adiciona um novo jogador, caso esse jogador tenha uma equipa, e adicionado nessa equipa
     * @param p jogador a adicionar
     */
    public void addPlayer(FootballPlayer p){
        Map<String,FootballPlayer> pList = getPlayersList();
        //caso nao exista jogador com o mesmo nome adiciona imediatamente
        if(!pList.containsKey(p.getName())){
            pList.put(p.getName()+p.getNumber(),p.clone());
            setPlayersList(pList);
            addPlayer2Team(p, p.getCurTeam());
        }
        else{//caso seja um jogador com o mesmo nome mas diferente numero de camisola pode adicionar
            if(!(pList.get(p.getName()).getNumber() == p.getNumber())){
                pList.put(p.getName()+p.getName(),p.clone());
                setPlayersList(pList);
                addPlayer2Team(p, p.getCurTeam());
            }
        }
    }

    /**
     * Adiciona um jogador em uma equipa
     * @param p jogador a adicionar
     * @param team equipa a ser adicionado
     */
    public void addPlayer2Team(FootballPlayer p ,String team){
        if(teams.containsKey(team) && p!= null){
            FootballTeam t = getTeam(team);
            if(!t.existsPlayerNumber(p.getName(),p.getNumber()) && t.getNPlayers() < MAX_PLAYER_TEAM){
                //mudar o numero da camisola se necessario
                if(t.existsShirtNumber(p.getNumber())){
                    p.setNumber(findAvailableShirt(p.getName(),t));
                }
                t.addPlayer(p);
                teams.replace(team,t);
            }
        }
    }

    /**
     * Procura um numero de camisola disponivel para um dado nome e equipa
     * verifica se no estado ja existe um jogador com o mesmo nome e camisola
     * @param name nome do jogador
     * @param t equipa do jogador
     * @return numero de camisola valido
     */
    public int findAvailableShirt(String name, FootballTeam t){
        boolean available = false;
        int shirt = 0;
        while(!available){
            if(!existsPlayer(name,shirt) && !t.existsShirtNumber(shirt)) available = true;
            else shirt++;
        }
        return shirt;
    }

    /**
     * Adiciona uma nova equipa
     * se a equipa tiver novos jogadores, adiciona-os no estado
     * @param t equipa a adicionar
     */
    public void addTeam(FootballTeam t){
        Map<String,FootballTeam> tList = getTeams();
        Map<String,FootballPlayer> pList = getPlayersList();
        tList.put(t.getName(), t.clone());
        setTeams(tList);
        t.getPlayers().forEach((k,v)-> updatePlayer(v)); //adiciona-os ao estadoF
    }

    /**
     * Atualiza uma equipa e os seus jogadores
     * @param team equipa a atualizar
     */
    public void updateTeam(FootballTeam team) {
        team.getPlayers().forEach((e,k)-> updatePlayer(k));
        this.teams.replace(team.getName(), team);
    }

    /**
     * Atualiza um jogador
     * @param player jogador a atualizar
     */
    public void updatePlayer(FootballPlayer player) {
        //da update do jogador na lista de jogadores do estado
        if (this.playersList.containsKey(player.getName()+player.getNumber())) {
            if(!playersList.get(player.getName()+player.getNumber()).equals(player)) {
                playersList.replace(player.getName() + player.getNumber(), player);
                //da update, caso necessario, do jogador na propria equipa
                if (teams.containsKey(player.getCurTeam()))
                    if (teams.get(player.getCurTeam()).existsPlayerNumber(player.getName(), player.getNumber())) {
                        teams.get(player.getCurTeam()).updatePlayer(player);
                    }
            }
        }
        //caso nao encontre o jogador no estado, adiciona-o
        else addPlayer(player);
    }

    /**
     * Remove um jogador do estado e da sua equipa
     * @param name nome do jogador a remover
     * @param shirt camisola do jogador a remover
     * @param team equipa do jogador a remover
     */
    public void removePlayer(String name,int shirt, String team){
        Map<String,FootballPlayer> pList = getPlayersList();
        if(pList.containsKey(name+shirt)){
            FootballPlayer p = pList.get(name+shirt);
            if(!p.getCurTeam().equals("None")) { //antes de remover o jogador do jogo, remove-o da equipa
                FootballTeam t = getTeam(p.getCurTeam());
                t.removePlayer(p.getName(),p.getNumber());
                updateTeam(t);
            }
            pList.remove(name+shirt);
            setPlayersList(pList);
        }
    }


    /**
     * Remove uma equipa, removendo todos os seus jogadores dos estado
     * @param team equipa a remover
     */
    public void removeTeam(String team){
        Map<String,FootballTeam> tList = getTeams();
        Map<String,FootballPlayer> pList = getPlayersList();
        if(tList.containsKey(team)) {
            FootballTeam t = getTeam(team);
            if(t.getNPlayers() > 0){
                t.getPlayers().forEach((k,v)-> removePlayer(v.getName(),v.getNumber(),v.getCurTeam())); //caso a equipa ainda tenha jogadores,
            }                                                                                           //remove-os do estado
            tList.remove(team);
            setTeams(tList);
        }
    }

    /**
     * Remove uma equipa, mantendo os jogadores no estado
     * @param team equipa a remover
     */
    public void removeOnlyTeam(String team){
        Map<String,FootballTeam> tList = getTeams();
        if(tList.containsKey(team)) {
            FootballTeam t = getTeam(team);
            if(t.getNPlayers() > 0) {
                t.getPlayers().forEach((k, v) -> removePlayerFromTeam(v)); //caso a equipa ainda tenha jogadores,
            }                                                                                 //remove-os da equipa
            tList.remove(team);
            setTeams(tList);
        }
    }

    /**
     * Remove um jogador da sua equipa, mantendo-o no estado
     * @param p jogador a remover
     */
    public void removePlayerFromTeam(FootballPlayer p){
        if(getTeams().containsKey(p.getCurTeam())){
            FootballTeam t = getTeam(p.getCurTeam());
            if(existsPlayer(p)){
                if (t.existsPlayerNumber(p.getName(),p.getNumber())) {
                    t.removePlayer(p.getName(),p.getNumber());
                }
            updateTeam(t);
            p.setCurTeamNone();
            updatePlayer(p);
            }
        }
    }

    /**
     * Transfere um jogador da sua equipa atual para uma nova equipa caso possivel
     * @param p jogador a transferir
     * @param teamToTransfer equipa de destino
     * @return resultado de sucesso da transferencia
     */
    public boolean transferPlayer(FootballPlayer p, FootballTeam teamToTransfer){
        boolean transfered = false;
        if(p!=null){
            int originalShirt = p.getNumber();
            if(teamToTransfer != null){
                if(teamToTransfer.getNPlayers() < (MAX_PLAYER_TEAM-1) && !p.getCurTeam().equals(teamToTransfer.getName())) {
                    removePlayerFromTeam(p);
                    addPlayer2Team(p, teamToTransfer.getName());
                    transfered = true;
                }
            }
            else{
                removePlayerFromTeam(p);
                transfered = true;
            }
            if(originalShirt != p.getNumber()) playersList.remove(p.getName()+originalShirt);
            updatePlayer(p);
        }
        return transfered;
    }

    /**
     * Incrementa o numero de jogadores
     */
    public void incNPlayers(){
        numbPlayers++;
    }

    /**
     * Decrementa o numero de jogadores
     */
    public void decNPlayers(){
        numbPlayers--;
    }

    /**
     * Incrementa o numero de equipas
     */
    public void incNTeams(){
        numbTeams++;
    }

    /**
     * Decrementa o numero de equipas
     */
    public void decNTeams(){
        numbTeams--;
    }

    /**
     * Verifica se um dado jogador existe no estado
     * @param p jogador a procurar
     * @return resultado da procura
     */
    public boolean existsPlayer(FootballPlayer p){
        Map<String,FootballPlayer> pList = getPlayersList();
        for(FootballPlayer player: pList.values()){
            if(p.equals(player)) return true;
        }
        return false;
    }

    /**
     * Verifica se existe um jogador com um dado nome e camisola no estado
     * @param name nome do jogador a procurar
     * @param shirt camisola do jogador a procurar
     * @return resultado da procura
     */
    public boolean existsPlayer(String name, Integer shirt){
        if(playersList.containsKey(name+shirt)){
            return playersList.get(name+shirt).getName().equals(name);
        }
        return false;
    }

    /**
     * Procura se existe uma equipa com o dado nome no estado
     * @param name nome da equipa a procurar
     * @return resultado da procura
     */
    public boolean existsTeam(String name){
        return teams.containsKey(name);
    }

    /**
     * Cria um novo jogo entre duas equipas
     * @param team1 equipa da casa
     * @param team2 equipa visitante
     */
    public void createGame(String team1, String team2){
        Map<String,FootballTeam> tList = getTeams();
        //Se as equipas existirem e tiverem jogadores suficientes e adicionado um novo jogo
        if(tList.containsKey(team1) && tList.containsKey(team2)){
            FootballTeam t1 = getTeam(team1);
            FootballTeam t2 = getTeam(team2);
            if(t1.getNPlayers() == MAX_PLAYER_TEAM && t2.getNPlayers() == MAX_PLAYER_TEAM){
                FootballGame g = new FootballGame(t1,1,t2,1);
                addGame(g);
            }
        }
    }

    /**
     * Grava o estado num ficheiro de objetos
     * @param filename ficheiro onde gravar
     * @throws IOException
     */
    public void saveState(String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.flush();
        oos.close();
    }

    /**
     * Torna a lista de jogadores em uma string, escrevendo o seu nome e habilidade
     * @param showTeam indica se deve ser incluido a equipa do jogador para cada jogador
     * @return string com a lista de jogadores
     */
    public String printPlayers(boolean showTeam){
        StringBuilder sb = new StringBuilder();
        AtomicInteger changeLine = new AtomicInteger(0);
        int limit = 10;
        if(showTeam) limit = 5;
        sb.append("Available Players:\n\t");
        int finalLimit = limit;
        playersList.values().forEach(k-> {
            if(changeLine.get() == finalLimit){
                sb.append("\n\t");
                changeLine.set(0);
            }
            else changeLine.set(changeLine.get()+1);
            sb.append(k.getName())
                    .append(" - ")
                    .append(k.getNumber());
            if(showTeam) sb.append(" - ").append(k.getCurTeam());
            sb.append(" , ");
        });
        return sb.append("\n").toString();
    }

    /**
     * Torna a lista de jogadores em uma string, escrevendo apenas os jogadores com a camisola
     * indicada como argumento
     * @param shirt camisola a filtrar
     * @param showTeam indica se deve ser incluido a equipa do jogador para cada jogador
     * @return string com a lista de jogadores
     */
    public String printPlayersWithShirt(int shirt,boolean showTeam){
        StringBuilder sb = new StringBuilder();
        AtomicInteger changeLine = new AtomicInteger(0);
        int limit = 10;
        if(showTeam) limit = 5;
        sb.append("Available Players:\n\t");
        int finalLimit = limit;

        playersList.values().stream().filter(e->e.getNumber() == shirt)
                .forEach(k-> {
                    if(changeLine.get() == finalLimit) {
                        sb.append("\n\t");
                        changeLine.set(0);
                    }
                    else changeLine.set(changeLine.get()+1);
                    sb.append(k.getName())
                            .append(" - ")
                            .append(k.getNumber());
                    if(showTeam) sb.append(" - ").append(k.getCurTeam());
                    sb.append(" , ");
                });
        return sb.append("\n").toString();
    }

    /**
     * Torna a lista de equipas em uma string, gravando o seu nome e habilidade media
     * @return string com lista de equipas
     */
    public String printTeams(){
        StringBuilder sb = new StringBuilder();
        AtomicInteger changeLine = new AtomicInteger(0);
        sb.append("Available Teams:\n\t");
        teams.forEach((e,k) -> {
            if(changeLine.get() == 10){
                sb.append("\n\t");
                changeLine.set(0);
            }
            else changeLine.set(changeLine.get()+1);
            sb.append(e)
                    .append(" - ")
                    .append(k.calcAverageSkill())
                    .append(", ");
        });
        return sb.append("\n").toString();
    }

    /**
     * Torna o historico de jogos em uma string, gravando o seu score e respetivos planteis das equipas
     * que jogaram
     * @return string com historico de jogos
     */
    public String printGameHistory(){
        StringBuilder sb = new StringBuilder();
        sb.append("**********************************************\n")
                .append("\t\t\t\tGame History\n");
        gameHistory.forEach(k ->
                sb.append(k.toString()).append("\n"));
        sb.append("\n**********************************************\n");
        return sb.toString();
    }


    /**
     * Equals da classe FootballState
     * @param o objeto a comparar
     * @return resultado da comparacao
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FootballState s = (FootballState) o;

        return this.getPlayersList().equals(s.getPlayersList()) &&
                this.getTeams().equals(s.getTeams()) &&
                this.getGameHistory().equals(s.getGameHistory()) &&
                this.getNPlayers() == s.getNPlayers() &&
                this.getNTeams() == s.getNTeams() &&
                this.getDay() == s.getDay();
    }

    /**
     * Clone da classe FootballState
     * @return clone
     */
    @Override
    public FootballState clone(){
        return new FootballState(this);
    }

    /**
     * ToString da classe FootballState
     * @return FootballState em formato string
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n*************************************\n")
        .append("\t\t\t\tState\n")
                .append("Number of players: ").append(getNPlayers());
        sb.append("\n").append(printPlayers(false));

        sb.append("\nNumber of teams: ").append(getNTeams())
        .append("\n").append(printTeams())
        .append("\nDays: ").append(getDay())
        .append("\n*************************************\n");

        return sb.toString();
    }

    /**
     * Torna FootballState em uma string saveable
     * @return string saveable
     */
    @Override
    public String toCSV() {
        return "Estado:" + this.getDay() + "\n";
    }

    /**
     * Grava o estado em um ficheiro de texto
     * @param filePath ficheiro em que vai gravar
     * @throws IOException
     */
    @Override
    public void save(String filePath) throws IOException {
        BufferedWriter br = new BufferedWriter(new FileWriter(filePath));

        br.write("Our format\n");
        br.write(this.toCSV());
        br.flush();
        br.close();

        for(FootballTeam ft: this.getTeams().values()) {
            ft.save(filePath);
        }
        for(FootballPlayer fp: this.getPlayersList().values()) {
            fp.save(filePath);
        }
        for(FootballGame fg: this.getGameHistory()) {
            fg.save(filePath);
        }
    }

    /**
     * Carrega um estado a partir de um ficheiro de texto
     * @param filePath ficheiro a analisar
     * @return estado carregado
     * @throws IOException
     */
    public static FootballState load(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        String[] split;
        FootballState fs;
        String lastTeam = "";
        boolean teacher = false;
        int day = 0;

        br.mark(1024);
        line = br.readLine();
        if(line.equals("Our format")) {
            line = br.readLine();
            split = line.split(":");
            fs = new FootballState(Integer.parseInt(split[1]));
        }
        else {
            teacher = true;
            fs = new FootballState(day);
            br.reset();
        }
        while((line = br.readLine()) != null) {
            split = line.split(":");

            switch (split[0]) {
                case "Defesa" -> {
                    Defender d = Defender.load(split[1], lastTeam, teacher);
                    fs.addPlayer(d);
                    fs.addPlayer2Team(d, d.getCurTeam());
                }
                case "Lateral" -> {
                    Lateral l = Lateral.load(split[1], lastTeam, teacher);
                    fs.addPlayer(l);
                    fs.addPlayer2Team(l, l.getCurTeam());
                }
                case "Medio" -> {
                    Midfielder m = Midfielder.load(split[1], lastTeam, teacher);
                    fs.addPlayer(m);
                    fs.addPlayer2Team(m, m.getCurTeam());
                }
                case "Avancado" -> {
                    Striker s = Striker.load(split[1], lastTeam, teacher);
                    fs.addPlayer(s);
                    fs.addPlayer2Team(s, s.getCurTeam());
                }
                case "Guarda-Redes" -> {
                    Goalkeeper g = Goalkeeper.load(split[1], lastTeam, teacher);
                    fs.addPlayer(g);
                    fs.addPlayer2Team(g, g.getCurTeam());
                }
                case "Equipa" -> {
                    FootballTeam ft = FootballTeam.load(split[1]);
                    fs.addTeam(ft);
                    lastTeam = ft.getName();
                }
                case "Jogo" -> {
                    FootballGame fg = FootballGame.load(split[1],fs.getTeams());
                    fs.addGame(fg);
                    day++;
                }
            }
        }
        if(teacher)
            fs.setDay(day);
        return fs;
    }
}
