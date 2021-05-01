import generic.Game.Game;

public class Interface {
    public static void main(String[] args){
        System.out.println("Hello world!");
        Game g = new Game();
        System.out.println("Teste de novo jogo:\n" + g.toString());

        State s = new State();
        System.out.println("Teste de novo estado:\n" + s.toString());
        System.out.println("Program finished!");
    }
}
