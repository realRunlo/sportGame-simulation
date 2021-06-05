package viewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SPORTMViewer {
    // Interfaces auxiliares

    /** Functional interface para handlers. */
    public interface Handler {  // método de tratamento
        public void execute() throws IOException, ClassNotFoundException;
    }

    /** Functional interface para pré-condições. */
    public interface PreCondition {  // Predicate ?
        public boolean validate();
    }

    // Varíável de classe para suportar leitura

    private static Scanner is = new Scanner(System.in);

    // Variáveis de instância

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";


    private List<String> options;            // Lista de opções
    private List<PreCondition> available;  // Lista de pré-condições
    private List<Handler> handlers;         // Lista de handlers
    private boolean exit = false;

    public SPORTMViewer(String[] options){
        this.options = Arrays.asList(options);
        this.available = new ArrayList<>();
        this.handlers = new ArrayList<>();
        this.options.forEach(s-> {
            this.available.add(()->true);
            this.handlers.add(()->System.out.println(RED+"Option not implemented!"+RESET));
        });
    }

    public void SimpleRun() throws IOException, ClassNotFoundException {
        int op;
        do {
            SimpleShow();
            op = readOption();
            // testar pré-condição
            if (op>0 && !this.available.get(op).validate()) {
                System.out.println(RED +"Option not available"+ RESET);
            } else if (op>0) {
                // executar handler
                this.handlers.get(op).execute();
            }
        } while (op != 0 && !exit);
    }

    /**
     * Método que regista uma uma pré-condição numa opção do NewMenu.
     *
     * @param i índice da opção (começa em 1)
     * @param b pré-condição a registar
     */
    public void setPreCondition(int i, PreCondition b) {
        this.available.set(i,b);
    }

    public void setSamePreCondition(int[] list, PreCondition b) {
        for(int i:list) this.available.set(i,b);

    }

    /**
     * Método para registar um handler numa opção do NewMenu.
     *
     * @param i indice da opção  (começa em 1)
     * @param h handlers a registar
     */
    public void setHandler(int i, Handler h) {
        this.handlers.set(i, h);
    }

    // Métodos auxiliares

    /** Apresentar o NewMenu */
    private void SimpleShow() {
        System.out.println(YELLOW + this.options.get(0) + RESET);
        for (int i=1; i<this.options.size(); i++) {
            System.out.print(i);
            System.out.print(" - ");
            System.out.println(this.available.get(i).validate() ? this.options.get(i) : "---");
        }
        System.out.println("0 - Return");
    }

    /** Ler uma opção válida */
    private int readOption() {
        int op;
        //Scanner is = new Scanner(System.in);

        informationMessage("Option: ");
        try {
            String line = is.nextLine();
            op = Integer.parseInt(line);
        }
        catch (NumberFormatException e) { // Não foi inscrito um int
            op = -1;
        }
        if (op<0 || op>=this.options.size()) {
            System.out.println(RED +"Invalid Option"+RESET);
            op = -1;
        }
        return op;
    }

    public int readOptionBetween(int lower, int higher, String[] options) {
        int op = -1;
        //Scanner is = new Scanner(System.in);
        int i = 0;
        if(options != null) {
            while (i < options.length) {
                System.out.println(i + " - " + options[i]);
                i++;
            }
        }
        informationMessage("Write a number between "+lower+" and "+higher+"\nOption: ");
        while(op == -1) {
            try {
                String line = is.nextLine();
                op = Integer.parseInt(line);
            } catch (NumberFormatException e) { // Não foi inscrito um int
                op = -1;
            }
            if (op < lower || op > higher) {
                System.out.println(RED + "Invalid Option" + RESET);
                op = -1;
            }
        }
        return op;
    }

    public void showInfo(Object o){
        System.out.println(o.toString());
        informationMessage("Press any key to continue");
        is.nextLine();
    }


    public void returnMenu(){
        exit = true;
    }

    public void titleMessage(String message){
        System.out.println(YELLOW +message+RESET);
    }

    public void confirmationMessage(String message){
        System.out.println(GREEN + message + RESET);
    }

    public void informationMessage(String message){
        System.out.println(PURPLE + message + RESET);
    }

    public void normalMessage(String message){
        System.out.println(message);
    }

    public void errorMessage(String error){
        System.out.println(RED + error + RESET);
    }


    public final static void clearConsole()
    {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e)
        {
            //  Handle any exceptions.
        }
    }

    }

