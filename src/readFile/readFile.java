package readFile;
import java.io.*;

import model.football.state.FootballState;


public class readFile implements Serializable {

    public readFile(){

    }

    public FootballState readState(String filename) throws IOException, ClassNotFoundException,FileNotFoundException {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f.getName());
        ObjectInputStream ois = new ObjectInputStream(fis);
        FootballState s;
        if((s = (FootballState) ois.readObject())!=null) return s;
        return null;
    }





}
