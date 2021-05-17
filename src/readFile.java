import model.football.state.FootballState;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class readFile implements Serializable {

    public readFile(){

    }

    public FootballState readState(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        FootballState s;
        if((s = (FootballState) ois.readObject())!=null) return s;
        return null;
    }





}
