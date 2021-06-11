package model.football.team.lineup;

import model.exceptions.PlayerDoenstExist;
import model.football.game.Substitution;
import model.football.player.*;
import model.football.team.FootballTeam;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class FootballLineup implements Serializable {
    private String teamName;
    private int strategy;
    private int globalSkill;
    private Set<FootballPlayer> playing;
    private Set<FootballPlayer> substitutes;
    private List<Substitution> substitutions;
    private static final int MAX_SUBSTITUTES = 11;

    /**
     * Construtor da classe FootballLineup
     */
    public FootballLineup() {
        setTeamName("None");
        setStrategy(1);
        setGlobalSkill(0);
        setPlaying(new HashSet<>());
        setSubstitutes(new HashSet<>());
        setSubstituitions(new ArrayList<>());
    }

    /**
     * Construtor da classe FootballLineup
     * @param name nome da equipa do plantel
     * @param s estrategia a implementar
     * @param playing lista dos jogadores titulares
     * @param substitutes lista dos jogadores suplentes
     * @param substitutions lista das substituicoes
     */
    public FootballLineup(String name,int s,Set<FootballPlayer> playing, Set<FootballPlayer> substitutes, List<Substitution> substitutions) {
        setTeamName(name);
        setStrategy(s);
        setPlaying(playing);
        setSubstitutes(substitutes);
        setGlobalSkill(calcGlobalSkill());
        setSubstituitions(substitutions);
    }

    /**
     * Construtor da classe FootballLineup
     * @param lineup plantel a copiar
     */
    public FootballLineup(FootballLineup lineup) {
        this.setTeamName(lineup.getTeamName());
        this.setStrategy(lineup.getStrategy());
        this.setPlaying(lineup.getPlaying());
        this.setSubstitutes(lineup.getSubstitutes());
        this.setSubstituitions(lineup.getSubstituitions());
        this.setGlobalSkill(calcGlobalSkill());
    }

    /**
     * Construtor da classe FootballLineup
     * Automaticamente gera os titulares e suplentes utilizando a estrategia
     * provida como argumento
     * @param t equipa do plantel
     * @param s estrategia a implementar
     */
    public FootballLineup(FootballTeam t,int s){
        setTeamName(t.getName());
        setStrategy(s);
        setPlaying(new HashSet<>());
        setSubstitutes(new HashSet<>());
        substitutions = new ArrayList<>();
        //Adicionar os titulares
        addPlaying(t.getTypePlayer(Goalkeeper.class,playing,substitutes));
        addPlayerTypeToGroup(t,Defender.class,true);
        addPlayerTypeToGroup(t, Lateral.class,true);
        addPlayerTypeToGroup(t, Midfielder.class,true);
        addPlayerTypeToGroup(t, Striker.class,true);


        //Adicionar os substitutos
        addSubstitute(t.getTypePlayer(Goalkeeper.class,playing,substitutes));
        addPlayerTypeToGroup(t,Defender.class,false);
        addPlayerTypeToGroup(t,Lateral.class,false);
        addPlayerTypeToGroup(t,Midfielder.class,false);
        addPlayerTypeToGroup(t,Striker.class,false);

        setGlobalSkill(calcGlobalSkill());
    }

    /**
     * Getter do nome do plantel
     * @return nome do plantel
     */
    public String getTeamName(){
        return teamName;
    }

    /**
     * Setter do nome do plantel
     * @param name nome do plantel
     */
    public void setTeamName(String name){
        teamName = name;
    }

    /**
     * Getter da estrategia
     * @return estrategia
     */
    public int getStrategy(){
        return strategy;
    }

    /**
     * Setter da estrategia
     * @param s nova estrategia
     */
    public void setStrategy(int s){
        strategy = strategyNormalize(s);
    }

    /**
     * Setter da habilidade global
     * @param globalSkill nova habilidade global
     */
    private void setGlobalSkill(int globalSkill) {
        this.globalSkill = globalSkill;
    }

    /**
     * Getter da habilidade global
     * @return habilidade global
     */
    public int getGlobalSkill() {
        return globalSkill;
    }

    /**
     * Setter dos titulares
     * @param playing novos titulares
     */
    private void setPlaying(Set<FootballPlayer> playing) {
        this.playing = new HashSet<>();
        playing.forEach(v -> this.playing.add(v.clone()));
    }

    /**
     * Getter dos titulares
     * @return titulares
     */
    public Set<FootballPlayer> getPlaying() {
        return playing.stream().map(FootballPlayer::clone).collect(Collectors.toSet());
    }

    /**
     * Setter dos suplentes
     * @param substitutes novos suplentes
     */
    private void setSubstitutes(Set<FootballPlayer> substitutes) {
        this.substitutes = new HashSet<>();
        substitutes.forEach(v -> this.substitutes.add(v.clone()));
    }

    /**
     * Getter dos suplentes
     * @return suplentes
     */
    public Set<FootballPlayer> getSubstitutes() {
        return substitutes.stream().map(FootballPlayer::clone).collect(Collectors.toSet());
    }

    /**
     * Getter das substituicoes
     * @return substituicoes
     */
    public List<Substitution> getSubstituitions() {
        return this.substitutions.stream()
                                  .map(Substitution::clone)
                                  .collect(Collectors.toList());
    }

    /**
     * Setter das substituicoes
     * @param substitutions novas substituicoes
     */
    public void setSubstituitions(List<Substitution> substitutions) {
        this.substitutions = substitutions.stream()
                                            .map(Substitution::clone)
                                            .collect(Collectors.toList());
    }

    /**
     * Calculo da habilidade global do plantel
     * @return habilidade global do plantel
     */
    public int calcGlobalSkill() {
        int sum = 0;
        int numPlayers = 0;

        Set<FootballPlayer> playing = getPlaying();
        Set<FootballPlayer> substitutes = getSubstitutes();

        numPlayers = playing.size() + substitutes.size();

        for(FootballPlayer player: playing)
            if(player != null) sum += player.getOverallSkill();
        for(FootballPlayer player: substitutes)
            if(player!=null) sum += player.getOverallSkill();

        if(numPlayers > 0)
            sum /= numPlayers;

        return sum;
    }

    /**
     * Procura por um jogador de uma dada classe nos titulares ou suplentes
     * @param player classe procurada
     * @param play booleano que indica se procura nos titulares ou suplentes
     * @return jogador da classe pedida caso exista
     * @throws PlayerDoenstExist
     */
    public FootballPlayer getPlayer(Class<?> player,boolean play) throws PlayerDoenstExist {
        List<FootballPlayer> list;
        Random rand = new Random();
        if(player != null) {
            if (play) {
                if(getPlaying().stream().anyMatch(e -> e.getClass().equals(player))) {
                    list = getPlaying().stream().filter(e -> e.getClass().equals(player)).collect(Collectors.toList());
                    return list.get(rand.nextInt(list.size()));
                }
                else throw new PlayerDoenstExist(player.getName());
            } else {
                if(getSubstitutes().stream().anyMatch(e -> e.getClass().equals(player))) {
                    list = getSubstitutes().stream().filter(e -> e.getClass().equals(player)).collect(Collectors.toList());
                    return list.get(rand.nextInt(list.size()));
                }
                else throw new PlayerDoenstExist(player.getName());
            }
        }
        else return null;
    }

    /**
     * Remove um jogador pedido dos titulares
     * @param player jogador a remover
     */
    public void remPlaying(FootballPlayer player) {
        Set<FootballPlayer> playing = this.getPlaying();

        playing.remove(player);

        this.setPlaying(playing);
        this.setGlobalSkill(this.calcGlobalSkill());
    }

    /**
     * Remove um jogador com uma dada camisola dos titulares
     * @param shirt camisola do jogador a remover
     */
    public void remPlaying(int shirt){
        if(playing.stream().anyMatch(k->k.getNumber() == shirt)) playing.removeIf(k->k.getNumber() == shirt);
        this.setGlobalSkill(this.calcGlobalSkill());
    }

    /**
     * Adiciona um jogador aos titulares
     * @param player jogador a adicionar
     * @return booleano indicador do sucesso da adicao
     */
    public boolean addPlaying(FootballPlayer player) {
        boolean added = false;
        if(player!=null) {
            Set<FootballPlayer> playing = getPlaying();
            if (playing.size() < 12 && availableSpotInStrategy(player.getClass())
                    && playing.stream().noneMatch(k->k.getNumber() == player.getNumber()))
            {
                playing.add(player);
                setPlaying(playing);
                added = true;
                this.setGlobalSkill(this.calcGlobalSkill());
            }
        }
        return added;
    }

    /**
     * Verifica se um jogador ja foi adicionado ao plantel
     * @param p jogador a procurar
     * @return booleano indicador do sucesso da procura
     */
    public boolean playerAdded(FootballPlayer p){
        boolean exists = false;
        if(playing.stream().anyMatch(k->k.getNumber() == p.getNumber())) exists = true;
        if(substitutes.stream().anyMatch(k->k.getNumber() == p.getNumber())) exists = true;
        return exists;
    }

    /**
     * Verifica se ainda ha espaco nos titulares para uma certa classe tendo em conta a
     * estrategia implementada pelo plantel
     * @param p classe em questao
     * @return booleano representante da existencia ou nao de espaco no plantel
     */
    public boolean availableSpotInStrategy(Class<? extends FootballPlayer> p){
        boolean available = false;
        //verificar guarda-redes
        if(p.equals(Goalkeeper.class) && playing.stream().noneMatch(k -> k instanceof Goalkeeper))
            available = true;
        else if(playing.stream().filter(k -> k.getClass().equals(p)).count() < numberOfType(p))
            available = true;
        return available;
    }


    /**
     * Procura um jogador de um certo tipo, de uma equipa aos titulares ou suplentes
     * @param t equipa onde se vai buscar o jogador
     * @param playerType tipo de jogador a adicionar
     * @param playing booleano que indica se se pretende adicionar nos titulares ou suplentes
     */
    public void addPlayerTypeToGroup(FootballTeam t,Class<?> playerType, boolean playing){
        int nToAdd = 0;
        if(playing) {
            nToAdd = numberOfType(playerType);
            for(int i =0; i<nToAdd;i++) addPlaying(t.getTypePlayer(playerType,getPlaying(),getSubstitutes()));
        }
        else{
            nToAdd = 2;
            for(int i =0; i<nToAdd;i++) addSubstitute(t.getTypePlayer(playerType,getPlaying(),getSubstitutes()));
        }
    }

    /**
     * Remove um suplente
     * @param player suplente a remover
     */
    public void remSubstitute(FootballPlayer player) {
        Set<FootballPlayer> subs = this.getSubstitutes();

        subs.remove(player);

        this.setSubstitutes(subs);
        this.setGlobalSkill(this.calcGlobalSkill());
    }

    /**
     * Remove um suplente com uma dada camisola
     * @param shirt camisola do suplente a remover
     */
    public void remSubstitute(int shirt){
        if(substitutes.stream().anyMatch(k->k.getNumber() == shirt)) substitutes.removeIf(k->k.getNumber() == shirt);
    }

    /**
     * Adiciona um suplente
     * @param player suplente a adicionar
     * @return booleano indicador do sucesso da adicao
     */
    public boolean addSubstitute(FootballPlayer player) {
        boolean added = false;
        if(player!=null) {
            Set<FootballPlayer> substitutes = getSubstitutes();
            if (substitutes.size() < MAX_SUBSTITUTES && substitutes.stream().noneMatch(k->k.getNumber() == player.getNumber()))
            {
                substitutes.add(player);
                setSubstitutes(substitutes);
                this.setGlobalSkill(this.calcGlobalSkill());
                added = true;
            }
        }
        return added;
    }

    /**
     * Adiciona uma substituicao
     * @param pOut jogador a ir para o banco
     * @param pIn jogador a ir jogar
     */
    public void addSubstituition(FootballPlayer pOut, FootballPlayer pIn) {
        this.substitutions.add(new Substitution(pOut,pIn));
        this.setGlobalSkill(this.calcGlobalSkill());
    }

    /**
     * Faz uma substituicao
     * @param player jogador a sair
     * @param sub jogador a entrar
     */
    public void substitutePlayer(FootballPlayer player, FootballPlayer sub) {
        remPlaying(player);
        remSubstitute(sub);

        addPlaying(sub);
        addSubstitute(player);

        addSubstituition(player,sub);
    }

    /**
     * Realiza uma substituicao utilizando como referencia as camisolas dos jogadores
     * @param playing camisola do jogador a sair
     * @param sub camisola do jogador a entrar
     * @return booleano indicador do sucesso da substituicao
     */
    public boolean substitutePlayer(int playing, int sub) {
        Optional<FootballPlayer> p = getPlaying().stream().filter(k->k.getNumber() == playing).findFirst();
        Optional<FootballPlayer> s = getSubstitutes().stream().filter(k->k.getNumber() == sub).findFirst();
        if(p.isPresent() && s.isPresent()) {
            if(p.get().getClass().getName().equals(s.get().getClass().getName())) {

                remPlaying(playing);
                remSubstitute(sub);
                addPlaying(s.get());
                addSubstitute(p.get());
                addSubstituition(p.get(),s.get());
                return true;
            }
        }
        return false;
    }

    /**
     * Indica o numero de jogadores titulares ou suplentes
     * @param playing booleano indicador se se pretende saber a quantidade de titulares
     *                ou suplentes
     * @return quantidade
     */
    public int numberOf(boolean playing){
        if (playing) return this.playing.size();
        else return this.substitutes.size();
    }

    /**
     * Indica o numero maximo de jogadores de um certo tipo numa dada tatica
     * @param player_type tipo de jogador
     * @return quantidade maximo desse tipo de jogador
     */
    public int numberOfType(Class<?> player_type){
        if(getStrategy() == 1){ //4-4-2
            if (Defender.class.equals(player_type)) return 2;
            else if(Lateral.class.equals(player_type)) return 2;
            else if (Midfielder.class.equals(player_type)) return 4;
            else return 2;
        }
        else{ //4-3-3
            if (Defender.class.equals(player_type)) return 2;
            else if(Lateral.class.equals(player_type)) return 2;
            else if (Midfielder.class.equals(player_type)) return 3;
            else return 3;
        }
    }

    /**
     * Metodo que verifica se um plantel esta pronto para realizar uma partida
     * @return booleano representante da preparacao do plantel
     */
    public boolean readyToPlay(){
        //para estar pronto para jogar, tem de ter um jogador de cada tipo na lineUp
        return playing.stream().anyMatch(p -> p instanceof Goalkeeper)
                && playing.stream().anyMatch(p -> p instanceof Defender)
                && playing.stream().anyMatch(p -> p instanceof Lateral)
                && playing.stream().anyMatch(p -> p instanceof Midfielder)
                && playing.stream().anyMatch(p -> p instanceof Striker);
    }

    /**
     * Metodo que verifica se um plantel possui um jogador com uma dada camisola
     * @param shirt camisola a procurar
     * @param playing booleano representante do local de procura, titulares ou suplentes
     * @return resultado da procura
     */
    public boolean existsPlayer(int shirt,boolean playing){
        if(playing) return getPlaying().stream().anyMatch(k->k.getNumber() == shirt);
        else return getSubstitutes().stream().anyMatch(k->k.getNumber() == shirt);
    }

    /**
     * Normaliza o valor da estrategia
     * @param strategy valor da estrategia
     * @return valor normalizado da estrategia
     */
    public int strategyNormalize(int strategy){
        if(strategy == 1) return 1;
        else if(strategy == 2) return 2;
            else return 1;
    }

    /**
     * Torna a lista de titulares em formato string
     * @return string dos titulares
     */
    public String printPlaying(){
        StringBuilder sb = new StringBuilder();

        sb.append("Playing : ");
        playing.forEach(k-> sb.append(k.getClass().getSimpleName().charAt(0))
                .append("->").append(k.getName())
                .append(" - ").append(k.getNumber())
                .append("/ "));
        return sb.toString();
    }

    /**
     * Torna a lista de suplentes em formato string
     * @return string dos suplentes
     */
    public String printSubstitutes(){
        StringBuilder sb = new StringBuilder();

        sb.append("Substitutes : ");
        substitutes.forEach(k-> sb.append(k.getClass().getSimpleName().charAt(0))
                .append("->").append(k.getName())
                .append(" - ").append(k.getNumber())
                .append("/ "));
        return sb.toString();
    }

    /**
     * Equals de FootballLineup
     * @param o objeto a comparar
     * @return resultado da comparacao
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FootballLineup lp = (FootballLineup) o;
        boolean valid = true;
        for(FootballPlayer p : lp.getPlaying()){
            valid = valid && playing.contains(p);
        }
        if(valid){
            for(FootballPlayer p : lp.getSubstitutes()){
                valid = valid && substitutes.contains(p);
            }
        }
        if(valid){
            for(Substitution s : lp.getSubstituitions()){
                valid = valid && substitutions.contains(s);
            }
        }
        return valid &&
                lp.getStrategy() == strategy &&
                lp.getTeamName().equals(teamName) &&
                lp.calcGlobalSkill() == calcGlobalSkill();
    }

    /**
     * ToString de FootballLineup
     * @return FootballLineup em formato string
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(teamName).append("\n")
                .append("Playing: ");
        playing.forEach(p-> sb.append(p.getName()).append(", "));
        sb.append("\nSubstitutes: ");
        substitutes.forEach(p-> sb.append(p.getName()).append(", "));
        sb.append("\n");
        return sb.toString();
    }
}
