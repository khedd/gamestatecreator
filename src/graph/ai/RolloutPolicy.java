package graph.ai;

import graph.GameGraph;

import java.util.ArrayList;

public abstract class RolloutPolicy<T> {
    final ScoringPolicy<T> mScoringPolicy;

    public RolloutPolicy(ScoringPolicy scoringPolicy){
        mScoringPolicy = scoringPolicy;
    }
    /**
     * resets the current information
     */
    public abstract void reset();

    /**
     * select a new node from the child nodes
     * @param node Current Node
     * @param children children
     * @return
     */
    public abstract GameGraph.GameGraphNode<T> select(GameGraph.GameGraphNode<T> node, ArrayList<GameGraph.GameGraphNode<T>> children);
    public abstract T select(T node, ArrayList<GameGraph.GameGraphNode<T>> children);
}
