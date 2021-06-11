package model.football.team;

import model.football.player.*;
import model.generic.team.Team;
import model.interfaces.Saveable;

import java.util.*;
import java.util.stream.Collectors;

public class FootballTeam extends Team implements Saveable {
    private Map<Integer, FootballPlayer> players;
    private int AverageOverlSkill;
    private int Nplayers;
    private static final int NumberOfPlayers = 23;

    /**
     * Contrutor de FootballTeam
     */
    public FootballTeam(){
        super();
        players = new HashMap<>();
        AverageOverlSkill = 0;
        Nplayers = 0;
    }

    /**
     * Construtor de FootballTeam
     * @param name nome da equipa
     */
    public FootballTeam(String name) {
        super(name);
        this.setPlayers(new HashMap<>());
        this.setAverageOverlSkill(0);
        this.setNplayers(0);
        this.setNplayers(0);
    }

    /**
     * Construtor de FootballTeam
     * @param name nome da equipa
     * @param GlobalSkill habilidade da equipa
     * @param newPlayers lista dos jogadores
     * @param newAverage habilidade media
     * @param newNplayers numero de jogadores
     */
    public FootballTeam(String name, int GlobalSkill, Map<Integer, FootballPlayer> newPlayers, int newAverage,int newNplayers){
        super(name,GlobalSkill);
        setPlayers(newPlayers);
        setAverageOverlSkill(newAverage);
        Nplayers = newNplayers;
    }

    /**
     * Construtor de FootballTeam
     * @param newTeam equipa a copiar
     */
    public FootballTeam(FootballTeam newTeam){
        super(newTeam);
        players = newTeam.getPlayers();
        AverageOverlSkill = newTeam.getAverageOverlSkill();
        Nplayers = newTeam.getNPlayers();
    }

    /**
     * Getter da lista de jogadores
     * @return lista de jogadores
     */
    public Map<Integer, FootballPlayer> getPlayers() {
        return players.entrySet().stream().collect(Collectors.toMap(e-> e.getKey(), e->e.getValue().clone()));
    }

    /**
     * Setter da lista de jogadores
     * @param newPlayers nova lista de jogadores
     */
    public void setPlayers(Map<Integer, FootballPlayer> newPlayers){
        players = newPlayers.entrySet().stream().collect(Collectors.toMap(e-> e.getKey(), e->e.getValue().clone()));
    }

    /**
     * Getter da habilidade media
     * @return habilidade media
     */
    public int getAverageOverlSkill(){
        return AverageOverlSkill;
    }

    /**
     * Setter da habilidade media
     * @param newAverage nova habilidade media
     */
    public void setAverageOverlSkill(int newAverage){
        AverageOverlSkill = newAverage;
    }

    /**
     * Getter do numero de jogadores
     * @return numero de jogadores
     */
    public int getNPlayers(){
        return Nplayers;
    }

    /**
     * Setter do numero de jogadores
     * @param n novo numero de jogadores
     */
    public void setNplayers(int n){
        Nplayers = n;
    }

    /**
     * Calcula a habilidade media da equipa
     * @return habilidade media
     */
    public int calcAverageSkill(){
        if(getNPlayers() > 0)
        return (int) players.values().stream().mapToInt(FootballPlayer::calcOverallSkill).sum() / getNPlayers();
        else return 0;
    }

    /**
     * Devolve um jogador com uma dada camisola
     * @param shirtNumber camisola do jogador a procurar
     * @return jogador
     */
    public FootballPlayer getPlayer(int shirtNumber){
        FootballPlayer p = null;
        if(players.containsKey(shirtNumber))
            p=players.get(shirtNumber).clone();
        return p;
    }

    /**
     * Verifica se existe algum jogador com uma dada camisola
     * @param shirtNumber camisola a procurar
     * @return resultado da procura
     */
    public boolean existsShirtNumber(int shirtNumber){
        return players.containsKey(shirtNumber);
    }

    /**
     * Verifica se exite algum jogador com um dado nome e numero de camisola
     * @param name nome do jogador procurado
     * @param shirtNumber camisola do jogador procurado
     * @return resultado da procura
     */
    public boolean existsPlayerNumber(String name, int shirtNumber){
        if(existsShirtNumber(shirtNumber)){
            return players.get(shirtNumber).getName().equals(name);
        }
        return false;
    }

    /**
     * Adiciona um novo jogador na equipa
     * Verifica se ainda da para adicionar jogadores e se o jogador nao
     * faz ja parte da equipa
     * @param p jogador a adicionar
     */
    public void addPlayer(FootballPlayer p){
        if(getNPlayers() < NumberOfPlayers && p!=null) {
            if(!existsPlayerNumber(p.getName(),p.getNumber())) {
                if (!p.getCurTeam().equals(this.getName())) {
                    p.setCurTeam(getName());
                    p.addBackground(getName());
                }
                players.put(p.getNumber(), p.clone());
                incTeamPlayers();
            }
        }
    }

    /**
     * Remove um jogador da equipa, caso ele faca parte da mesma
     * @param name nome do jogador a remover
     * @param shirtNumber camisola do jogador a remover
     */
    public void removePlayer(String name, int shirtNumber){
        if(getNPlayers() > 0) {
            if (existsPlayerNumber(name, shirtNumber)) {
                FootballPlayer p = players.get(shirtNumber);
                p.setCurTeamNone();
                players.remove(shirtNumber);
                decTeamPlayers();
            }
        }
    }

    /**
     * Remove o jogador com o numero de camisola dado
     * @param shirtNumber numero de camisola do jogador a remover
     */
    public void removePlayer(int shirtNumber){
        if(getNPlayers() > 0) {
            if (existsShirtNumber(shirtNumber)) {
                FootballPlayer p = players.get(shirtNumber);
                p.setCurTeamNone();
                players.remove(shirtNumber);
                decTeamPlayers();
            }
        }
    }

    /**
     * Incrementa o numero de jogadores
     */
    public void incTeamPlayers(){
        Nplayers++;
    }

    /**
     * Decrementa o numero de jogadores
     */
    public void decTeamPlayers(){
        Nplayers--;
    }

    /**
     * Devolve um jogador devolve um jogador da classe pedida como argumento, que nao se
     * encontre ja presente nos titulares ou suplentes
     * @param t classe procurada
     * @param Playinglist titulares
     * @param Substituteslist suplentes
     * @return jogador com a classe procurada caso possivel
     */
    public FootballPlayer getTypePlayer(Class<?> t,Set<FootballPlayer> Playinglist,Set<FootballPlayer> Substituteslist){
        if(getNPlayers() != 0) {
            Optional<FootballPlayer> p_type = players.values().stream()
                    .filter(p ->p.getClass() == t && (Playinglist.stream().noneMatch(k ->  k.getNumber() == (p.getNumber())))
                            && Substituteslist.stream().noneMatch(k -> k.getNumber() == (p.getNumber())))
                    .findFirst();
            if (p_type.isPresent()) return p_type.get().clone();
        }
        return null;
    }

    /**
     * Atualiza um dado jogador
     * @param p jogador a atualizar
     */
    public void updatePlayer(FootballPlayer p){
        if(this.players.values().stream().anyMatch(e->e.getName().equals(p.getName()) && e.getNumber() == p.getNumber()))
            players.replace(p.getNumber(),this.players.values().stream().filter(e->e.getName().equals(p.getName()) && e.getNumber() == p.getNumber()).findFirst().get(),p);

    }

    /**
     * Torna a lista de jogadores em uma string
     * @return lista de jogadores em formato de string
     */
    public String printPlayers(){
        StringBuilder sb = new StringBuilder();
        sb.append("Available Players ('Name','Shirt','OverallSkill'):\n");
        sb.append("Goalkeepers: "); players.values()
                .forEach(k->{
                    if(k instanceof Goalkeeper)
                        sb.append(k.getName()).append(" , ").append(k.getNumber()).append(" , ").append(k.calcOverallSkill()).append("/ ");
                });
        sb.append("\nDefenders: "); players.values()
                .forEach(k->{
                    if(k instanceof Defender)
                        sb.append(k.getName()).append(" , ").append(k.getNumber()).append(" , ").append(k.calcOverallSkill()).append("/ ");
                });
        sb.append("\nLateral: "); players.values()
                .forEach(k->{
                    if(k instanceof Lateral)
                        sb.append(k.getName()).append(" , ").append(k.getNumber()).append(" , ").append(k.calcOverallSkill()).append("/ ");
                });
        sb.append("\nMidfielders: "); players.values()
                .forEach(k->{
                    if(k instanceof Midfielder)
                        sb.append(k.getName()).append(" , ").append(k.getNumber()).append(" , ").append(k.calcOverallSkill()).append("/ ");
                });
        sb.append("\nStrikers: "); players.values()
                .forEach(k->{
                    if(k instanceof Striker)
                        sb.append(k.getName()).append(" , ").append(k.getNumber()).append(" , ").append(k.calcOverallSkill()).append("/ ");
                });
        return sb.toString();
    }

    /**
     * Equals da classe FootballTeam
     * @param o objeto a comparar
     * @return resultado da comparacao
     */
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FootballTeam t = (FootballTeam) o;
        boolean valid = true;
        for(FootballPlayer p : t.getPlayers().values()){
            valid = valid && players.containsValue(p);
        }
        return valid &&
                t.calcAverageSkill() == calcAverageSkill() &&
                t.getNPlayers() == t.getNPlayers() && super.equals(t);
    }

    /**
     * Devolve um inteiro correspondente a uma posicao de jogador
     * necessaria de preencher na equipa
     * @return posicao necessaria na equipa
     */
    public int typeNeeded(){
        if(Nplayers >= 22) return -1;
        if(players.values().stream().filter(k->k instanceof Goalkeeper).count() < 2) return 0;
        else if(players.values().stream().filter(k->k instanceof Defender).count() < 4) return 1;
        else if(players.values().stream().filter(k->k instanceof Lateral).count() < 4) return 2;
        else if(players.values().stream().filter(k->k instanceof Midfielder).count() < 6) return 3;
        else return 4;
    }


    /**
     * Clone de FootballTeam
     * @return clone
     */
    @Override
    public FootballTeam clone(){
        return new FootballTeam(this);
    }

    /**
     * ToString de FootballTeam
     * @return FootballTeam no formato string
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append("\nNumber of players: ")
                .append(getNPlayers()).append("\n");
        sb.append(printPlayers());
        sb.append("\nOverall Skill: ").append(calcAverageSkill()).append("\n");
        return sb.toString();
    }

    /**
     * Torna FootballTeam em uma string saveable
     * @return string saveable
     */
    public String toCSV() {
        return "Equipa:" + super.toCSV() + "\n";
    }

    /**
     * Carrega um FootballTeam a partir de uma string
     * @param csvLine string a analisar
     * @return equipa carregada
     */
    public static FootballTeam load(String csvLine) {
        return new FootballTeam(csvLine);
    }
}
