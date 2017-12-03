import java.util.*;

//graph is hold as an adjacency list
public class GameGraph<T> {


    public static class GameGraphNode<T>{
        T node; /// vertex
        String action; /// edge
    }
    final HashMap<T, ArrayList< GameGraphNode<T>>> mGraph = new HashMap<>(); ///adjacency list
    final Queue<T> mUnExplored = new LinkedList<>();

    public boolean addNode ( T node){
        if (mGraph.containsKey( node)){
            return false;
        }else {
            mGraph.put( node, new ArrayList<GameGraphNode<T>>());
            mUnExplored.add( node);
            return true;
        }
    }


    public boolean addChildren ( T node,  ArrayList< GameGraphNode<T>> children){
        if ( mGraph.containsKey( node)) {
//            ArrayList<GameGraphNode<T>> entry = mGraph.get(node);
            mGraph.put( node, children);
            mUnExplored.remove( node);
            for ( GameGraphNode<T> ggn: children){
                addNode( ggn.node);
            }
            return true;
        }
        return false;
    }

    public T getNext (){
        if ( mUnExplored.isEmpty())
            return null;
        return mUnExplored.peek();
    }
    public boolean hasNext (){
        return  !( mUnExplored.isEmpty());
    }

    public void print() {
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

    public Set<T> getVertices (){
        return mGraph.keySet();
    }


    public int calculateCyclometicComplexity (){
        int totalNodes = mGraph.size();
        int totalEdges = 0;
        int exits = 0;
        for ( Map.Entry<T, ArrayList<GameGraphNode<T>>> nodes: mGraph.entrySet()){
            int size = nodes.getValue().size();
            if ( size == 0){
                exits++;
            }else{
                totalEdges += size;
            }
        }
        System.out.println(" Total Edges: " + totalEdges + " totalNodes: " + totalNodes + " exits: " + exits);
        int complexity = totalEdges - totalNodes + 2 * exits;
        return complexity;


    }
}
