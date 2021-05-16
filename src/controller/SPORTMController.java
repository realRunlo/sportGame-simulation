package controller;
import State.State;
import readFile.readFile;
import viewer.SPORTMViewer;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class SPORTMController implements Observer {
    private final SPORTMViewer view;
    private State state;
    private final Scanner scanner;
    private int option;

    public SPORTMController(){
        view = new SPORTMViewer();
        state = new State();
        scanner = new Scanner(System.in);
        option = 0;
    }

    public void run(){
        view.showWelcome();
        while(true){
        do {
            view.showInitialMenu();
            if(option <0 || option > 2) System.out.println("Please choose a valid option");
            option = scanner.nextInt();
        }while(option<0 || option > 2);

        switch (option) {
            case 0 -> loadState();
            case 1 -> System.out.println("Work in progress");
            case 2 -> terminate();
        }
        }
    }

    public void setState(State s){
        state = s.clone();
    }


    public void loadState(){
        option = 0;
        readFile r = new readFile();
        int fileRead = 0;
        do {
            do {
                view.showLoadState();
                option = scanner.nextInt();
            } while (option < 0 || option > 1);
            if (option == 0) {
                try {
                    setState(r.readState("estado.txt"));
                } catch (ClassNotFoundException e) {
                    System.out.println("Class not found");
                    fileRead = -1;
                } catch (IOException e) {
                    System.out.println("Error accessing the file");
                    e.printStackTrace();
                    fileRead = -1;
                }
            } else {
                try {
                    setState(r.readState(scanner.nextLine()));
                } catch (ClassNotFoundException e) {
                    System.out.println("Class not found");
                    fileRead = -1;
                } catch (IOException e) {
                    System.out.println("Error accessing the file");
                    fileRead = -1;
                }
            }
        }while(fileRead == -1);
    }

    public void terminate(){
        view.showTermination();
        System.exit(0);
    }


    @Override
    public void update(Observable o, Object s) {
        setState((State) s);
    }
}
