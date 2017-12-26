package graph.ai;

import graph.GameGraph;
import graph.MCTS;

import java.util.ArrayList;
import java.util.Random;


public class RandomRollout<T> extends MCTS.RolloutPolicy<T> {
    public int mMaximumRollout = 10;
    private ArrayList<T> mNodesVisited = new ArrayList<>();


    public RandomRollout(MCTS.ScoringPolicy<T> scoringPolicy, int maximumRollout) {
        super(scoringPolicy);
        mMaximumRollout = maximumRollout;
    }

    @Override
    public void reset() {

    }

    @Override
    public T select(T node, ArrayList<GameGraph.GameGraphNode<T>> children) {
        if ( children.isEmpty())
            return null;
        //get random
        int childrenCount = children.size();
        Random random = new Random();
        // FIXME: 26.12.2017 for deugging purposes
        int randInt = random.nextInt( childrenCount);
        T nodeRolled = children.get( randInt).node;
        mNodesVisited.add( nodeRolled);
        return nodeRolled;
    }



}
