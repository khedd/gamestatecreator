package graph.ai;


import graph.GameGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MaxPathScoring<T> extends ScoringPolicy<T> {

    /**
     * get the number of unique elements in the set
     * @param nodes
     * @return number of unique nodes
     */
    @Override
    public double evaluate(ArrayList<GameGraph.GameGraphNode<T>> nodes) {
        Set<GameGraph.GameGraphNode<T>> set = new HashSet<>(nodes);
        return set.size() + 1;
    }
}
