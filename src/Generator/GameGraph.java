package Generator;

import EscapeGame.EscapeScenarioCondition;
import EscapeGame.StateBinarization;

import java.util.*;

//graph is hold as an adjacency list
public class GameGraph<T> {


    static class GameGraphNode<T>{
        T vertex; /// vertex
        String edge; /// edge

        GameGraphNode(T vertex) {
            this.vertex = vertex;
            edge = "";
        }

        GameGraphNode(T vertex, String edge) {
            this.vertex = vertex;
            this.edge = edge;
        }
    }
    final HashMap<T, ArrayList< GameGraphNode<T>>> mGraph = new HashMap<>(); ///adjacency list
    private final Queue<T> mUnExplored = new LinkedList<>();

    boolean addNode(T node){
        if (mGraph.containsKey( node)){
            return false;
        }else {
            mGraph.put( node, new ArrayList<GameGraphNode<T>>());
            mUnExplored.add( node);
            return true;
        }
    }


    void addChildren(T node, ArrayList<GameGraphNode<T>> children){
        if ( mGraph.containsKey( node)) {
//            ArrayList<GameGraphNode<T>> entry = mGraph.get(vertex);
            mGraph.put( node, children);
            mUnExplored.remove( node);
            for ( GameGraphNode<T> ggn: children){
                addNode( ggn.vertex);
            }
        }
    }

    T getNext(){
        if ( mUnExplored.isEmpty())
            return null;
        return mUnExplored.peek();
    }
    boolean hasNext(){
        return  !( mUnExplored.isEmpty());
    }

    void print() {
        System.out.println( mGraph.size());
    }

    /**
     * prints the adjacency list in a top down approach
     */
    public void printTopDown ( StateBinarization stateBinarization){
        for ( Map.Entry<T, ArrayList<GameGraphNode<T>>> nodes: mGraph.entrySet()){
            EscapeScenarioCondition esc = stateBinarization.debinarize((Long) nodes.getKey());
//            esc.toString()
        }

    }


    Set<T> getVertices(){
        return mGraph.keySet();
    }

}
