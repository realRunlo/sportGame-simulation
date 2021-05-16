package readFile;
import java.io.*;

import State.State;


public class readFile implements Serializable {

    public readFile(){

    }

    public State readState(String filename) throws IOException, ClassNotFoundException {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f.getName());
        ObjectInputStream ois = new ObjectInputStream(fis);
        State s;
        if((s = (State) ois.readObject())!=null) return s;
        return null;
    }





}
