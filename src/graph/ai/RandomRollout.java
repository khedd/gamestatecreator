package graph.ai;

import graph.GameGraph;

import java.util.ArrayList;
import java.util.Random;


public class RandomRollout<T> extends RolloutPolicy<T> {
    public int mMaximumRollout = 10;
    private ArrayList<T> mNodesVisited = new ArrayList<>();


    public RandomRollout(ScoringPolicy<T> scoringPolicy, int maximumRollout) {
        super(scoringPolicy);
        mMaximumRollout = maximumRollout;
    }

    @Override
    public void reset() {

    }

    @Override
    public GameGraph.GameGraphNode<T> select(GameGraph.GameGraphNode<T> node, ArrayList<GameGraph.GameGraphNode<T>> children) {
        if ( children.isEmpty())
            return null;
        //get random
        int childrenCount = children.size();
        Random random = new Random();
        int randInt = random.nextInt( childrenCount);
        GameGraph.GameGraphNode<T> nodeRolled = children.get( randInt);
//        mNodesVisited.add( nodeRolled);
        return nodeRolled;
    }

    @Override
    public T select(T node, ArrayList<GameGraph.GameGraphNode<T>> children) {
        if ( children.isEmpty())
            return null;
        //get random
        int childrenCount = children.size();
        Random random = new Random();
        int randInt = random.nextInt( childrenCount);
        T nodeRolled = children.get( randInt).node;
        mNodesVisited.add( nodeRolled);
        return nodeRolled;
    }



}
