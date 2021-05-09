import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class readFile implements Serializable {

    public readFile(){

    }

    public State readState(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        State s;
        if((s = (State) ois.readObject())!=null) return s;
        return null;
    }





}
