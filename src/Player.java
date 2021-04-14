import java.util.ArrayList;
import java.util.List;


public class Player {
    private String name;
    private List<String> background;
    //talvez se possa adicionar mais coisas depois
    //algumas do diagrama fazem mais sentido na subclasse

    public Player(){
        name = " ";
        background = null;
    }

    public Player(String nName, List<String> newBackground){
        name = nName;
        setBackground(newBackground);
    }

    public Player(Player p){
        name = p.getName();
        background = p.getBackground();
    }

    public String getName(){
        return name;
    }

    public void setName(String newName){
        name = newName;
    }

    public List<String> getBackground(){
        List<String> b = new ArrayList<>();
        background.forEach(a-> b.add(a));
        return b;
    }

    public void setBackground(List<String> s){
        s.forEach(a-> background.add(a));
    }

}
