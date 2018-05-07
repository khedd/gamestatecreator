import graph.GameGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static graph.GameGraph.UNVISITABLE_EDGE;

/**
 * simple memory class that can be used for graph algorithms
 */
public class Memory<T> {
    /**
     * Memory obtained from past runs
     */
    private final HashMap<T, ArrayList<GameGraph.GameGraphNode<T>>> mMemory = new HashMap<>();
    /**
     * determines the usage of memory when suggesting a child, memory always remembers but does
     * not interfere as long it is active. Think of a passive memory just records if false, does actions
     * based on the previous recordings if active
     */
    private boolean mIsActive = false;

    /**
     * initialize a graph using an adjacency graph
     * @param graph To be copied for to create a memory
     */
    Memory(HashMap<T, ArrayList<GameGraph.GameGraphNode<T>>> graph) {
        //deep copy the graph so that graph update or memory update will not
        //affect each other
        for ( Map.Entry<T, ArrayList<GameGraph.GameGraphNode<T>>> nodes: graph.entrySet()){
            ArrayList<GameGraph.GameGraphNode<T>> copyNodes = new ArrayList<>();

            for (GameGraph.GameGraphNode<T> gameGraphNode : nodes.getValue()) {
                GameGraph.GameGraphNode<T> copyGGN = new GameGraph.GameGraphNode<>( gameGraphNode);
                //to make sure in memory no visits are done yet
                if ( gameGraphNode.visitCount == UNVISITABLE_EDGE)
                    copyGGN.visitCount = 1; //make this node visited
                else
                    copyGGN.visitCount = 0;

                copyNodes.add( copyGGN);
            }
            mMemory.put( nodes.getKey(), copyNodes);
        }
    }

    /**
     * updates a node in memory by comparing with the original one, as we have a deep copy of the
     * original graph
     * @param parent Parent of the child in the original graph
     * @param child Child of the parent in the original graph
     */
    public void update(GameGraph.GameGraphNode<T> parent, GameGraph.GameGraphNode<T> child) {
        if ( parent != null & child != null){
            ArrayList<GameGraph.GameGraphNode<T>> gameGraphNodes = mMemory.get( parent.node);
            for (GameGraph.GameGraphNode<T> node: gameGraphNodes){
                if ( node.equals( child)){
                    node.visitCount++;
                    break;
                }
            }
        }
    }

    public GameGraph.GameGraphNode<T> selectChild (GameGraph.GameGraphNode<T> parent){
        GameGraph.GameGraphNode<T> minChild = null;
        if ((parent != null)){
            int minVisit = Integer.MAX_VALUE;
            ArrayList<GameGraph.GameGraphNode<T>> gameGraphNodes = mMemory.get( parent.node);
            for (GameGraph.GameGraphNode<T> node: gameGraphNodes){
                if ( minVisit > node.visitCount){
                    minVisit = node.visitCount;
                    minChild = node;
                }
            }
        }
        return minChild;
    }

    /**
     * asks whether to use memory
     * todo add different mechanisms to use the memory currently 10% chance to use
     * @return true if the memory is usable, false if not
     */
    public boolean useMemory (){
        Random random = new Random();
        int rand = random.nextInt(10);
        return mIsActive && (rand == 0);
    }

    /**
     * activates the usage of memory
     */
    public void activate() {
        mIsActive = true;
    }

    /**
     * deactivates the usage of memory
     */
    public void deactivate (){
        mIsActive = false;
    }

    /**
     * returns the HashMap of unvisited states and its edges, can be used to understand why a
     * persona did not visit that edge
     * @return HashMap representing unvisited edges coming out of a state
     */
    public HashMap<T, ArrayList<GameGraph.GameGraphNode<T>>> getUnvisited() {
        HashMap<T, ArrayList<GameGraph.GameGraphNode<T>>> unvisited = new HashMap<>();

        for ( Map.Entry<T, ArrayList<GameGraph.GameGraphNode<T>>> nodes: mMemory.entrySet()){
            ArrayList<GameGraph.GameGraphNode<T>> unvisitedNodes = new ArrayList<>();

            for (GameGraph.GameGraphNode<T> gameGraphNode : nodes.getValue()) {
                //copy the node just to remove the reference
                if ( gameGraphNode.visitCount == 0){
                    unvisitedNodes.add( new GameGraph.GameGraphNode<>(gameGraphNode));
                }
            }
            if (!unvisitedNodes.isEmpty())
                unvisited.put( nodes.getKey(), unvisitedNodes);
        }
        return unvisited;
    }
}
