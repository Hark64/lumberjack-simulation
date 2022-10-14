import java.util.*;

public class NodeComparator implements Comparator<Node>{

    public int compare(Node a1, Node a2){
        return a1.getF() - a2.getF();
    }
}
