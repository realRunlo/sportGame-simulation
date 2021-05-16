package viewer;

import java.util.Arrays;
import java.util.List;

public class SPORTMViewer {
    private static final List<String> Menu1 = Arrays.asList("Choose your option\n",
                                                            "[0]->Load State\n",
                                                            "[1]->Enter Game\n",
                                                            "[2]->Quit Game\n");
    private static final List<String> MenuLoadState = Arrays.asList("Choose your option\n",
                                                                    "[0]->Load default State\n",
                                                                    "[1]->Load custom State\n");


    public SPORTMViewer() {

    }

    public void showWelcome(){
        System.out.println("Welcome to SPORTM!");
        System.out.println("""
                Made by:
                ->Group 68
                ->Goncalo Braz Afonso a93178
                ->Goncalo Jose Teixeira Pereira a93168
                ->Marco Andre Pereira da Costa a93283""");

    }


    public void showInitialMenu(){
        System.out.println(Menu1.toString());
    }

    public void showLoadState(){
        System.out.println(MenuLoadState.toString());
    }


    public void showTermination(){
        System.out.println("Closing the Game\nWe hope you've enjoyed your time.");
    }

}
