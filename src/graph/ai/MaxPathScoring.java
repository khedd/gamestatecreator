package graph.ai;

import graph.MCTS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MaxPathScoring<T> extends MCTS.ScoringPolicy<T> {
    /**
     * return 1 as it will score just one node
     * @param node
     * @return
     */
    @Override
    public double evaluate(T node) {
        return 1;
    }

    /**
     * get the number of unique elements in the set
     * @param nodes
     * @return
     */
    @Override
    public double evaluate(ArrayList<T> nodes) {
        Set<T> set = new HashSet<>();
        set.addAll( nodes);
        return set.size();
    }
}
